package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.MailResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freeMarkerConfiguration;


    @Value("${mail.username}")
    private String username;


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

            //helper.setCc(cc); per mandare in cc

            helper.setFrom(username);
            javaMailSender.send(message);

            response.setMessage(ftlFileName + " | Mail sent to : " + to);
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage(ftlFileName + "Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    //-------------------[send new Appointment mail]----------------------
    public MailResponse sendNewAppointmentMail(Appointment appointment){
        String to = appointment.getVehicle().getUser().getEmail();
        String subject = "Nuovo appuntamento";
        //todo creare modello email
        Map<String, Object> model = Map.of(
                "appointment", appointment
        );
        return sendEmail(to, subject, model, "newAppointment");
    }

    public void sendCustomAppointmentApprovedMail(Appointment appointment) {
    }

    public void sendCustomAppointmentRejectedMail(Appointment appointment) {
    }

    public void sendFinishedAppointmentData(Appointment appointment) {
    }

    public void sendStockAppointmentApprove(Appointment appointment) {
    }

    public void sendStockAppointmentReject(Appointment appointment) {
    }
}












