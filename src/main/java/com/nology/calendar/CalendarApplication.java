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
//		Person Sean = new Person("Sean Pursani");
//		Person Ruth = new Person("Ruth Campbell");
//		Person Daniel = new Person("Daniel Han");
//		Person Lana = new Person("Lana Ferrari");
//		Sean.addContact(Ruth);
//		Sean.addContact(Daniel);
//		Sean.addContact(Lana);
//		Calendar seanCalendar = Sean.setDailyBound(LocalTime.of(8, 0), LocalTime.of(19, 30));
//		seanCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(9, 0));
//		seanCalendar.createAppointment(AppointmentLength.THIRTY, LocalTime.of(9, 30));
//		seanCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(13, 0));
//		seanCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(17, 30));
//		Calendar ruthCalendar = Ruth.setDailyBound(LocalTime.of(9, 0), LocalTime.of(19, 30));
//		ruthCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(13, 30));
//		ruthCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(15, 0));
//		ruthCalendar.createAppointment(AppointmentLength.THIRTY, LocalTime.of(17, 0));
//		Calendar danielCalendar = Daniel.setDailyBound(LocalTime.of(9, 0), LocalTime.of(19, 0));
//		danielCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(12, 0));
//		danielCalendar.createAppointment(AppointmentLength.FIFTEEN, LocalTime.of(18, 0));
//		danielCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(15, 0));
//		Calendar lanaCalendar = Lana.setDailyBound(LocalTime.of(12, 0), LocalTime.of(19, 0));
//		lanaCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(12, 0));
//		lanaCalendar.createAppointment(AppointmentLength.FIFTEEN, LocalTime.of(14, 0));
//		lanaCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(15, 0));
//		System.out.println(seanCalendar.showAppointmentList());
//		System.out.println(ruthCalendar.showAppointmentList());
//		System.out.println(danielCalendar.showAppointmentList());
//		System.out.println(lanaCalendar.showAppointmentList());
//		System.out.println(Sean.compareCalendar(List.of(Ruth, Daniel, Lana), AppointmentLength.FIFTEEN, 15));

		Person janeDoe = new Person("Jane Doe");
		Calendar janeDoeCalendar = janeDoe.setDailyBound(LocalTime.of(9, 0), LocalTime.of(12,0));
		Person jeremySmith = new Person("Jeremy Smith");
		Calendar jeremySmithCalendar = jeremySmith.setDailyBound(LocalTime.of(9, 0), LocalTime.of(12, 0));
		jeremySmith.addContact(janeDoe);
		jeremySmithCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(10, 0));
		janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(11, 0));
		System.out.println(jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 60));
	}



}
