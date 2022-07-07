package com.nology.calendar;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Calendar {
    private final LocalTime earliestAvailableTime;
    private final LocalTime latestAvailableTime;
    private final List<Appointment> appointmentList = new ArrayList<>();

    public Calendar(LocalTime earliestAvailableTime, LocalTime latestAvailableTime) {
        this.earliestAvailableTime = earliestAvailableTime;
        this.latestAvailableTime = latestAvailableTime;
    }

    public String createAppointment (String meetingTitle, AppointmentLength appointmentLength, LocalTime startTime) {
        LocalTime endTime = startTime.plus(getIntFromEnum(appointmentLength), ChronoUnit.MINUTES);
        Appointment myAppointment = new Appointment(startTime, endTime);
        appointmentList.add(myAppointment);
        return myAppointment.toString();
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

    public String showAppointmentList() {
        List<Appointment> ascendingOrder = appointmentList.stream().sorted(Comparator.comparing(Appointment::getStartTime)).collect(Collectors.toList());
        StringBuilder appointmentList = new StringBuilder();
        for (Appointment appointment : ascendingOrder) {
            appointmentList.append(appointment).append("\n");
        }
        return appointmentList.toString();
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList.stream().sorted(Comparator.comparing(Appointment::getStartTime)).collect(Collectors.toList());
    }

    public LocalTime getEarliestAvailableTime() {
        return earliestAvailableTime;
    }

    public LocalTime getLatestAvailableTime() {
        return latestAvailableTime;
    }

    @Override
    public String toString() {
        return  "Available from " + earliestAvailableTime + " to " + latestAvailableTime + "\n" +
                showAppointmentList();
    }
}
