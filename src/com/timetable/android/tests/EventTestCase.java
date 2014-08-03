package com.timetable.android.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.timetable.android.Event;
import com.timetable.android.EventPeriod;
import com.timetable.android.alarm.EventAlarm;

public class EventTestCase extends TestCase {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	public void setUp() {
		
	}
	
	public void testIsException() throws ParseException {
		Event event = new Event();
		Date exception = dateFormat.parse("31.07.2014");
		event.exceptions.add(exception);
		
		assertTrue(event.isException(exception));
		
	}
	public void testIsTodayPeriodNone() throws ParseException {
		Event event = new Event();
		event.period = new EventPeriod();
		Date searchDate = dateFormat.parse("27.12.2013");
		event.date = dateFormat.parse("27.12.2013");
		
		assertTrue(event.isToday(searchDate));
	
		event.date = dateFormat.parse("24.12.2013");
		
		assertFalse(event.isToday(searchDate));
	}
	
	public void testIsTodayPeriodDaily() throws ParseException {
		Event event = new Event();
		Date searchDate = dateFormat.parse("27.12.2013");
		event.date = dateFormat.parse("24.12.2013");
		event.period.type = EventPeriod.Type.DAILY;
		event.period.interval = 1;
		
		assertTrue(event.isToday(searchDate));
		
		event.exceptions.add(searchDate);
		
		assertFalse(event.isToday(searchDate));
		
		event.period.interval = 2;
		
		assertFalse(event.isToday(searchDate));
		
		event.exceptions.clear();
		event.period.interval = 3;
		
		assertTrue(event.isToday(searchDate));
		
		event.period.endDate = dateFormat.parse("26.12.2013");
		event.startTime = timeFormat.parse("23:59:59");
		
		assertFalse(event.isToday(searchDate));
		
		event.period.endDate = dateFormat.parse("27.12.2013");
		
		assertFalse(event.isToday(searchDate));
		
		event.period.endDate = dateFormat.parse("28.12.2013");
		
		assertTrue(event.isToday(searchDate));
		
		// testing leap years
		
		event.period.endDate = null;
		event.date = dateFormat.parse("27.02.2012");
		searchDate = dateFormat.parse("01.03.2012");
		
		assertTrue(event.isToday(searchDate));
		
		event.date = dateFormat.parse("27.02.1900"); //not a leap year
		searchDate = dateFormat.parse("01.03.1900"); 
		
		assertFalse(event.isToday(searchDate));
}
	
	public void testIsTodayPeriodWeekly() throws ParseException {
		Date searchDate = dateFormat.parse("04.01.2014"); //Saturday
		Event event = new Event();
		event.period.type = EventPeriod.Type.WEEKLY;
		event.period.interval = 1;
		event.date = dateFormat.parse("29.11.2013"); //Saturday
		
		assertFalse(event.isToday(searchDate));
		
		event.period.addWeekOccurrence(EventPeriod.SUNDAY); // every Sunday
		
		assertFalse(event.isToday(searchDate));
		
		event.period.addWeekOccurrence(EventPeriod.SATURDAY); // every Saturday
		
		assertTrue(event.isToday(searchDate));
		
		event.period.interval = 2;
		
		assertFalse(event.isToday(searchDate));
		
		searchDate = dateFormat.parse("11.01.2014");
		
		assertTrue(event.isToday(searchDate));
		
		event.period.endDate = dateFormat.parse("10.01.2014");
		
		assertFalse(event.isToday(searchDate));
		
		event.period.endDate = null;
		event.period.interval = 2;
		event.date = dateFormat.parse("30.06.2014");
		event.period.addWeekOccurrence(EventPeriod.MONDAY);
		searchDate = dateFormat.parse("07.07.2014");
		
		assertFalse(event.isToday(searchDate));
		
		searchDate = dateFormat.parse("14.07.2014");
		
		assertTrue(event.isToday(searchDate));
		
	}
	
