package com.nology.calendar;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        System.out.println("Hi " + getName() + "! Your calendar has been created with your specified bounds, " + earliestAvailableTime + "-" + latestAvailableTime);
        return calendar;
    }

    public String addContact(Person coworker) {
        coworkers.add(coworker);
        return "Successfully added " + coworker.getName() + " to your list of contacts";
    }

    public List<Appointment> compareCalendar(Person coworker, AppointmentLength appointmentLength) {
        if (coworkers.stream().noneMatch(contact -> Objects.equals(contact.getName(), coworker.getName()))) throw new IllegalArgumentException("Coworker not in your contact list");
        LocalTime earliestBoundary = (getCalendar().getEarliestAvailableTime().isAfter(coworker.getCalendar().getEarliestAvailableTime())
                ? getCalendar().getEarliestAvailableTime()
                : coworker.getCalendar().getEarliestAvailableTime());
        LocalTime latestBoundary = (getCalendar().getLatestAvailableTime().isAfter(coworker.getCalendar().getLatestAvailableTime())
                ? getCalendar().getLatestAvailableTime()
                : coworker.getCalendar().getLatestAvailableTime());
        List<Appointment> availableSlots = new ArrayList<>();
        availableSlots.addAll(availableSlot(getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength)));
        availableSlots.addAll(availableSlot(coworker.getCalendar(), earliestBoundary, latestBoundary, getCalendar().getIntFromEnum(appointmentLength)));
        return availableSlots.stream().distinct().collect(Collectors.toList());
    }

    private List<Appointment> availableSlot(Calendar calendar, LocalTime earliestBoundary, LocalTime latestBoundary, long length) {
        List<Appointment> availableAppointments = new ArrayList<>();
        LocalTime tempEarliestBoundary = earliestBoundary;
        LocalTime tempLatestBoundary = latestBoundary;
        List<Appointment> userAppointments = getCalendar().getAppointmentList();
        for (int i = 0; i < userAppointments.size(); i++) {
            if (earliestBoundary.isAfter(userAppointments.get(i).getStartTime())) {
                tempLatestBoundary = userAppointments.get(i+1).getStartTime();
            }
            while (tempEarliestBoundary.plusMinutes(60).isBefore(tempLatestBoundary) || tempEarliestBoundary.plusMinutes(60).equals(tempLatestBoundary)) {
                availableAppointments.add(new Appointment(tempEarliestBoundary, tempEarliestBoundary.plusMinutes(length)));
                tempEarliestBoundary = tempEarliestBoundary.plusMinutes(15);
            }
            if (i < userAppointments.size() - 2) {
                tempEarliestBoundary = userAppointments.get(i+1).getEndTime();
                tempLatestBoundary = userAppointments.get(i+2).getStartTime();
                System.out.println(tempEarliestBoundary + " " + tempLatestBoundary);
            } else {
                tempEarliestBoundary = userAppointments.get(i).getEndTime();
                tempLatestBoundary = latestBoundary;
                System.out.println(tempEarliestBoundary + " " + tempLatestBoundary);
            }
        }
        return availableAppointments;
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

