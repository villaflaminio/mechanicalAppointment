package it.mtempobono.mechanicalappointment.service.impl;

import com.google.api.client.util.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.GoogleCalendarCreateEvent;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.builders.AppointmentBuilder;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.CustomAppointmentEvaluation;
import it.mtempobono.mechanicalappointment.model.entity.*;
import it.mtempobono.mechanicalappointment.repository.AppointmentRepository;
import it.mtempobono.mechanicalappointment.repository.MechanicalActionRepository;
import it.mtempobono.mechanicalappointment.repository.OpenDayRepository;
import it.mtempobono.mechanicalappointment.repository.VehicleRepository;
import it.mtempobono.mechanicalappointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    //region Fields

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private OpenDayRepository openDayRepository;

    @Autowired
    private MechanicalActionRepository mechanicalActionRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;
    //endregion

    // region Public Methods

    public static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Finds the overlapping intervals in a list of TimePeriods.
     *
     * @param intervals the list of TimePeriods to analyze
     * @return a list of TimePeriods representing the overlapping intervals
     */
    public List<TimePeriod> findOverlappingIntervals(List<TimePeriod> intervals) {
        intervals.sort(TimePeriod::compareTo); // Sort the intervals by start date
        List<TimePeriod> overlappingIntervals = new ArrayList<>();

        for (int i = 0; i < intervals.size() - 1; i++) {
            for (int j = i + 1; j < intervals.size(); j++) {
                TimePeriod interval1 = intervals.get(i);
                TimePeriod interval2 = intervals.get(j);
                // Check if the intervals overlap
                if (interval1.getEnd().getLocalTime().isAfter(interval2.getStart().getLocalTime()) && interval2.getEnd().getLocalTime().isAfter(interval1.getStart().getLocalTime())) {
                    // Find the start and end dates of the overlapping interval
                    LocalTime overlapStart = interval1.getStart().getLocalTime().isBefore(interval2.getStart().getLocalTime()) ? interval2.getStart().getLocalTime() : interval1.getStart().getLocalTime();
                    LocalTime overlapEnd = interval1.getEnd().getLocalTime().isAfter(interval2.getEnd().getLocalTime()) ? interval2.getEnd().getLocalTime() : interval1.getEnd().getLocalTime();
                    TimePeriod overlapping = new TimePeriod(overlapStart, overlapEnd);
                    // Add the overlapping interval to the list only if it's not already present
                    if (!overlappingIntervals.contains(overlapping)) {
                        overlappingIntervals.add(overlapping);
                    }
                }
            }
        }
        return overlappingIntervals;
    }

//    public List<TimePeriod> findOverlappingIntervalFromTimePeriod(List<TimePeriod> intervals) {
//        Collections.sort(intervals); //(n log n)
//        List<TimePeriod> overlappingInterval = new ArrayList<>();
//        List<int[]> overlappingIntervals = new ArrayList<>();
//
//        for (int i = 0; i < intervals.size(); i++) { //n
//
//            for (int j = i + 1; j < intervals.size(); j++) {
//                if (isOverlapping(intervals.get(i).getStart(), intervals.get(i).getEnd(),
//                        intervals.get(j).getStart(), intervals.get(j).getEnd())) {
//
//                    LocalTime prevStartInterval = intervals.get(i).getStart();
//                    LocalTime currStartInterval = intervals.get(j).getStart();
//
//                    LocalTime prevEndInterval = intervals.get(i).getEnd();
//                    LocalTime currEndInterval = intervals.get(j).getEnd();
//
//                    int[] prevInterval = {prevStartInterval.toSecondOfDay(), prevEndInterval.toSecondOfDay()};
//                    int[] currInterval = {currStartInterval.toSecondOfDay(), currEndInterval.toSecondOfDay()};
//
//                    int[] overlapping = new int[]{Math.max(prevInterval[0], currInterval[0]), Math.min(prevInterval[1], currInterval[1])};
//
//                    if (overlappingIntervals.stream()
//                            .noneMatch(x -> Arrays.equals(x, overlapping)))
//                        overlappingIntervals.add(overlapping);
//
//                }
//            }
//        }
//        for (int[] interval : overlappingIntervals) {
//
//            overlappingInterval.add(new TimePeriod(LocalTime.ofSecondOfDay(interval[0]), LocalTime.ofSecondOfDay(interval[1])));
//        }
//        return overlappingInterval;
//    }


    public List<TimePeriod> getAvailableAppointments(OpenDay openDay, MechanicalAction work) {
//        OpenDay openDay = openDayRepository.findById(openDayId).orElseThrow(() -> new RuntimeException("OpenDay not found"));

        DayPlan day = openDay.getWorkPlan();

        List<TimePeriod> scheduledAppointments = new ArrayList<TimePeriod>(openDay.getAppointments().stream().map(Appointment::getInternalTime).toList());

        List<TimePeriod> availablePeroids = day.timePeriodsWithBreaksExcluded();

        Integer maxEventInParallel = openDay.getMaxParallelAppointments();

        if (maxEventInParallel == 0) {
            availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, scheduledAppointments);

        } else {
            List<TimePeriod> intervalFilled = findOverlappingIntervals(scheduledAppointments);

            for (int i = 1; i < maxEventInParallel; i++) {
                intervalFilled = findOverlappingIntervals(intervalFilled);
            }
            availablePeroids = excludeAppointmentsFromTimePeriods(availablePeroids, intervalFilled);
        }


        return calculateAvailableHours(availablePeroids, work);
    }

