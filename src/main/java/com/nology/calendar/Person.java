package com.nology.calendar;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private Calendar calendar;
    private final List<Person> coworkers = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public Calendar setDailyBound(LocalTime earliestAvailableTime, LocalTime latestAvailableTime) {
        setCalendar(new Calendar(earliestAvailableTime, latestAvailableTime));
        System.out.println("Hi " + getName() + "! Your calendar has been created with your earliest and latest availability being, " + earliestAvailableTime + "-" + latestAvailableTime);
        return calendar;
    }

    public String addContact(Person coworker) {
        coworkers.add(coworker);
        return "Successfully added " + coworker.getName() + " to your list of contacts";
    }

    public String compareCalendar(Person coworker, AppointmentLength appointmentLength, int interval) {
        Set<Appointment> sharedTimeSlots = new HashSet<>();
        StringBuilder meetingTimes = new StringBuilder();
        List<Appointment> allAvailableSlots = new ArrayList<>();

        if (coworkers.stream().noneMatch(contact -> Objects.equals(contact.getName(), coworker.getName())))
            throw new IllegalArgumentException("Coworker not in your contact list");
        LocalTime earliestBoundary = (getCalendar().getEarliestAvailableTime().isAfter(coworker.getCalendar().getEarliestAvailableTime())
                ? getCalendar().getEarliestAvailableTime()
                : coworker.getCalendar().getEarliestAvailableTime());
        LocalTime latestBoundary = (getCalendar().getLatestAvailableTime().isBefore(coworker.getCalendar().getLatestAvailableTime())
                ? getCalendar().getLatestAvailableTime()
                : coworker.getCalendar().getLatestAvailableTime());

        allAvailableSlots.addAll(checkAvailableSlots(getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength), interval));
        allAvailableSlots.addAll(checkAvailableSlots(coworker.getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength), interval));

        for (Appointment appointment : allAvailableSlots) {
            if (!sharedTimeSlots.add(appointment)) { meetingTimes.append(appointment).append("\n"); } }
        return (meetingTimes.toString().length() > 0)
                ? "Yay! Here are the slots when everyone is available: \n" + meetingTimes.toString()
                : "Sorry! No appointments can be made.";
    }

    private List<Appointment> checkAvailableSlots(Calendar calendar, LocalTime earliestBoundary, LocalTime latestBoundary, long length, int interval) {
        List<Appointment> userAvailableSlots = new ArrayList<>();
        LocalTime tempEarliestBoundary = earliestBoundary;
        LocalTime tempLatestBoundary;
        List<Appointment> userAppointments = calendar.getAppointmentList();
        boolean isEarliestBoundaryBeforeUsers = !earliestBoundary.isAfter(userAppointments.get(0).getStartTime()) && !earliestBoundary.equals(userAppointments.get(0).getStartTime());
        int count = isEarliestBoundaryBeforeUsers ? 1 : 0;
        for (int i = 0; i < userAppointments.size() + count; i++) {
            if (i == 0 && (earliestBoundary.isAfter(userAppointments.get(i).getStartTime()) || earliestBoundary.equals(userAppointments.get(i).getStartTime()))) {
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
            } else if (i == 0 && (earliestBoundary.isBefore(userAppointments.get(i).getStartTime()) || earliestBoundary.equals(userAppointments.get(i).getStartTime()))) {
                tempLatestBoundary = userAppointments.get(i).getStartTime();
            } else if (isEarliestBoundaryBeforeUsers && i < userAppointments.size() - 1) {
                tempEarliestBoundary = userAppointments.get(i-1).getEndTime();
                tempLatestBoundary = userAppointments.get(i).getStartTime();
            } else if (!isEarliestBoundaryBeforeUsers && i > 0 && i < userAppointments.size()-1) {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
            } else {
                tempEarliestBoundary = userAppointments.get(i-count).getEndTime();
                tempLatestBoundary = (isEarliestBoundaryBeforeUsers && i == userAppointments.size() - count)
                        ? userAppointments.get(i).getStartTime()
                        : latestBoundary;
            }
            while (tempEarliestBoundary.plusMinutes(length).isBefore(tempLatestBoundary) || tempEarliestBoundary.plusMinutes(length).equals(tempLatestBoundary)) {
                Appointment newAppointment = new Appointment(tempEarliestBoundary, tempEarliestBoundary.plusMinutes(length));
                userAvailableSlots.add(newAppointment);
                tempEarliestBoundary = tempEarliestBoundary.plusMinutes(interval);
            }
        }
        return userAvailableSlots;
    }

    public String showContacts() {
        List<String> contacts = coworkers.stream().map(Person::getName).collect(Collectors.toList());
        StringBuilder contactsList = new StringBuilder();
        for (String contact: contacts) { contactsList.append(contact).append(" "); }
        return (contactsList.toString().length() == 0) ? "no contacts yet" : contactsList.toString();
    }

    public String getName() {
        return name;
    }

    private void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    private Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return "Name= " + name + "\n" + calendar + "Co-workers= " + showContacts();
    }


}

