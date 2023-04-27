/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package it.mtempobono.mechanicalappointment.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.gson.Gson;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Yaniv Inbar
 */
public class GoogleCalendarAPI {
	/**
	 * Be sure to specify the name of your application. If the application name
	 * is {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "MechanicalAppointment";

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/calendar_sample");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	private static com.google.api.services.calendar.Calendar client;

	static final java.util.List<Calendar> addedCalendarsUsingBatch = Lists
			.newArrayList();

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {
		// load client secrets

		InputStreamReader in = new InputStreamReader(new FileInputStream("C:\\my_space\\mechanicalAppointment\\src\\main\\resources\\client_secret_apps.googleusercontent.com.json"));
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
				.setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	private static void showCalendars() throws IOException {
		View.header("Show Calendars");
		CalendarList feed = client.calendarList().list().execute();
		View.display(feed);
	}

	private static void addCalendarsUsingBatch() throws IOException {
		View.header("Add Calendars using Batch");
		BatchRequest batch = client.batch();

		// Create the callback.
		JsonBatchCallback<Calendar> callback = new JsonBatchCallback<Calendar>() {

			@Override
			public void onSuccess(Calendar calendar, HttpHeaders responseHeaders) {
				View.display(calendar);
				addedCalendarsUsingBatch.add(calendar);
			}

			@Override
			public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
				System.out.println("Error Message: " + e.getMessage());
			}
		};

		// Create 2 Calendar Entries to insert.
		Calendar entry1 = new Calendar().setSummary("Calendar for Testing 1");
		client.calendars().insert(entry1).queue(batch, callback);

		Calendar entry2 = new Calendar().setSummary("Calendar for Testing 2");
		client.calendars().insert(entry2).queue(batch, callback);

		batch.execute();
	}

	private static Calendar addCalendar() throws IOException {
		View.header("Add Calendar");
		Calendar entry = new Calendar();
		entry.setSummary("Mechanical Appointment");
		Calendar result = client.calendars().insert(entry).execute();
		View.display(result);
		return result;
	}

	private static Calendar updateCalendar(Calendar calendar) throws IOException {
		View.header("Update Calendar");
		Calendar entry = new Calendar();
		entry.setSummary("Updated Calendar for Testing");
		Calendar result = client.calendars().patch(calendar.getId(), entry).execute();
		View.display(result);
		return result;
	}

	private static void addEvent(Calendar calendar) throws IOException {
		View.header("Add Event");
		Event event = newEvent();
		Event result = client.events().insert(calendar.getId(), event).execute();
		View.display(result);

		client.events().delete(calendar.getId(), result.getId()).execute();
	}

	/**
	 * Set all-day-event
	 * @param event
	 * @return
	 */
	private static Event setAllDayEvent(Event event, String dateStr) {
	    Date startDate = new Date(); // Or a date from the database
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    if (dateStr.isEmpty()) {
		    try {
		    	startDate = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }
	    Date endDate = new Date(startDate.getTime() + 86400000); // An all-day event is 1 day (or 86400000 ms) long

	    String startDateStr = dateFormat.format(startDate);
	    String endDateStr = dateFormat.format(endDate);
	    // Out of the 6 methods for creating a DateTime object with no time element, only the String version works
	    DateTime startDateTime = new DateTime(startDateStr);
	    DateTime endDateTime = new DateTime(endDateStr);

	    // Must use the setDate() method for an all-day event (setDateTime() is used for timed events)
	    EventDateTime startEventDateTime = new EventDateTime().setDate(startDateTime);
	    EventDateTime endEventDateTime = new EventDateTime().setDate(endDateTime);

	    event.setStart(startEventDateTime);
	    event.setEnd(endEventDateTime);
	    
	    return event;
	}
	
	private static Event newEvent() {
		Event event = new Event();
		// Set event summary
		event.setSummary("Test flaminio");
		
		// Set all-day-event
//		String dateStr = "";
//		event = setAllDayEvent(event, dateStr);
		
//		 Set start and end time of event
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 3600000);
		DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		// Set event start time
		event.setStart(new EventDateTime().setDateTime(start));
		DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		// Set event end time
		event.setEnd(new EventDateTime().setDateTime(end));
		
		event.setDescription("Test description");
		event.setLocation("Italia");
		return event;
	}

	private static void showEvents(Calendar calendar) throws IOException {
		View.header("Show Events");
		Events feed = client.events().list(calendar.getId()).execute();
		View.display(feed);
	}

	private static void deleteCalendarsUsingBatch() throws IOException {
		View.header("Delete Calendars Using Batch");
		BatchRequest batch = client.batch();
		for (Calendar calendar : addedCalendarsUsingBatch) {
			client.calendars().delete(calendar.getId())
					.queue(batch, new JsonBatchCallback<Void>() {

						@Override
						public void onSuccess(Void content,
								HttpHeaders responseHeaders) {
							System.out.println("Delete is successful!");
						}

						@Override
						public void onFailure(GoogleJsonError e,
								HttpHeaders responseHeaders) {
							System.out.println("Error Message: "
									+ e.getMessage());
						}
					});
		}

		batch.execute();
	}

	private static void deleteCalendar(Calendar calendar) throws IOException {
		View.header("Delete Calendar");
		client.calendars().delete(calendar.getId()).execute();
	}

	public static ConfigInfo getConfig() {
		String JSON = readConfig();
		Gson gson = new Gson();
		ConfigInfo ci = gson.fromJson(JSON, ConfigInfo.class);
		
		return ci;
	}
	
	private static String readConfig() {
		StringBuilder sb = new StringBuilder();
//		String filePath = "config.txt";
//		BufferedReader reader = null;

		try {
//			reader = new BufferedReader(new InputStreamReader(
//					new FileInputStream(filePath), "UTF-8")); // read document format in "UTF-8"
//			String str = null;
//			while ((str = reader.readLine()) != null) {
				sb.append("{\"account\": \"mtempobono@gmail.com\"}");
//			}
		} catch (Exception e) {
			System.err.println("Please create your config.txt with your gmail address in JSON format:");
			System.err.println("{\"account\": \"mtempobono@gmail.com\"}");
//			e.printStackTrace();
		} finally {
//			try {
////				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		}
		return sb.toString();
	}

	class ConfigInfo {
		public String account = "";
	}
	
	public static void main(String[] args) {
		try {
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

			for(CalendarListEntry entry : feed.getItems()) {
				System.out.println(entry.getSummary());
				if (entry.getSummary().equals("Mechanical Appointment")) {
					calendar = client.calendars().get(entry.getId()).execute();
					break;
				}
			}
			//run commands
//			addCalendar();
//			showCalendars();
//			updateCalendar(calendar);
			addEvent(calendar);
			showEvents(calendar);

//			addCalendarsUsingBatch();
//			deleteCalendarsUsingBatch();
//			Calendar calendar = addCalendar();
//			deleteCalendar(calendar);

		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
	}
}