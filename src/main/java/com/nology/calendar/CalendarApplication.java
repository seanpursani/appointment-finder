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
		Calendar seanCalendar = Sean.setDailyBound(LocalTime.of(7, 0), LocalTime.of(20, 0));
		seanCalendar.createAppointment("Breakfast with Client", AppointmentLength.SIXTY, LocalTime.of(7, 0));
		seanCalendar.createAppointment("Dentist Appointment", AppointmentLength.THIRTY, LocalTime.of(13, 0));
		seanCalendar.createAppointment("Conducting Interview", AppointmentLength.SIXTY, LocalTime.of(14, 0));
		seanCalendar.createAppointment("Performance Review", AppointmentLength.FIFTEEN, LocalTime.of(16, 0));
		seanCalendar.createAppointment("Performance Review", AppointmentLength.THIRTY, LocalTime.of(17, 0));
		Calendar ruthCalendar = Ruth.setDailyBound(LocalTime.of(8, 0), LocalTime.of(19, 0));
		ruthCalendar.createAppointment("Meeting with Colleague", AppointmentLength.THIRTY, LocalTime.of(9,0));
		ruthCalendar.createAppointment("Marketing Debrief", AppointmentLength.SIXTY, LocalTime.of(12, 0));
		ruthCalendar.createAppointment("Performance Review", AppointmentLength.FIFTEEN, LocalTime.of(13, 0));
		ruthCalendar.createAppointment("Monthly team meeting", AppointmentLength.SIXTY, LocalTime.of(15, 0));
		ruthCalendar.createAppointment("Marketing Debrief", AppointmentLength.THIRTY, LocalTime.of(18, 0));
		System.out.println(seanCalendar.showAppointmentList());
		System.out.println(ruthCalendar.showAppointmentList());
		System.out.println(Sean.compareCalendar(Ruth, AppointmentLength.FIFTEEN, 60));
	}

}
