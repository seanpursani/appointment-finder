package com.nology.calendar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarApplicationTest {
    
    @Nested
    @DisplayName("Person") 
    class PersonTestCases {

        Person janeDoe = new Person("Jane Doe");
        Person jeremySmith = new Person("Jeremy Smith");

        @ParameterizedTest (name = "{0}")
        @DisplayName("instantiatePersonWithNameStoredAsProperty")
        @ValueSource(strings = {"Des", "Janet", "Ruth", "Sean"})
        void ShouldInstantiatePersonWithNameStoredAsProperty (String expectedName) {
            Person person = new Person(expectedName);
            assertEquals(expectedName, person.getName());
        }

        @Test
        @DisplayName("AddContactToPersonObject")
        void addContactToPersonObject() {
            janeDoe.addContact(jeremySmith);
            List<Person> expected = new ArrayList<>(List.of(jeremySmith));
            assertEquals(expected, janeDoe.getCoworkers(), () -> "Lists should be equal");
        }

        @Test
        @DisplayName("displayContactList")
        void displayContactList() {
            janeDoe.addContact(jeremySmith);
            assertEquals("Jeremy Smith ", janeDoe.showContacts());
            assertEquals("No contacts yet", jeremySmith.showContacts());
        }
    }

    @Nested
    @DisplayName("Calendar")
    class CalendarTestCases {

        Person janeDoe = new Person("Jane Doe");
        Calendar janeDoeCalendar = janeDoe.setDailyBound(LocalTime.of(8, 0), LocalTime.of(18,0));

        @Test
        @DisplayName("Testing properties of calendar object")
        void calendarProperties() {
            assertAll("properties",
                    () -> assertEquals(LocalTime.of(8, 0), janeDoeCalendar.getEarliestAvailableTime()),
                    () -> assertEquals(LocalTime.of(18, 0), janeDoeCalendar.getLatestAvailableTime()),
                    () -> assertEquals(0, janeDoeCalendar.getAppointmentList().size())
            );
        }

        @Test
        @DisplayName("CreateAppointmentMethod")
        void createAppointmentMethod() {
            janeDoeCalendar.createAppointment(AppointmentLength.THIRTY, LocalTime.of(14, 0));
            assertAll("properties",
                    () -> assertEquals(1, janeDoeCalendar.getAppointmentList().size()),
                    () -> assertEquals(LocalTime.of(14, 0), janeDoeCalendar.getAppointmentList().get(0).getStartTime()),
                    () -> assertEquals(LocalTime.of(14, 30), janeDoeCalendar.getAppointmentList().get(0).getEndTime())
            );
        }

        @Test
        @DisplayName("Meeting out of range exception")
        void meetingOutOfRangeException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(19, 0)));
            assertEquals("Appointment @ 19:00 is outside of your working range", exception.getMessage());
        }

    }
    
}