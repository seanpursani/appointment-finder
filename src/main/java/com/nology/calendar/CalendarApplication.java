package com.nology.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
		Person Sean = new Person("Sean Pursani");
		Person Ruth = new Person("Ruth Campbell");
		Person Daniel = new Person("Daniel Han");
		Sean.addContact(Ruth);
		Sean.addContact(Daniel);
		Calendar seanCalendar = Sean.setDailyBound(LocalTime.of(8, 0), LocalTime.of(19, 30));
		seanCalendar.createAppointment("Breakfast with Client", AppointmentLength.SIXTY, LocalTime.of(9, 0));
		seanCalendar.createAppointment("Breakfast with Client", AppointmentLength.THIRTY, LocalTime.of(9, 30));
		seanCalendar.createAppointment("Dentist Appointment", AppointmentLength.SIXTY, LocalTime.of(13, 0));
		seanCalendar.createAppointment("Conducting Interview", AppointmentLength.SIXTY, LocalTime.of(17, 30));
		Calendar ruthCalendar = Ruth.setDailyBound(LocalTime.of(9, 0), LocalTime.of(19, 30));
		ruthCalendar.createAppointment("Monthly team meeting", AppointmentLength.SIXTY, LocalTime.of(13, 30));
		ruthCalendar.createAppointment("Client Negotiations", AppointmentLength.SIXTY, LocalTime.of(15, 0));
		ruthCalendar.createAppointment("Do Not Disturb", AppointmentLength.THIRTY, LocalTime.of(17, 0));
		Calendar danielCalendar = Daniel.setDailyBound(LocalTime.of(9, 0), LocalTime.of(19, 0));
		danielCalendar.createAppointment("Breakfast with Client", AppointmentLength.SIXTY, LocalTime.of(12, 0));
		danielCalendar.createAppointment("Lunch", AppointmentLength.FIFTEEN, LocalTime.of(18, 0));
		danielCalendar.createAppointment("Client Negotiations", AppointmentLength.SIXTY, LocalTime.of(15, 0));
		System.out.println(seanCalendar.showAppointmentList());
		System.out.println(ruthCalendar.showAppointmentList());
		System.out.println(danielCalendar.showAppointmentList());
		System.out.println(Sean.compareCalendar(List.of(Ruth, Daniel), AppointmentLength.THIRTY, 60));
	}

}