//    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeroids, MechanicalAction work) {
//        ArrayList<TimePeriod> availableHours = new ArrayList();
//        for (TimePeriod peroid : availableTimePeroids) {
//            TimePeriod workPeroid = new TimePeriod(peroid.getStart(), peroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
//            while (workPeroid.getEnd().isBefore(peroid.getEnd()) || workPeroid.getEnd().equals(peroid.getEnd())) {
//                availableHours.add(new TimePeriod(workPeroid.getStart(), workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes())));
//                workPeroid.setStart(workPeroid.getStart().plusMinutes(work.getInternalDuration().toMinutes()));
//                workPeroid.setEnd(workPeroid.getEnd().plusMinutes(work.getInternalDuration().toMinutes()));
//            }
//        }
//        return availableHours;
//    }

    /**
     * Calculates the available hours for a MechanicalAction within a list of TimePeriods.
     *
     * @param availableTimePeriods the list of TimePeriods representing the available time
     * @param work                 the MechanicalAction to schedule
     * @return a list of TimePeriods representing the available hours for the MechanicalAction
     */
    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeriods, MechanicalAction work) {
        List<TimePeriod> availableHours = new ArrayList<>(availableTimePeriods.size() * 2);
        // On each available time period
        for (TimePeriod period : availableTimePeriods) {
            LocalTime startTime = period.getStart().getLocalTime();
            LocalTime endTime = period.getEnd().getLocalTime();
            // Iterate over each interval of the work time that can fit within the period
            while (startTime.plusMinutes(work.getInternalDuration().toMinutes()).isBefore(endTime)) {
                // Add the interval to the list of available hours
                availableHours.add(new TimePeriod(startTime, startTime.plusMinutes(work.getInternalDuration().toMinutes())));
                startTime = startTime.plusMinutes(work.getInternalDuration().toMinutes());
            }
            // Add any remaining time as a separate interval
            if (startTime.isBefore(endTime)) {
                availableHours.add(new TimePeriod(startTime, endTime));
            }
        }
        return availableHours;
    }

    /**
     * This method excludes appointments from the given time periods, returning the resulting time periods.
     *
     * @param periods      the list of time periods to be modified
     * @param appointments the list of time periods representing appointments to be excluded
     * @return a list of time periods that represent the resulting availability after excluding the appointments
     */
    public List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> periods, List<TimePeriod> appointments) {

        List<TimePeriod> toAdd = new ArrayList<>();

        // Sort the appointments by start time, in ascending order
        Collections.sort(appointments);

        // Loop through each appointment
        for (TimePeriod appointment : appointments) {

            // Create a copy of the appointment period to avoid modifying the original object
            TimePeriod appointmentPeriod = new TimePeriod(appointment.getStart().getLocalTime(), appointment.getEnd().getLocalTime());

            // Loop through each period
            for (TimePeriod period : periods) {

                // Case 1: appointment overlaps the start of the period
                if ((appointment.getStart().getLocalTime().isBefore(period.getStart().getLocalTime()) || appointment.getStart().equals(period.getStart()))
                        && appointment.getEnd().getLocalTime().isAfter(period.getStart().getLocalTime()) &&
                        appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                    period.setStart(appointment.getEnd());
                }

                // Case 2: appointment overlaps the end of the period
                if (appointment.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) &&
                        appointment.getStart().getLocalTime().isBefore(period.getEnd().getLocalTime())
                        && (appointment.getEnd().getLocalTime().isAfter(period.getEnd().getLocalTime()) || appointment.getEnd().equals(period.getEnd()))) {
                    period.setEnd(appointment.getStart());
                }

                // Case 3: appointment is within the period
                if (appointment.getStart().getLocalTime().isAfter(period.getStart().getLocalTime()) && appointment.getEnd().getLocalTime().isBefore(period.getEnd().getLocalTime())) {
                    toAdd.add(new TimePeriod(period.getStart().getLocalTime(), appointment.getStart().getLocalTime()));
                    period.setStart(appointment.getEnd());
                }
            }
        }

        // Add the new time periods created in case 3 to the list of periods
        periods.addAll(toAdd);

        // Sort the periods by start time, in ascending order
        Collections.sort(periods);

        return periods;
    }


