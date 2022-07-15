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
    
    public String removeContact(Person coworker) {
        coworkers.remove(coworker);
        return "Successfully removed " + coworker.getName() + " from your list of contacts";
    }

    public String compareCalendar(List<Person> coworkersToCompareAgainst, AppointmentLength length, int interval) {
        for (Person coworker : coworkersToCompareAgainst) {
            if (!getCoworkers().contains(coworker))
                throw new IllegalArgumentException("Coworker not in your contact list");
        }
        return new ScheduleComparator(getCalendar(), coworkersToCompareAgainst, length, interval).compareSchedule();
    }

    public String showContacts() {
        List<String> contacts = coworkers.stream().map(Person::getName).collect(Collectors.toList());
        StringBuilder contactsList = new StringBuilder();
        for (String contact: contacts) { contactsList.append(contact).append(", "); }
        return (contactsList.toString().length() == 0) ? "No contacts yet" : contactsList.toString();
    }

    public List<Person> getCoworkers() {
        return coworkers;
    }

    public String getName() {
        return name;
    }

    private void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return "Name= " + name + "\n" + calendar + "Co-workers= " + showContacts();
    }

}

