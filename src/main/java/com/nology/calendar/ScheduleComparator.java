package com.nology.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleComparator {
    private final Calendar calendar;
    private final List<Person> coworkers;
    private final AppointmentLength length;
    private final int interval;
    private final List<Appointment> userAvailableSlots = new ArrayList<>();

    public ScheduleComparator(Calendar calendar, List<Person> coworkers, AppointmentLength length, int interval) {
        this.calendar = calendar;
        this.coworkers = coworkers;
        this.length = length;
        this.interval = interval;
    }

    public List<Appointment> compareSchedule(LocalDate date) {
        List<Appointment> allAvailableSlots = new ArrayList<>();
        checkAvailableSlots(getCalendar(), getLatestEarlyBoundary(), getEarliestLateBoundary(), getCalendar().getIntFromEnum(getLength()), getInterval(), date);
        for (Person coworker: getCoworkers()) {
            allAvailableSlots.addAll(checkAvailableSlots(coworker.getCalendar(), getLatestEarlyBoundary(), getEarliestLateBoundary(), getCalendar().getIntFromEnum(getLength()), getInterval(), date));
        }
        List<Appointment> sharedTimeSlots = allAvailableSlots.stream()
                .filter(appointment -> Collections.frequency(allAvailableSlots, appointment) == getCoworkers().size()+1)
                .distinct()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .collect(Collectors.toList());
        if (sharedTimeSlots.size() < 1) return List.of();
        return sharedTimeSlots;
    }

    private List<Appointment> checkAvailableSlots(Calendar calendar, LocalTime earliestBoundary, LocalTime latestBoundary, long length, int interval, LocalDate date) {
        if (interval < length) throw new IllegalArgumentException("Interval length cannot be shorter than meeting length");
        LocalTime tempEarliestBoundary = earliestBoundary;
        LocalTime tempLatestBoundary = latestBoundary;
        List<Appointment> userAppointments = calendar.getAppointmentList().stream()
                .filter(appointment -> appointment.getDate().equals(date) && isAppointmentInRange(appointment, earliestBoundary, latestBoundary))
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .collect(Collectors.toList());
        if ((userAppointments.size()) < 1) return List.of();
        boolean isEarliestBoundaryBeforeUsers = userAppointments.get(0).getStartTime().isAfter(earliestBoundary);
        int count = (isEarliestBoundaryBeforeUsers) ? 1 : 0;

        for (int i = 0; i < userAppointments.size() + count; i++) {
            if (i == 0 && userAppointments.size() == 1 && !isEarliestBoundaryBeforeUsers) {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
            } else if (i == 0 && isEarliestBoundaryBeforeUsers && userAppointments.get(i).getStartTime().isAfter(tempEarliestBoundary)) {
                tempLatestBoundary = userAppointments.get(i).getStartTime();
            } else if (!isEarliestBoundaryBeforeUsers && i == userAppointments.size()-1) {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
                tempLatestBoundary = latestBoundary;
            } else if (isEarliestBoundaryBeforeUsers && i == userAppointments.size()) {
                tempEarliestBoundary = userAppointments.get(i - count).getEndTime();
                tempLatestBoundary = latestBoundary;
            } else if (isEarliestBoundaryBeforeUsers) {
                tempEarliestBoundary = userAppointments.get(i-count).getEndTime();
                tempLatestBoundary = userAppointments.get(i).getStartTime();
            } else {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
            }
            while (tempEarliestBoundary.plusMinutes(length).isBefore(tempLatestBoundary) || tempEarliestBoundary.plusMinutes(length).equals(tempLatestBoundary)) {
                Appointment newAppointment = new Appointment(date, tempEarliestBoundary, tempEarliestBoundary.plusMinutes(length));
                getUserAvailableSlots().add(newAppointment);
                tempEarliestBoundary = tempEarliestBoundary.plusMinutes(interval);
            }
        }
        return getUserAvailableSlots();
    }

    private boolean isAppointmentInRange(Appointment appointment, LocalTime earliestBoundary, LocalTime latestBoundary) {
        boolean isValid = !appointment.getEndTime().isBefore(earliestBoundary);
        if (appointment.getStartTime().isBefore(earliestBoundary) && !appointment.getEndTime().isAfter(earliestBoundary)) isValid = false;
        if (appointment.getStartTime().isAfter(latestBoundary) || appointment.getStartTime().equals(latestBoundary)) isValid = false;
        return isValid;
    }

    private LocalTime getEarliestLateBoundary() {
        List<Person> sortedLatestAvailableTimes = getCoworkers().stream()
                .sorted(Comparator.comparing(coworker -> coworker.getCalendar().getLatestAvailableTime()))
                .collect(Collectors.toList());
        Person coworkerWithEarliestLateBoundary = sortedLatestAvailableTimes.get(0);
        return (getCalendar().getLatestAvailableTime().isBefore(coworkerWithEarliestLateBoundary.getCalendar().getLatestAvailableTime())
                ? getCalendar().getLatestAvailableTime()
                : coworkerWithEarliestLateBoundary.getCalendar().getLatestAvailableTime());
    }

    private LocalTime getLatestEarlyBoundary() {
        List<Person> sortedEarliestAvailableTimes = getCoworkers().stream()
                .sorted(Comparator.comparing(coworker -> coworker.getCalendar().getEarliestAvailableTime()))
                .collect(Collectors.toList());
        Person coworkerWithLatestEarlyBoundary = sortedEarliestAvailableTimes.get(sortedEarliestAvailableTimes.size() - 1);
        return (getCalendar().getEarliestAvailableTime().isAfter(coworkerWithLatestEarlyBoundary.getCalendar().getEarliestAvailableTime())
                ? getCalendar().getEarliestAvailableTime()
                : coworkerWithLatestEarlyBoundary.getCalendar().getEarliestAvailableTime());
    }

    public List<Appointment> getUserAvailableSlots() {
        return userAvailableSlots;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public List<Person> getCoworkers() {
        return coworkers;
    }

    public AppointmentLength getLength() {
        return length;
    }

    public int getInterval() {
        return interval;
    }
}
