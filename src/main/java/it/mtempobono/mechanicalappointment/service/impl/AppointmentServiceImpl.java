package it.mtempobono.mechanicalappointment.service.impl;

import com.google.api.client.util.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.builder.VoteBuilder;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentSearchDto;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentVote;
import it.mtempobono.mechanicalappointment.model.GoogleCalendarCreateEvent;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.builder.AppointmentBuilder;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.CustomAppointmentEvaluation;
import it.mtempobono.mechanicalappointment.model.entity.*;
import it.mtempobono.mechanicalappointment.repository.*;
import it.mtempobono.mechanicalappointment.service.AppointmentService;
import it.mtempobono.mechanicalappointment.util.CalculatedTimeslots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    //region Fields

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    private final AppointmentCore appointmentCore = new AppointmentCore();
    @Autowired
    private EmailService emailService;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OpenDayRepository openDayRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MechanicalActionRepository mechanicalActionRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;
    //endregion

    // region Public Methods

    // endregion Public Methods

    // region CRUD Methods

    /**
     * Get all the appointments
     *
     * @return the list of appointments
     */
    @Override
    public ResponseEntity<List<Appointment>> findAll() {
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(appointmentRepository.findAll());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());

        } finally {
            logger.debug("Exit from findAll() method");
        }
    }

    /**
     * Get the appointment by id
     *
     * @param id the appointment id
     * @return the appointment
     */
    @Override
    public ResponseEntity<Appointment> findById(Long id) {
        try {
            logger.info("findById() called with id: {}", id);
            Optional<Appointment> appointment = appointmentRepository.findById(id);
            return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());

        } finally {
            logger.debug("Exit from findById() method");
        }
    }

    private String createCalendarEvent(OpenDay openDay, AppointmentDto appointmentDto, Vehicle vehicle, MechanicalAction mechanicalAction){
        try {

            GoogleCalendarCreateEvent googleCalendarCreateEvent = new GoogleCalendarCreateEvent();
            googleCalendarCreateEvent.setSummary("Appointment");
            googleCalendarCreateEvent.setDescription("Appointment for " + vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getPlate() + " for " + mechanicalAction.getName());

            LocalDate day = openDay.getDate();
            LocalTime startTime = appointmentDto.getTimeSlotSelected().getStart().getLocalTime();
            LocalTime endTime = appointmentDto.getTimeSlotSelected().getEnd().getLocalTime();

            DateTime startCalendarTime = new DateTime(
                    (LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), startTime.getHour(), startTime.getMinute(), 0).format(DateTimeFormatter.ISO_DATE_TIME)));

            DateTime endCalendarTime = new DateTime(
                    (LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), endTime.getHour(), endTime.getMinute(), 0).format(DateTimeFormatter.ISO_DATE_TIME)));

            googleCalendarCreateEvent.setStartTime(startCalendarTime);
            googleCalendarCreateEvent.setEndTime(endCalendarTime);
            googleCalendarCreateEvent.setLocation(openDay.getGarage().getAddress());

            return googleCalendarService.addEvent(googleCalendarCreateEvent);
        } catch (Exception e) {
            logger.warn("Error in createCalendarEvent() method: {}", e.getMessage());

        } finally {
            logger.debug("Exit from createCalendarEvent() method");
        }
        return "";
    }

    /**
     * Create a new appointment
     *
     * @param appointmentDto the appointment to create
     * @return the created appointment
     */
    @Override
    public ResponseEntity<Appointment> save(AppointmentDto appointmentDto) throws Exception {
        try {
            logger.info("save() called with appointment: {}", appointmentDto);

            // Retrieve the linked open day by id
            OpenDay openDay = openDayRepository.findById(appointmentDto.getOpenDayId()).orElseThrow(() -> new RuntimeException("Open day not found"));

            // Retrieve the linked vehicle by id
            Vehicle vehicle = vehicleRepository.findById(appointmentDto.getVehicleId()).orElse(null);

            // Check that vehicle exists
            if (vehicle == null) {
                logger.error("Vehicle not found");
                throw new RuntimeException("Vehicle not found");
            }

            // Check that appointment status is not null and valid.
            AppointmentStatus appointmentStatus = AppointmentStatus.AWAITING_APPROVAL;

            Appointment newAppointment;
            if (appointmentDto.getIsMechanicalActionCustom()) {
                newAppointment = appointmentRepository.save(createCustomAppointment(appointmentDto, openDay, vehicle, appointmentStatus));
            } else {
                newAppointment = appointmentRepository.save(createStockAppointment(appointmentDto, openDay, vehicle, appointmentStatus));
            }

            // Save the appointment
            Appointment savedAppointment = appointmentRepository.save(newAppointment);
            emailService.sendNewAppointmentMail(savedAppointment.getId());

            return ResponseEntity.ok(savedAppointment);

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
            throw e;
        } finally {
            logger.debug("Exit from save() method");
        }
    }

    private Appointment createCustomAppointment(AppointmentDto appointmentDto, OpenDay openDay, Vehicle vehicle, AppointmentStatus appointmentStatus) {
        MechanicalAction mechanicalAction = mechanicalActionRepository.findByName("CUSTOM");

        // Create the appointment using dto data and builder class
        return AppointmentBuilder.anAppointment()
                .openDay(openDay)
                .comment(appointmentDto.getComment())
                .price(mechanicalAction.getPrice())
                .mechanicalAction(mechanicalAction)
                .vehicle(vehicle)
                .status(appointmentStatus)
                .internalTime(new TimePeriod(LocalTime.of(0, 0), LocalTime.of(0, 0)))
                .externalTime(new TimePeriod(LocalTime.of(0, 0), LocalTime.of(0, 0)))
                .idCalendarEvent(null)
                .mechanicalActionCustom(true)
                .build();

    }

    public Appointment createStockAppointment(AppointmentDto appointmentDto, OpenDay openDay, Vehicle vehicle, AppointmentStatus appointmentStatus) throws Exception {
        // Retrieve the linked mechanical action by id
        MechanicalAction mechanicalAction = mechanicalActionRepository.findById(appointmentDto.getMechanicalActionId()).orElseThrow(() -> new RuntimeException("Mechanical action not found"));

        if (mechanicalAction.getName().equals("CUSTOM")) throw new Exception("Mechanical action not found");

        // Check that internal time is not null and that the start time is before the end time
        if (appointmentDto.getTimeSlotSelected().getStart() != null || appointmentDto.getTimeSlotSelected().getEnd() != null) {

//            List<TimePeriod> availableHours = appointmentCore.getAvailableAppointments(openDay, mechanicalAction).getAvailableHoursOnExternalTime();
            CalculatedTimeslots slots = appointmentCore.getAvailableAppointments(openDay, mechanicalAction);

            List<TimePeriod> availableHoursInternal = slots.getAvailableHoursOnInteralTime();
            List<TimePeriod> availableHoursExternal = slots.getAvailableHoursOnExternalTime();

            if (availableHoursExternal.stream().anyMatch(timePeriod -> timePeriod.getStart().equals(appointmentDto.getTimeSlotSelected().getStart())) &&
                    availableHoursInternal.stream().anyMatch(timePeriod -> timePeriod.getStart().equals(appointmentDto.getTimeSlotSelected().getStart()))) {
                logger.error("Appointment internal time is not valid. Could be null or start after the end time.");
                throw new RuntimeException("Appointment internal time is not valid. Could be null or start after the end time.");
            }
        }
        //internal time = external time start + mechanical action duration
        TimePeriod internalTime = new TimePeriod(appointmentDto.getTimeSlotSelected().getStart().getLocalTime(),
                appointmentDto.getTimeSlotSelected().getStart().getLocalTime().plus(mechanicalAction.getInternalDuration().toMinutes(), ChronoUnit.MINUTES));

        TimePeriod externalTime = appointmentDto.getTimeSlotSelected();

        // Create the appointment using dto data and builder class
        return AppointmentBuilder.anAppointment()
                .openDay(openDay)
                .comment(appointmentDto.getComment())
                .price(mechanicalAction.getPrice())
                .mechanicalAction(mechanicalAction)
                .vehicle(vehicle)
                .status(appointmentStatus)
                .internalTime(internalTime)
                .externalTime(externalTime)
                .idCalendarEvent(createCalendarEvent(openDay, appointmentDto, vehicle, mechanicalAction))
                .mechanicalActionCustom(false)
                .build();
    }


    public ResponseEntity<Appointment> handleCustomAppointment(CustomAppointmentEvaluation customAppointmentEvaluation) {

        Appointment appointment = appointmentRepository.findById(customAppointmentEvaluation.getAppointmentId()).orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (customAppointmentEvaluation.isApproved()) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointment.setInternalTime(customAppointmentEvaluation.getInternalTime());
            appointment.setExternalTime(customAppointmentEvaluation.getExternalTime());
            appointment.setPrice(customAppointmentEvaluation.getPrice());

            emailService.sendAppointmentApprovedMail(appointment.getId());
            return ResponseEntity.ok(appointmentRepository.save(appointment));
        } else {
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointmentRepository.save(appointment);
            emailService.sendAppointmentRejectedMail(appointment.getId());
            return ResponseEntity.ok(appointment);
        }
    }

    /**
     * Update the status of an appointment
     *
     * @param status        the new status
     * @param appointmentId the id of the appointment to update
     * @return the updated appointment
     */
    @Schema(example = "CONFIRMED", description = "The new status of the appointment")
    public ResponseEntity<Appointment> appointmentStateUpdate(String status, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));

        return ResponseEntity.ok(appointmentRepository.save(setStatus(status, appointment)));
    }

    @Override
    public ResponseEntity<Vote> voteAppointment(AppointmentVote appointmentVote) {
        Appointment appointment = appointmentRepository.findById(appointmentVote.getAppointmentId()).orElseThrow(() -> new RuntimeException("Appointment not found"));

        Vote vote = VoteBuilder.aVote()
                .appointment(appointment)
                .comment(appointmentVote.getComment())
                .rating(appointmentVote.getRating())
                .user(appointment.getVehicle().getUser())
                .build();

        Vote newVote = voteRepository.save(vote);

        return ResponseEntity.ok(newVote);
    }

    @Override
    public ResponseEntity<Vote> modifyVote(AppointmentVote appointmentVote, Long id) {
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new RuntimeException("Vote not found"));

        vote.setComment(appointmentVote.getComment());
        vote.setRating(appointmentVote.getRating());

        Vote newVote = voteRepository.save(vote);

        return ResponseEntity.ok(newVote);
    }

    @Override
    public ResponseEntity<Boolean> deleteVote(Long id) {
        voteRepository.deleteById(id);

        return ResponseEntity.ok(true);
    }


    private Appointment setStatus(String status, Appointment appointment) {
        switch (status) {
            case "CONFIRMED" -> {
                appointment.setStatus(AppointmentStatus.CONFIRMED);
                emailService.sendAppointmentApprovedMail(appointment.getId());
            }
            case "FINISHED" -> {
                appointment.setStatus(AppointmentStatus.FINISHED);
                emailService.sendFinishedAppointmentData(appointment.getId());
            }
            case "REJECTED" -> {
                appointment.setStatus(AppointmentStatus.REJECTED);
                emailService.sendAppointmentRejectedMail(appointment.getId());
            }
            default -> throw new RuntimeException("Status not found");
        }
        return appointment;
    }

    /**
     * Update the appointment
     * Is possible to update the vehicle, the comment , the status and the time slot
     * To modify the oder data is necessary to delete the appointment and create a new one
     *
     * @param appointmentDto the appointment to update
     * @return the updated appointment
     */
    @Override
    public ResponseEntity<Appointment> update(AppointmentDto appointmentDto, Long id) {
        try {
            logger.info("update() called with appointment: {} and id {}", appointmentDto, id);

            // Try to find the appointment by id
            Appointment appointmentToUpdate = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));

            // Retrieve the linked vehicle by id
            if (appointmentDto.getVehicleId() != null) {
                Vehicle vehicle = vehicleRepository.findById(appointmentDto.getVehicleId()).orElseThrow(() -> new RuntimeException("Vehicle not found"));
                appointmentToUpdate.setVehicle(vehicle);
            }

            if (appointmentDto.getComment() != null) {
                appointmentToUpdate.setComment(appointmentDto.getComment());
            }

            if (appointmentDto.getTimeSlotSelected() != null) {
                appointmentToUpdate.setInternalTime(appointmentDto.getTimeSlotSelected());
                // Retrieve the linked mechanical action by id
                MechanicalAction mechanicalAction = appointmentToUpdate.getMechanicalAction();

                // Check that internal time is not null and that the start time is before the end time
                if (appointmentDto.getTimeSlotSelected().getStart() != null || appointmentDto.getTimeSlotSelected().getEnd() != null) {

                    try {
                        List<TimePeriod> availableHours = appointmentCore.getAvailableAppointments(appointmentToUpdate.getOpenDay(), mechanicalAction).getAvailableHoursOnExternalTime();

                        if (availableHours.stream().noneMatch(timePeriod -> timePeriod.getStart().equals(appointmentDto.getTimeSlotSelected().getStart()))) {
                            logger.error("Appointment internal time is not valid. Could be null or start after the end time.");
                            throw new RuntimeException("Appointment internal time is not valid. Could be null or start after the end time.");
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
                //internal time = external time start + mechanical action duration
                TimePeriod internalTime = new TimePeriod(appointmentDto.getTimeSlotSelected().getStart().getLocalTime(),
                        appointmentDto.getTimeSlotSelected().getStart().getLocalTime().plus(mechanicalAction.getInternalDuration().toMinutes(), ChronoUnit.MINUTES));

                TimePeriod externalTime = appointmentDto.getTimeSlotSelected();

                appointmentToUpdate.setInternalTime(internalTime);
                appointmentToUpdate.setExternalTime(externalTime);

                try {
                    googleCalendarService.removeEvent(appointmentToUpdate.getIdCalendarEvent());
                } catch (Exception e) {
                    logger.warn("Appointment not found in google calendar");
                }
                appointmentToUpdate.setIdCalendarEvent(createCalendarEvent(appointmentToUpdate.getOpenDay(), appointmentDto, appointmentToUpdate.getVehicle(), mechanicalAction));
            }

            return ResponseEntity.ok(appointmentRepository.save(appointmentToUpdate));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Delete the appointment
     *
     * @param id the appointment id
     * @return the deleted appointment
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the appointment exists
            if (!appointmentRepository.existsById(id)) {
                logger.error("Appointment not found");
                return ResponseEntity.notFound().build();
            }
            try {
                googleCalendarService.removeEvent(appointmentRepository.findById(id).get().getIdCalendarEvent());
            }catch (Exception e){
                logger.warn("Appointment not found in google calendar");
            }
            appointmentRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());

        } finally {
            logger.debug("Exit from delete() method");
        }
    }

    @Override
    public ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(AppointmentSearchDto appointmentSearchDto) {
        OpenDay openDay = openDayRepository.findById(appointmentSearchDto.getOpendayId()).orElseThrow(() -> new RuntimeException("OpenDay not found"));
        MechanicalAction mechanicalAction = mechanicalActionRepository.findById(appointmentSearchDto.getMechanicalActionId()).orElseThrow(() -> new RuntimeException("MechanicalAction not found"));

        if (appointmentSearchDto.isExternalTimeslot())
            return ResponseEntity.ok(appointmentCore.getAvailableAppointments(openDay, mechanicalAction).getAvailableHoursOnExternalTime());

        return ResponseEntity.ok(appointmentCore.getAvailableAppointments(openDay, mechanicalAction).getAvailableHoursOnInteralTime());
    }

    @Override
    public ResponseEntity<List<Appointment>> findAllByUserPrincipal(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        List<Appointment> appointments = appointmentRepository.findAllByVehicleIn(user.getVehicle());
        return ResponseEntity.ok(appointments);
    }

    @Override
    public ResponseEntity<List<Appointment>> findAllByUserId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        List<Appointment> appointments = appointmentRepository.findAllByVehicleIn(user.getVehicle());
        return ResponseEntity.ok(appointments);
    }
    // endregion CRUD Methods
}