//    public List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> peroids, List<TimePeriod> appointments) {
//
//        List<TimePeriod> toAdd = new ArrayList();
//        Collections.sort(appointments);
//        for (TimePeriod appointment : appointments) {
//            TimePeriod appointMentPeroid = new TimePeriod(appointment.getStart(), appointment.getEnd());
//            for (TimePeriod peroid : peroids) {
//                if ((appointment.getStart().isBefore(peroid.getStart()) || appointment.getStart().equals(peroid.getStart()))
//                        && appointment.getEnd().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
//                    peroid.setStart(appointment.getEnd());
//                }
//                if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getStart().isBefore(peroid.getEnd())
//                        && appointment.getEnd().isAfter(peroid.getEnd()) || appointment.getEnd().equals(peroid.getEnd())) {
//                    peroid.setEnd(appointment.getStart());
//                }
//                if (appointment.getStart().isAfter(peroid.getStart()) && appointment.getEnd().isBefore(peroid.getEnd())) {
//                    toAdd.add(new TimePeriod(peroid.getStart(), appointment.getStart()));
//                    peroid.setStart(appointment.getEnd());
//                }
//            }
//        }
//        peroids.addAll(toAdd);
//        Collections.sort(peroids);
//        return peroids;
//    }


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
        } finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
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
        } finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    private String createCalendarEvent(OpenDay openDay, AppointmentDto appointmentDto, Vehicle vehicle, MechanicalAction mechanicalAction) throws Exception {
        GoogleCalendarCreateEvent googleCalendarCreateEvent = new GoogleCalendarCreateEvent();
        googleCalendarCreateEvent.setSummary("Appointment");
        googleCalendarCreateEvent.setDescription("Appointment for " + vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getPlate() + " for " + mechanicalAction.getName());

        LocalDate day = openDay.getDate();
        LocalTime startTime = appointmentDto.getTimeSlotSelected().getStart().getLocalTime();
        LocalTime endTime = appointmentDto.getTimeSlotSelected().getEnd().getLocalTime();

        DateTime startCalendarTime = new DateTime(new Date(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), startTime.getHour(), startTime.getMinute()));
        DateTime endCalendarTime = new DateTime(new Date(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), endTime.getHour(), endTime.getMinute()));

        googleCalendarCreateEvent.setStartTime(startCalendarTime);
        googleCalendarCreateEvent.setEndTime(endCalendarTime);
        googleCalendarCreateEvent.setLocation(openDay.getGarage().getAddress());

        return googleCalendarService.addEvent(googleCalendarCreateEvent);
    }

    /**
     * Create a new appointment
     *
     * @param appointmentDto the appointment to create
     * @return the created appointment
     */
    @Override
    public ResponseEntity<Appointment> save(AppointmentDto appointmentDto) {
        try {
            logger.info("save() called with appointment: {}", appointmentDto);

            // Retrieve the linked open day by id
            OpenDay openDay = openDayRepository.findById(appointmentDto.getOpenDayId()).orElseThrow(() -> new RuntimeException("Open day not found"));

            // Retrieve the linked vehicle by id
            Vehicle vehicle = vehicleRepository.findById(appointmentDto.getVehicleId()).orElse(null);

            // Check that vehicle exists
            if (vehicle == null) {
                logger.error("Vehicle not found");
                return ResponseEntity.badRequest().build();
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
            emailService.sendNewAppointmentMail(savedAppointment);

            return ResponseEntity.ok(savedAppointment);

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from save() method");
        }
        return ResponseEntity.badRequest().build();
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
                .build();

    }

    public Appointment createStockAppointment(AppointmentDto appointmentDto, OpenDay openDay, Vehicle vehicle, AppointmentStatus appointmentStatus) throws Exception {
        // Retrieve the linked mechanical action by id
        MechanicalAction mechanicalAction = mechanicalActionRepository.findById(appointmentDto.getMechanicalActionId()).orElseThrow(() -> new RuntimeException("Mechanical action not found"));

        // Check that internal time is not null and that the start time is before the end time
        if (appointmentDto.getTimeSlotSelected().getStart() != null || appointmentDto.getTimeSlotSelected().getEnd() != null) {

            List<TimePeriod> availableHours = getAvailableAppointments(openDay, mechanicalAction);

            if (availableHours.stream().noneMatch(timePeriod -> timePeriod.getStart().equals(appointmentDto.getTimeSlotSelected().getStart()))) {
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
                .build();
    }


    public ResponseEntity<Appointment> handleCustomAppointment(CustomAppointmentEvaluation customAppointmentEvaluation) {

        Appointment appointment = appointmentRepository.findById(customAppointmentEvaluation.getAppointmentId()).orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (customAppointmentEvaluation.isApproved()) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointment.setInternalTime(customAppointmentEvaluation.getInternalTime());
            appointment.setExternalTime(customAppointmentEvaluation.getExternalTime());
            appointment.setPrice(customAppointmentEvaluation.getPrice());

            emailService.sendCustomAppointmentApprovedMail(appointment);
            return ResponseEntity.ok(appointmentRepository.save(appointment));
        } else {
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointmentRepository.save(appointment);
            emailService.sendCustomAppointmentRejectedMail(appointment);
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


    private Appointment setStatus(String status, Appointment appointment) {
        if (status.equals("CONFIRMED")) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            emailService.sendStockAppointmentApprove(appointment);

        } else if (status.equals("FINISHED")) {
            appointment.setStatus(AppointmentStatus.FINISHED);
            emailService.sendFinishedAppointmentData(appointment);

        } else if (status.equals("REJECTED")) {
            appointment.setStatus(AppointmentStatus.REJECTED);
            emailService.sendStockAppointmentReject(appointment);

        } else {
            throw new RuntimeException("Status not found");
        }
        return appointment;
    }

    /**
     * Update the appointment
     * Is possible to update the vehicle, the comment , the status and the time slot
     * To modify the oder data is necessary to delete the appointment and create a new one
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


            if (appointmentDto.getStatus() != null) {
                appointmentToUpdate.setStatus(setStatus(appointmentDto.getStatus(), appointmentToUpdate).getStatus());
            }

            if (appointmentDto.getTimeSlotSelected() != null) {
                appointmentToUpdate.setInternalTime(appointmentDto.getTimeSlotSelected());
                // Retrieve the linked mechanical action by id
                MechanicalAction mechanicalAction = appointmentToUpdate.getMechanicalAction();

                // Check that internal time is not null and that the start time is before the end time
                if (appointmentDto.getTimeSlotSelected().getStart() != null || appointmentDto.getTimeSlotSelected().getEnd() != null) {

                    List<TimePeriod> availableHours = getAvailableAppointments(appointmentToUpdate.getOpenDay(), mechanicalAction);

                    if (availableHours.stream().noneMatch(timePeriod -> timePeriod.getStart().equals(appointmentDto.getTimeSlotSelected().getStart()))) {
                        logger.error("Appointment internal time is not valid. Could be null or start after the end time.");
                        throw new RuntimeException("Appointment internal time is not valid. Could be null or start after the end time.");
                    }
                }
                //internal time = external time start + mechanical action duration
                TimePeriod internalTime = new TimePeriod(appointmentDto.getTimeSlotSelected().getStart().getLocalTime(),
                        appointmentDto.getTimeSlotSelected().getStart().getLocalTime().plus(mechanicalAction.getInternalDuration().toMinutes(), ChronoUnit.MINUTES));

                TimePeriod externalTime = appointmentDto.getTimeSlotSelected();

                appointmentToUpdate.setInternalTime(internalTime);
                appointmentToUpdate.setExternalTime(externalTime);

                googleCalendarService.removeEvent(appointmentToUpdate.getIdCalendarEvent());
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

            appointmentRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from delete() method");
        }
        return ResponseEntity.badRequest().build();
    }
    // endregion CRUD Methods
}
