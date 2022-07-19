package com.nology.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
		Person Sean = new Person("Sean Pursani");
		Person Ruth = new Person("Ruth Campbell");
		Sean.addContact(Ruth);
		Calendar seanCalendar = Sean.createCalendar(LocalTime.of(8, 0), LocalTime.of(16, 00));
		seanCalendar.createAppointment(AppointmentLength.THIRTY, LocalDateTime.of(2023, Month.AUGUST, 10, 9, 30));
		seanCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 10, 10, 30));
		seanCalendar.createAppointment(AppointmentLength.THIRTY, LocalDateTime.of(2023, Month.AUGUST, 10, 12, 0));
		seanCalendar.createAppointment(AppointmentLength.THIRTY, LocalDateTime.of(2023, Month.AUGUST, 1, 12, 0));
		seanCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 12, 11, 0));
		System.out.println(seanCalendar.getAppointmentList());
		Calendar ruthCalendar = Ruth.createCalendar(LocalTime.of(9, 0), LocalTime.of(18, 00));
		ruthCalendar.createAppointment(AppointmentLength.FIFTEEN, LocalDateTime.of(2023, Month.AUGUST, 10, 10, 0));
		ruthCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 10, 11, 0));
		ruthCalendar.createAppointment(AppointmentLength.THIRTY, LocalDateTime.of(2023, Month.AUGUST, 10, 13, 0));
		ruthCalendar.createAppointment(AppointmentLength.FIFTEEN, LocalDateTime.of(2023, Month.AUGUST, 11, 15, 0));
		ruthCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 12, 11, 0));
		ruthCalendar.deleteAppointment(LocalDateTime.of(2023, Month.AUGUST, 12, 11, 0));
		System.out.println(ruthCalendar.getAppointmentList());
		System.out.println(Sean.compareCalendar(List.of(Ruth), AppointmentLength.SIXTY, 60, LocalDate.of(2023, Month.AUGUST, 10)));
	}
}
