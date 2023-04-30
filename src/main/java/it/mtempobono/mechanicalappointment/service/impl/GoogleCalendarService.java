package it.mtempobono.mechanicalappointment.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.gson.Gson;
import it.mtempobono.mechanicalappointment.model.GoogleCalendarCreateEvent;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;


@Service
public class GoogleCalendarService {
    /**
     * Be sure to specify the name of your application. If the application name
     * is {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "MechanicalAppointment";

    /**
     * Directory to store user credentials.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".store/calendar_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory
            .getDefaultInstance();

    private static com.google.api.services.calendar.Calendar client;

    static final java.util.List<com.google.api.services.calendar.model.Calendar> addedCalendarsUsingBatch = Lists
            .newArrayList();

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {
        // load client secrets

        InputStreamReader in = new InputStreamReader(new FileInputStream("src/main/resources/client_secret_apps.googleusercontent.com.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, in);
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret()
                .startsWith("Enter ")) {
            System.out
                    .println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
                            + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR))
                .setDataStoreFactory(dataStoreFactory)
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(5093).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow,
                receiver).authorize("user");
    }

    /// <summary>
    /// Get the config info from the config.txt file
    /// </summary>
    private static String readConfig() {
        StringBuilder sb = new StringBuilder();

        try {

            sb.append("{\"account\": \"mtempobono@gmail.com\"}");
        } catch (Exception e) {
            System.err.println("Please create your config.txt with your gmail address in JSON format:");
            System.err.println("{\"account\": \"mtempobono@gmail.com\"}");
        }
        return sb.toString();
    }



    private static Event createEvent(GoogleCalendarCreateEvent createEvent) {
        Event event = new Event();
        // Set event summary
        event.setSummary(createEvent.getSummary());

        // Set event start time
        event.setStart(new EventDateTime().setDateTime(createEvent.getStartTime()));
        // Set event end time
        event.setEnd(new EventDateTime().setDateTime(createEvent.getEndTime()));

        event.setDescription(createEvent.getDescription());
        event.setLocation(createEvent.getLocation());
        return event;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     */
    private Calendar getCalendar() throws Exception{
        // initialize the transport
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // initialize the data store factory
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        // authorization
        Credential credential = authorize();

        // set up global Calendar instance
        client = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

        ConfigInfo configInfo = getConfig();
        com.google.api.services.calendar.model.Calendar calendar = client
                .calendars().get(configInfo.account).execute();
        CalendarList feed = client.calendarList().list().execute();

        for (CalendarListEntry entry : feed.getItems()) {
            if (entry.getSummary().equals("Mechanical Appointment")) {
             return   calendar = client.calendars().get(entry.getId()).execute();
            }
        }
        throw new Exception("Calendar not found");
    }

    public String addEvent(GoogleCalendarCreateEvent createEvent) throws Exception {

        Calendar calendar = getCalendar();

        Event event = createEvent(createEvent);
        Event result = client.events().insert(calendar.getId(), event).execute();
       return result.getId();

    }

    class ConfigInfo {
        public String account = "";
    }
    public static ConfigInfo getConfig() {
        String JSON = readConfig();
        Gson gson = new Gson();
        ConfigInfo ci = gson.fromJson(JSON, ConfigInfo.class);

        return ci;
    }

    public void removeEvent(String eventId) throws Exception {

        // initialize the transport
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // initialize the data store factory
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        // authorization
        Credential credential = authorize();

        // set up global Calendar instance
        client = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

        ConfigInfo configInfo = getConfig();
        com.google.api.services.calendar.model.Calendar calendar = client
                .calendars().get(configInfo.account).execute();
        CalendarList feed = client.calendarList().list().execute();

        for (CalendarListEntry entry : feed.getItems()) {
            System.out.println(entry.getSummary());
            if (entry.getSummary().equals("Mechanical Appointment")) {
                 calendar = client.calendars().get(entry.getId()).execute();
            }
        }

        client.events().delete(calendar.getId(), eventId).execute();

    }


}
