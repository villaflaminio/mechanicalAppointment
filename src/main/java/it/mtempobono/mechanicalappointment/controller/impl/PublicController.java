package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.GoogleCalendarCreateEvent;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.builder.PlaceBuilder;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentCore;
import it.mtempobono.mechanicalappointment.service.impl.GoogleCalendarService;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("api/public")
public class PublicController {

    private AppointmentCore appointmentCore = AppointmentCore.getInstance();
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    //popolate db
    @GetMapping("/populate")
    private void populate() throws IOException {

        List<Place> places = new ArrayList<>();
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/listacomuni.txt", StandardCharsets.ISO_8859_1))) {
            String line = br.readLine();
            while (line != null) {
                records.add(getRecordFromLine(line));
                line = br.readLine();
            }
        }

        for (List<String> record : records) {
            String Istat = record.get(0);
            String Comune = record.get(1).replace("'", "\\'");
            String Provincia = record.get(2);
            String Regione = record.get(3);

            //create and popolate place with builder
            Place place = PlaceBuilder.aPlace()
                    .istat(Integer.valueOf(Istat))
                    .municipality(Comune)
                    .province(Provincia)
                    .region(Regione)
                    .build();
            System.out.println("add place: " + place);
            places.add((place));
        }
        placeRepository.saveAll(places);

    }


    //test DayPlan
    @GetMapping("/test")
    private void test() {
        OpenDay openDay = new OpenDay();

        DayPlan dayPlan = new DayPlan();
        dayPlan.setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        dayPlan.setWorkingHours(new TimePeriod(LocalTime.of(6, 0), LocalTime.of(17, 0)));
        List<TimePeriod> breaks = new ArrayList<>();
        breaks.add(new TimePeriod(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        dayPlan.setBreaks(breaks);

        System.out.println(dayPlan);
        System.out.println(dayPlan.getWorkingHours());
        List<TimePeriod> timePeroidsWithBreaksExcluded = dayPlan.timePeriodsWithBreaksExcluded();
        openDay.setWorkPlan(dayPlan);


        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(14, 0), LocalTime.of(16, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(15, 30), LocalTime.of(17, 0)));

        Appointment appointment4 = new Appointment();
        appointment4.setInternalTime(new TimePeriod(LocalTime.of(14, 30), LocalTime.of(15, 30)));

//        Appointment appointment5 = new Appointment();
//        appointment5.setInternalTime(new TimePeriod(LocalTime.of(14, 30), LocalTime.of(16, 00)));
//        appointment5.setStartTime(LocalTime.of(14, 30));

        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);
//        appointments.add(appointment5);
        openDay.setAppointments(appointments);

        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(1));


        List<TimePeriod> availableHours = this.appointmentCore.getAvailableAppointments(openDay, work).getAvailableHoursOnInteralTime();

        System.out.println(availableHours);

    }


    //test google calendar
    @GetMapping("/testAddToCalendar")
    private void testGoogle() throws Exception {
        GoogleCalendarCreateEvent googleCalendarCreateEvent = new GoogleCalendarCreateEvent();
        googleCalendarCreateEvent.setSummary("test");
        googleCalendarCreateEvent.setLocation("test");
        googleCalendarCreateEvent.setDescription("test");
        googleCalendarCreateEvent.setStartTime(new com.google.api.client.util.DateTime("2023-04-27T10:00:00+02:00"));
        googleCalendarCreateEvent.setEndTime(new com.google.api.client.util.DateTime("2023-04-27T13:00:00+02:00"));

        System.out.println(googleCalendarService.addEvent(googleCalendarCreateEvent));
    }


    @GetMapping("/testRemoveFromCalendar")
    private void testRemoveFromCalendar() throws Exception {
        googleCalendarService.removeEvent("ige73uam2pmac6447ccklft6p4");
    }


}
