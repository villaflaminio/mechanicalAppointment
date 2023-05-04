package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.dto.MailResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.repository.AppointmentRepository;
import it.mtempobono.mechanicalappointment.util.EmailSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freeMarkerConfiguration;

    @Value("${mail.username}")
    private String username;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Methods that sends an email using Freemarker specified template.

    // -------------------[sendEmail]----------------------
    public MailResponse sendEmail(String to, String subject, Map<String, Object> model, String ftlFileName) {
        MailResponse response = new MailResponse();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // Set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // Get the correct template into resources/mail-templates
            Template t = freeMarkerConfiguration.getTemplate(ftlFileName + ".ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(to);

            // Set up the email
            helper.setText(html, true);
            helper.setSubject(subject);

            helper.setFrom(username);
            javaMailSender.send(message);

            response.setMessage(ftlFileName + " | Mail sent to : " + to);
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage(ftlFileName + "Mail Sending failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    public ResponseEntity<Void> sendNewAppointmentMail(Long appointmentId){
        try {
            logger.info("sendNewAppointmentMail() called with appointment: {}", appointmentId);

            // Retrieve the appointment linked to the id
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                logger.error("Appointment not found. Cannot be able to send sendNewAppointmentMail. ");
                return ResponseEntity.notFound().build();
            }

            String to = appointment.getVehicle().getUser().getEmail();

            String startTime = appointment.getExternalTime().getStart().getHour() + ":" + appointment.getExternalTime().getStart().getMinute();
            String endTime = appointment.getExternalTime().getEnd().getHour() + ":" + appointment.getExternalTime().getEnd().getMinute();

            String appointmentDate = appointment.getOpenDay().getDate().getDayOfMonth() + "/" + appointment.getOpenDay().getDate().getMonthValue() + "/" + appointment.getOpenDay().getDate().getYear();

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", appointment.getVehicle().getUser().getName());
            model.put("appointmentTime", startTime + " - " + endTime);
            model.put("appointmentDate", appointmentDate);
            model.put("comment", appointment.getComment());
            model.put("price", "€" + appointment.getPrice());
            model.put("mechanicalAction", appointment.getMechanicalAction().getName());
            model.put("contactEmail", username);

            MailResponse mailResponse = sendEmail(to, EmailSubjects.APPOINTMENT_CREATED,
                    model, "appointmentCreated");

            if (mailResponse.getStatus()) {
                logger.info("Email sent to: " + to);
                logger.info(mailResponse.getMessage());
                return ResponseEntity.ok().build();

            } else {
                logger.error("Email sending failed to: " + to);
                logger.error(mailResponse.getMessage());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            logger.error("Error in sendNewAppointmentMail() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from sendNewAppointmentMail() method");
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Void> sendAppointmentApprovedMail(Long appointmentId) {

        try {
            logger.info("sendAppointmentApprovedMail() called with appointment: {}", appointmentId);

            // Retrieve the appointment linked to the id
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                logger.error("Appointment not found. Cannot be able to send sendAppointmentApprovedMail. ");
                return ResponseEntity.notFound().build();
            }

            String to = appointment.getVehicle().getUser().getEmail();

            String startTime = appointment.getExternalTime().getStart().getHour() + ":" + appointment.getExternalTime().getStart().getMinute();
            String endTime = appointment.getExternalTime().getEnd().getHour() + ":" + appointment.getExternalTime().getEnd().getMinute();

            String appointmentType = appointment.getMechanicalActionCustom() ? "Custom" : "Stock";

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", appointment.getVehicle().getUser().getName());
            model.put("appointmentTime", startTime + " - " + endTime);
            model.put("comment", appointment.getComment());
            model.put("price", "€" + appointment.getPrice());
            model.put("mechanicalAction", appointment.getMechanicalAction().getName());
            model.put("appointmentType", appointmentType);
            model.put("contactEmail", username);

            MailResponse mailResponse = sendEmail(to, appointmentType + " " + EmailSubjects.APPOINTMENT_APPROVED,
                    model, "appointmentConfirmation");

            if (mailResponse.getStatus()) {
                logger.info("Email sent to: " + to);
                logger.info(mailResponse.getMessage());
                return ResponseEntity.ok().build();

            } else {
                logger.error("Email sending failed to: " + to);
                logger.error(mailResponse.getMessage());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            logger.error("Error in sendAppointmentApprovedMail() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from sendAppointmentApprovedMail() method");
        }
        return ResponseEntity.badRequest().build();

    }

    public ResponseEntity<Void> sendAppointmentRejectedMail(Long appointmentId) {
        try {
            logger.info("sendAppointmentRejectedMail() called with appointment: {}", appointmentId);

            // Retrieve the appointment linked to the id
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                logger.error("Appointment not found. Cannot be able to send sendAppointmentRejectedMail. ");
                return ResponseEntity.notFound().build();
            }

            String to = appointment.getVehicle().getUser().getEmail();

            String startTime = appointment.getExternalTime().getStart().getHour() + ":" + appointment.getExternalTime().getStart().getMinute();
            String endTime = appointment.getExternalTime().getEnd().getHour() + ":" + appointment.getExternalTime().getEnd().getMinute();

            String appointmentType = appointment.getMechanicalActionCustom() ? "Custom" : "Stock";

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", appointment.getVehicle().getUser().getName());
            model.put("appointmentTime", startTime + " - " + endTime);
            model.put("comment", appointment.getComment());
            model.put("price", "€" + appointment.getPrice());
            model.put("mechanicalAction", appointment.getMechanicalAction().getName());
            model.put("appointmentType", appointmentType);
            model.put("contactEmail", username);

            MailResponse mailResponse = sendEmail(to, appointmentType + " " + EmailSubjects.APPOINTMENT_REJECTION,
                    model, "appointmentRejection");

            if (mailResponse.getStatus()) {
                logger.info("Email sent to: " + to);
                logger.info(mailResponse.getMessage());
                return ResponseEntity.ok().build();

            } else {
                logger.error("Email sending failed to: " + to);
                logger.error(mailResponse.getMessage());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            logger.error("Error in sendAppointmentRejectedMail() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from sendAppointmentRejectedMail() method");
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Void> sendFinishedAppointmentData(Long appointmentId) {
        try {
            logger.info("sendFinishedAppointmentData() called with appointment: {}", appointmentId);

            // Retrieve the appointment linked to the id
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                logger.error("Appointment not found. Cannot be able to send sendFinishedAppointmentData. ");
                return ResponseEntity.notFound().build();
            }

            String to = appointment.getVehicle().getUser().getEmail();

            String startTime = appointment.getExternalTime().getStart().getHour() + ":" + appointment.getExternalTime().getStart().getMinute();
            String endTime = appointment.getExternalTime().getEnd().getHour() + ":" + appointment.getExternalTime().getEnd().getMinute();

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", appointment.getVehicle().getUser().getName());
            model.put("appointmentTime", startTime + " - " + endTime);
            model.put("comment", appointment.getComment());
            model.put("price", "€" + appointment.getPrice());
            model.put("mechanicalAction", appointment.getMechanicalAction().getName());
            model.put("contactEmail", username);

            MailResponse mailResponse = sendEmail(to, EmailSubjects.APPOINTMENT_FINISHED,
                    model, "appointmentFinished");

            if (mailResponse.getStatus()) {
                logger.info("Email sent to: " + to);
                logger.info(mailResponse.getMessage());
                return ResponseEntity.ok().build();

            } else {
                logger.error("Email sending failed to: " + to);
                logger.error(mailResponse.getMessage());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            logger.error("Error in sendFinishedAppointmentData() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from sendFinishedAppointmentData() method");
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Void> sendDeletedAppointmentData(Long appointmentId) {
        try {
            logger.info("sendDeletedAppointmentData() called with appointment: {}", appointmentId);

            // Retrieve the appointment linked to the id
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                logger.error("Appointment not found. Cannot be able to send sendDeletedAppointmentData. ");
                return ResponseEntity.notFound().build();
            }

            String to = appointment.getVehicle().getUser().getEmail();

            String startTime = appointment.getExternalTime().getStart().getHour() + ":" + appointment.getExternalTime().getStart().getMinute();
            String endTime = appointment.getExternalTime().getEnd().getHour() + ":" + appointment.getExternalTime().getEnd().getMinute();

            String appointmentDate = appointment.getOpenDay().getDate().getDayOfMonth() + "/" + appointment.getOpenDay().getDate().getMonthValue() + "/" + appointment.getOpenDay().getDate().getYear();

            Map<String, Object> model = new HashMap<>();
            model.put("customerName", appointment.getVehicle().getUser().getName());
            model.put("appointmentTime", startTime + " - " + endTime);
            model.put("appointmentDate", appointmentDate);
            model.put("comment", appointment.getComment());
            model.put("price", "€" + appointment.getPrice());
            model.put("mechanicalAction", appointment.getMechanicalAction().getName());
            model.put("contactEmail", username);

            MailResponse mailResponse = sendEmail(to, EmailSubjects.APPOINTMENT_DELETED,
                    model, "appointmentDeleted");

            if (mailResponse.getStatus()) {
                logger.info("Email sent to: " + to);
                logger.info(mailResponse.getMessage());
                return ResponseEntity.ok().build();

            } else {
                logger.error("Email sending failed to: " + to);
                logger.error(mailResponse.getMessage());
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            logger.error("Error in sendDeletedAppointmentData() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from sendDeletedAppointmentData() method");
        }
        return ResponseEntity.badRequest().build();
    }

}












