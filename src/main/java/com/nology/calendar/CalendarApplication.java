package com.nology.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;

@SpringBootApplication
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
		Person Sean = new Person("Sean Pursani");
		Person Ruth = new Person("Ruth Campbell");
		Sean.addContact(Ruth);
		Ruth.addContact(Sean);
		Calendar seanCalendar = Sean.setDailyBound(LocalTime.of(9, 0), LocalTime.of(18, 0));
		seanCalendar.createAppointment("Meeting with Boss", AppointmentLength.THIRTY, LocalTime.of(7, 0));
		seanCalendar.createAppointment("Dentist Appointment", AppointmentLength.SIXTY, LocalTime.of(13, 0));
		seanCalendar.createAppointment("Conducting Interview", AppointmentLength.THIRTY, LocalTime.of(15, 0));
		seanCalendar.createAppointment("Performance Review", AppointmentLength.THIRTY, LocalTime.of(16, 0));
		Calendar ruthCalendar = Ruth.setDailyBound(LocalTime.of(8, 0), LocalTime.of(19, 0));
		ruthCalendar.createAppointment("Meeting with Colleague", AppointmentLength.SIXTY, LocalTime.of(15, 0));
		ruthCalendar.createAppointment("Marketing Debrief", AppointmentLength.FIFTEEN, LocalTime.of(12, 0));
		ruthCalendar.createAppointment("Performance Review", AppointmentLength.THIRTY, LocalTime.of(11, 0));
		ruthCalendar.createAppointment("Monthly team meeting", AppointmentLength.SIXTY, LocalTime.of(17, 0));
//		System.out.println(ruthCalendar.getAppointmentList());
		System.out.println(seanCalendar.getAppointmentList());
		System.out.println(Sean.compareCalendar(Ruth, AppointmentLength.SIXTY));
//		System.out.println(Ruth.compareCalendar(Sean, AppointmentLength.SIXTY));

		// 9:00-10:00
		// 9:15-10:15
		// 9:30-10:30
		// 9:45-10:45
		// 10:00-11:00
		// 10:15-11:15
		// 10:30-11:30
		// 10:45-11:45
		// 11:00-12:00
		// 14:00-15:00
		// 17:00-18:00

	}

}
