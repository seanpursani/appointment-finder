package com.nology.calendar;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Calendar {
    private final LocalTime earliestAvailableTime;
    private final LocalTime latestAvailableTime;
    private final List<Appointment> appointmentList = new ArrayList<>();

    public Calendar(LocalTime earliestAvailableTime, LocalTime latestAvailableTime) {
        this.earliestAvailableTime = earliestAvailableTime;
        this.latestAvailableTime = latestAvailableTime;
    }

    public String createAppointment(AppointmentLength appointmentLength, LocalDateTime time) {
        LocalTime endTime = time.toLocalTime().plus(getIntFromEnum(appointmentLength), ChronoUnit.MINUTES);
        if ((endTime.isBefore(earliestAvailableTime) || endTime.equals(earliestAvailableTime)) || (endTime.isAfter(latestAvailableTime)))
            throw new IllegalArgumentException("Appointment @ " + time.toLocalTime() + " is outside of your working range");
        if (time.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(("Appointment cannot be made in the past"));
        Appointment myAppointment = new Appointment(time.toLocalDate(), time.toLocalTime(), endTime);
        appointmentList.add(myAppointment);
        return "Appointment on " + DateTimeFormatter.ofPattern("dd-MM-yyyy @ HH:mm").format(time) + " created.";
    }

    public void deleteAppointment(LocalDateTime time) {
        if (getAppointmentList().stream().noneMatch(appointment -> appointment.getDate().equals(time.toLocalDate()) && appointment.getStartTime().equals(time.toLocalTime())))
            throw new IllegalArgumentException("No such appointment in your calendar.");
        appointmentList.removeIf(appointment -> appointment.getDate().equals(time.toLocalDate()) && appointment.getStartTime().equals(time.toLocalTime()));
    }

    public long getIntFromEnum(AppointmentLength appointmentLength) {
        if (appointmentLength == AppointmentLength.SIXTY) {
            return 60;
        } else if (appointmentLength == AppointmentLength.THIRTY) {
            return 30;
        } else {
            return 15;
        }
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList.stream()
                .sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getStartTime))
                .collect(Collectors.toList());
    }

    public LocalTime getEarliestAvailableTime() {
        return earliestAvailableTime;
    }

    public LocalTime getLatestAvailableTime() {
        return latestAvailableTime;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "earliestAvailableTime=" + earliestAvailableTime +
                ", latestAvailableTime=" + latestAvailableTime +
                ", appointmentList=" + getAppointmentList() +
                '}';
    }
}
