package com.nology.calendar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarApplicationTest {
    
    @Nested
    @DisplayName("Person") 
    class PersonTestCases {

        private final Person janeDoe = new Person("Jane Doe");
        private final Person jeremySmith = new Person("Jeremy Smith");

        @ParameterizedTest (name = "{0}")
        @DisplayName("instantiatePersonWithNameStoredAsProperty")
        @ValueSource(strings = {"Des", "Janet", "Ruth", "Sean"})
        void instantiatePersonObjectWithNameStoredAsProperty (String expectedName) {
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
        @DisplayName("DeleteContactToPersonObject")
        void DeleteContactToPersonObject() {
            janeDoe.addContact(jeremySmith);
            janeDoe.removeContact(jeremySmith);
            assertEquals(List.of(), janeDoe.getCoworkers(), () -> "Lists should be equal");
        }

        @Test
        @DisplayName("PersonNotInContactListException")
        void PersonNotInContactListException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoe.removeContact(jeremySmith));
            assertEquals("Coworker not in list of contacts", exception.getMessage());
        }

        @Test
        @DisplayName("displayPersonContactList")
        void displayPersonContactList() {
            janeDoe.addContact(jeremySmith);
            assertEquals("Jeremy Smith, ", janeDoe.showContacts());
            assertEquals("No contacts yet", jeremySmith.showContacts());
        }
    }

    @Nested
    @DisplayName("Calendar")
    class CalendarTestCases {

        private final Person janeDoe = new Person("Jane Doe");
        private final Calendar janeDoeCalendar = janeDoe.createCalendar(LocalTime.of(8, 0), LocalTime.of(18,0));

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
            janeDoeCalendar.createAppointment(AppointmentLength.THIRTY, LocalDateTime.of(2023, Month.AUGUST, 15, 14, 0));
            assertAll("properties",
                    () -> assertEquals(1, janeDoeCalendar.getAppointmentList().size()),
                    () -> assertEquals(LocalTime.of(14, 0), janeDoeCalendar.getAppointmentList().get(0).getStartTime()),
                    () -> assertEquals(LocalTime.of(14, 30), janeDoeCalendar.getAppointmentList().get(0).getEndTime())
            );
        }

        @Test
        @DisplayName("MeetingOutOfTimeRangeExceptionHandling")
        void meetingOutOfRangeException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 15, 19, 0)));
            assertEquals("Appointment @ 19:00 is outside of your working range", exception.getMessage());
        }

        @Test
        @DisplayName("DeleteAppointmentFromCalendar")
        void deleteAppointmentFromCalendar() {
            janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.AUGUST, 15, 16, 0));
            assertEquals(1, janeDoeCalendar.getAppointmentList().size());
            janeDoeCalendar.deleteAppointment(LocalDateTime.of(2023, Month.AUGUST, 15, 16, 0));
            assertEquals(0, janeDoeCalendar.getAppointmentList().size());
        }

        @Test
        @DisplayName("DeletingAppointmentInvalidRequest")
        void AppointmentDeletionInvalidException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoeCalendar.deleteAppointment(LocalDateTime.of(2023, Month.AUGUST, 12, 19, 0)));
            assertEquals("No such appointment in your calendar.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("CompareCalendarMethod")
    class CompareCalendarTestCases {

        private final Person janeDoe = new Person("Jane Doe");
        private final Calendar janeDoeCalendar = janeDoe.createCalendar(LocalTime.of(9, 0), LocalTime.of(12,0));
        private final Person jeremySmith = new Person("Jeremy Smith");
        private final Calendar jeremySmithCalendar = jeremySmith.createCalendar(LocalTime.of(9, 0), LocalTime.of(12, 0));

        @Test
        @DisplayName("throwExceptionContactNotInListOfContacts")
        void ContactInvalidException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 60, LocalDate.of(2023, Month.AUGUST, 10)));
            assertEquals("Coworker not in your contact list", exception.getMessage());
        }

        @Test
        @DisplayName("throwExceptionIntervalLengthShorterThanMeeting")
        void IntervalInvalidException() {
            jeremySmith.addContact(janeDoe);
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 0, LocalDate.of(2023, Month.AUGUST, 10)));
            assertEquals("Interval length cannot be shorter than meeting length", exception.getMessage());
        }

        @Test
        @DisplayName("CompareCalendarMethod")
        void compareCalendarMethod() {
            jeremySmith.addContact(janeDoe);
            jeremySmithCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.JULY, 13, 10, 0));
            jeremySmithCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.JULY, 15, 10, 0));
            janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.JULY, 13, 11, 0));
            janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalDateTime.of(2023, Month.JULY, 15, 9, 0));
            List<Appointment> expected = List.of(new Appointment(LocalDate.of(2023, Month.JULY, 13), LocalTime.of(9, 0),LocalTime.of(10, 0)));
            assertEquals(expected, jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 60, LocalDate.of(2023, Month.JULY, 13)));
        }


    }
    
}