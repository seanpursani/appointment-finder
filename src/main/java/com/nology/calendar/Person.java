package com.nology.calendar;

import org.springframework.cglib.core.Local;

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
        System.out.println("Hi " + getName() + "! Your calendar has been created with the bounds, " + earliestAvailableTime + "-" + latestAvailableTime);
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
        if (coworkers.stream().noneMatch(contact -> Objects.equals(contact.getName(), coworker.getName()))) throw new IllegalArgumentException("Coworker not in your contact list");
        LocalTime earliestBoundary = (getCalendar().getEarliestAvailableTime().isAfter(coworker.getCalendar().getEarliestAvailableTime())
                ? getCalendar().getEarliestAvailableTime()
                : coworker.getCalendar().getEarliestAvailableTime());
        LocalTime latestBoundary = (getCalendar().getLatestAvailableTime().isBefore(coworker.getCalendar().getLatestAvailableTime())
                ? getCalendar().getLatestAvailableTime()
                : coworker.getCalendar().getLatestAvailableTime());
        //allAvailableSlots.addAll(availableSlot(getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength), interval));
        allAvailableSlots.addAll(availableSlot(coworker.getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength), interval));
        for (Appointment appointment : allAvailableSlots) {
            if (!sharedTimeSlots.add(appointment)) { meetingTimes.append(appointment).append("\n"); } }
        System.out.println(allAvailableSlots.stream().sorted(Comparator.comparing(Appointment::getStartTime)).collect(Collectors.toList()));
        System.out.println(sharedTimeSlots.stream().sorted(Comparator.comparing(Appointment::getStartTime)).collect(Collectors.toList()));
        return (meetingTimes.toString().length() > 0)
                ? "Woohoo! Here are times when everyone is available: \n" + meetingTimes.toString()
                : "Sorry, no appointments can be made.";
    }

    private List<Appointment> availableSlot(Calendar calendar, LocalTime earliestBoundary, LocalTime latestBoundary, long length, int interval) {
        List<Appointment> availableSlots = new ArrayList<>();
        LocalTime tempEarliestBoundary = earliestBoundary;
        LocalTime tempLatestBoundary;
        boolean isEarly = false;
        System.out.println("Overall Boundary " + tempEarliestBoundary + ": " + latestBoundary);
        List<Appointment> userAppointments = calendar.getAppointmentList();
        System.out.println(userAppointments);
        for (int i = 0; i < userAppointments.size() + 1 ; i++) {
            if (i == 0 && (earliestBoundary.isAfter(userAppointments.get(i).getStartTime()) || earliestBoundary.equals(userAppointments.get(i).getStartTime()))) {
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
                System.out.println("1 Earliest " + tempEarliestBoundary + " " + "Latest " + tempLatestBoundary);
            } else if (i == 0 && (earliestBoundary.isBefore(userAppointments.get(i).getStartTime()) || earliestBoundary.equals(userAppointments.get(i).getStartTime()))) {
                tempLatestBoundary = userAppointments.get(i).getStartTime();
                System.out.println("2 Earliest " + tempEarliestBoundary + " " + "Latest " + tempLatestBoundary);
                isEarly = true;
            } else if (isEarly && i < userAppointments.size() - 1) {
                tempEarliestBoundary = userAppointments.get(i-1).getEndTime();
                tempLatestBoundary = userAppointments.get(i).getStartTime();
                System.out.println("3 Earliest " + tempEarliestBoundary + " " + "Latest " + tempLatestBoundary);
            } else if (!isEarly && i > 0 && i < userAppointments.size()-1) {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
                System.out.println("4 Earliest " + tempEarliestBoundary + " " + "Latest " + tempLatestBoundary);
            }
            else {
                tempEarliestBoundary = userAppointments.get(i-1).getEndTime();
                tempLatestBoundary = latestBoundary;
                System.out.println("5 Earliest " + tempEarliestBoundary + " " + "Latest " + tempLatestBoundary);
            }
            while (tempEarliestBoundary.plusMinutes(length).isBefore(tempLatestBoundary) || tempEarliestBoundary.plusMinutes(length).equals(tempLatestBoundary)) {
                Appointment newAppointment = new Appointment(tempEarliestBoundary, tempEarliestBoundary.plusMinutes(length));
                availableSlots.add(newAppointment);
                tempEarliestBoundary = tempEarliestBoundary.plusMinutes(interval);
                System.out.println("New Appointment " + newAppointment);
            }
        }
        return availableSlots;
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
        return "Name= " + name + "\n" + calendar + "Coworkers= " + showContacts();
    }


}

