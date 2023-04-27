package it.mtempobono.mechanicalappointment.service;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

/**
 * @author Yaniv Inbar
 */
public class View {

	static void header(String name) {
		System.out.println();
		System.out.println("============== " + name + " ==============");
		System.out.println();
	}

	static void display(CalendarList feed) {
		if (feed.getItems() != null) {
			for (CalendarListEntry entry : feed.getItems()) {
				System.out.println();
				System.out
						.println("-----------------------------------------------");
				display(entry);
			}
		}
	}

	static void display(Events feed) {
		if (feed.getItems() != null) {
			for (Event entry : feed.getItems()) {
				System.out.println();
				System.out
						.println("-----------------------------------------------");
				display(entry);
			}
		}
	}

	static void display(CalendarListEntry entry) {
		System.out.println("ID: " + entry.getId());
		System.out.println("Summary: " + entry.getSummary());
		if (entry.getDescription() != null) {
			System.out.println("Description: " + entry.getDescription());
		}
	}

	static void display(Calendar entry) {
		System.out.println("ID: " + entry.getId());
		System.out.println("Summary: " + entry.getSummary());
		if (entry.getDescription() != null) {
			System.out.println("Description: " + entry.getDescription());
		}
	}

	static void display(Event event) {
		if (event.getSummary().equals("test calender API")) {
			if (event.getSummary() != null) {
				System.out.println("Summary: " + event.getSummary());
			}
			if (event.getDescription() != null) {
				System.out.println("Description: " + event.getDescription());
			}
			if (event.getStart() != null) {
				System.out.println("Start Time: " + event.getStart());
			}
			if (event.getEnd() != null) {
				System.out.println("End Time: " + event.getEnd());
			}
			if (event.getLocation() != null) {
				System.out.println("Location: " + event.getLocation());
			}
			if (event.getColorId() != null) {
				System.out.println("ColorId: " + event.getColorId());
			}
			if (event.getStatus() != null) {
				System.out.println("Status: " + event.getStatus());
			}
			if (event.getReminders() != null) {
				System.out.println("Reminders: " + event.getReminders());
			}
		}
	}
}