	public void testIsTodayPeriodMonthly() throws ParseException {
		Date searchDate = dateFormat.parse("29.01.2014");
		Event event = new Event();
		event.date = dateFormat.parse("29.12.2013");
		event.period.type = EventPeriod.Type.MONTHLY;
		event.period.interval = 1;
		
		assertTrue(event.isToday(searchDate));
		
		event.period.interval = 2;
		
		assertFalse(event.isToday(searchDate));
		
		searchDate = dateFormat.parse("29.12.2015");
		
		assertTrue(event.isToday(searchDate));
		
		event.period.interval = 6;
		
		assertTrue(event.isToday(searchDate));
	
		event.period.endDate = dateFormat.parse("29.12.2014");
		
		assertFalse(event.isToday(searchDate));
		
		event.period.endDate = dateFormat.parse("30.12.2015");

		assertTrue(event.isToday(searchDate));
		
		event.date = dateFormat.parse("30.12.2013");

		assertFalse(event.isToday(searchDate));
	}
	
	public void testIsTodayPeriodYearly() throws ParseException {
		Event event = new Event();
		event.date = dateFormat.parse("29.12.2013");
		event.period.type = EventPeriod.Type.YEARLY;
		event.period.interval = 1;
		Date searchDate = dateFormat.parse("29.12.2014");
		
		assertTrue(event.isToday(searchDate));
		
		event.period.interval = 3;
		
		assertFalse(event.isToday(searchDate));
		
		searchDate = dateFormat.parse("29.12.2019");
		
		assertTrue(event.isToday(searchDate));
		
		event.date = dateFormat.parse("30.12.2013");
		
		assertFalse(event.isToday(searchDate));
		
		event.date = dateFormat.parse("29.11.2013");
		
		assertFalse(event.isToday(searchDate));
		
	}
	
	
	public void testIsOk() throws ParseException {
		Event event = new Event();
		
		assertFalse(event.isOk());
		
		event.date = dateFormat.parse("29.12.2013");
		
		assertFalse(event.isOk());
		
		event.startTime = timeFormat.parse("17:00:00");
		
		assertFalse(event.isOk());
		
		event.name = "Name";
		
		assertTrue(event.isOk());
		
		event.name = null;
		
		assertFalse(event.isOk());
		
		event.name = "Name";
		event.place = null;
		
		assertFalse(event.isOk());
		
		event.place = "Place";
		event.note = null;
		
		assertFalse(event.isOk());
		
		event.note = "Note";
		event.period.type =  EventPeriod.Type.MONTHLY;
		
		assertFalse(event.isOk());
		
		event.period.interval = 2;
		
		assertTrue(event.isOk());
		
		event.period.type = EventPeriod.Type.WEEKLY;
		
		assertTrue(event.isOk());
		
		event.period.setWeekOccurrences(0);
		
		assertTrue(event.isOk());
		
		event.name = "";
		for (int i = 0; i < Event.MAX_NAME_LENGTH; i++) {
			event.name = event.name.concat("a");
		}
		
		assertTrue(event.isOk());
		
		event.name = event.name.concat("a");
		
		assertFalse(event.isOk());
		
		event.name = "Name";
		event.place = "";
		for (int i = 0; i < Event.MAX_PLACE_LENGTH; i++) {
			event.place = event.place.concat("a");
		}
		
		assertTrue(event.isOk());
		
		event.place = event.place.concat("a");
		
		assertFalse(event.isOk());
		
		event.place = "Place";
		event.note = "";
		for (int i = 0; i < Event.MAX_NOTE_LENGTH; i++) {
			event.note = event.note.concat("a");
		}
		
		assertTrue(event.isOk());
		
		event.note = event.note.concat("a");
		
		assertFalse(event.isOk());
	}
	
	public void testEquals() throws ParseException {
		Event event1 = new Event();
		event1.name = "Name";
		event1.date = dateFormat.parse("30.12.2013");
		event1.startTime = timeFormat.parse("11:00:00");
		
		Event event2 = new Event();
		event2.name = event1.name;
		event2.date = event1.date;
		event2.startTime = event1.startTime;
		
		assertTrue(event1.equals(event2));
		
		event2.name = "Name2";
		
		assertFalse(event1.equals(event2));
		
		event2.name = event1.name;
		event2.date = dateFormat.parse("31.12.2013");
		
		assertFalse(event1.equals(event2));
		
		event2.date = event1.date;
		event2.startTime = timeFormat.parse("11:00:01");
		
		assertFalse(event1.equals(event2));
		
		event2.startTime = event1.startTime;
		event1.alarm = new EventAlarm();
		
		assertFalse(event1.equals(event2));
		
		event2.alarm = event1.alarm;
		
		assertTrue(event1.equals(event2));
	}
	
	public void tearDown() {
		
	}
}
