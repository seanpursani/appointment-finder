package com.nology.calendar;

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

    public String compareSchedule() {
        StringBuilder meetingTimes = new StringBuilder("Here are the time slots when everyone is available: \n");
        List<LocalTime> timeBoundaries = getScheduleBoundaries();
        List<Appointment> allAvailableSlots = new ArrayList<>(checkAvailableSlots(getCalendar(), timeBoundaries.get(0), timeBoundaries.get(1), getCalendar().getIntFromEnum(getLength()), getInterval()));
        for (Person coworker: getCoworkers()) {
            allAvailableSlots.addAll(checkAvailableSlots(coworker.getCalendar(), timeBoundaries.get(0), timeBoundaries.get(1), getCalendar().getIntFromEnum(getLength()), getInterval()));
        }
        List<Appointment> sharedTimeSlots = allAvailableSlots.stream()
                .filter(appointment -> Collections.frequency(allAvailableSlots, appointment) == getCoworkers().size()+1)
                .distinct()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .collect(Collectors.toList());
        if (sharedTimeSlots.size() < 1) return "Sorry no appointments can be made!";
        for (Appointment appointment: sharedTimeSlots) {
            meetingTimes.append(appointment).append("\n");
        }
        return meetingTimes.toString();
    }

    private List<Appointment> checkAvailableSlots(Calendar calendar, LocalTime earliestBoundary, LocalTime latestBoundary, long length, int interval) {
        if (interval < length) throw new IllegalArgumentException("The interval length cannot be shorter than the meeting length");
        LocalTime tempEarliestBoundary = earliestBoundary;
        LocalTime tempLatestBoundary = latestBoundary;
        List<Appointment> userAppointments = calendar.getAppointmentList().stream()
                .filter(appointment -> isInRange(appointment, earliestBoundary, latestBoundary))
                .collect(Collectors.toList());
        if ((userAppointments.size()) < 1) return List.of();
        boolean isEarliestBoundaryBeforeUsers = userAppointments.get(0).getStartTime().isAfter(earliestBoundary);
        int count = (isEarliestBoundaryBeforeUsers) ? 1 : 0;
        for (int i = 0; i < userAppointments.size() + count; i++) {
            if (i == 0 && userAppointments.size() == 1) {
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
                Appointment newAppointment = new Appointment(tempEarliestBoundary, tempEarliestBoundary.plusMinutes(length));
                getUserAvailableSlots().add(newAppointment);
                tempEarliestBoundary = tempEarliestBoundary.plusMinutes(interval);
            }
        }
        return getUserAvailableSlots();
    }

    private boolean isInRange(Appointment appointment, LocalTime earliestBoundary, LocalTime latestBoundary) {
        boolean isValid = !appointment.getEndTime().isBefore(earliestBoundary);
        if (appointment.getStartTime().isBefore(earliestBoundary) && !appointment.getEndTime().isAfter(earliestBoundary)) isValid = false;
        if (appointment.getStartTime().isAfter(latestBoundary) || appointment.getStartTime().equals(latestBoundary)) isValid = false;
        return isValid;
    }

    public List<LocalTime> getScheduleBoundaries() {
        List<Person> sortedEarliestAvailableTimes = getCoworkers().stream()
                .sorted(Comparator.comparing(coworker -> coworker.getCalendar().getEarliestAvailableTime()))
                .collect(Collectors.toList());
        Person coworkerWithLatestEarlyBoundary = sortedEarliestAvailableTimes.get(sortedEarliestAvailableTimes.size() - 1);
        List<Person> sortedLatestAvailableTimes = getCoworkers().stream()
                .sorted(Comparator.comparing(coworker -> coworker.getCalendar().getLatestAvailableTime()))
                .collect(Collectors.toList());
        Person coworkerWithEarliestLateBoundary = sortedLatestAvailableTimes.get(0);
        LocalTime earliestBoundary = (getCalendar().getEarliestAvailableTime().isAfter(coworkerWithLatestEarlyBoundary.getCalendar().getEarliestAvailableTime())
                ? getCalendar().getEarliestAvailableTime()
                : coworkerWithLatestEarlyBoundary.getCalendar().getEarliestAvailableTime());
        LocalTime latestBoundary = (getCalendar().getLatestAvailableTime().isBefore(coworkerWithEarliestLateBoundary.getCalendar().getLatestAvailableTime())
                ? getCalendar().getLatestAvailableTime()
                : coworkerWithEarliestLateBoundary.getCalendar().getLatestAvailableTime());
        return List.of(earliestBoundary, latestBoundary);
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
