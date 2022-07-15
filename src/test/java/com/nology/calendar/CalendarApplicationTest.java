package com.nology.calendar;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
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
        @DisplayName("displayPersonContactList")
        void displayPersonContactList() {
            janeDoe.addContact(jeremySmith);
            assertEquals("Jeremy Smith ", janeDoe.showContacts());
            assertEquals("No contacts yet", jeremySmith.showContacts());
        }
    }

    @Nested
    @DisplayName("Calendar")
    class CalendarTestCases {

        private final Person janeDoe = new Person("Jane Doe");
        private final Calendar janeDoeCalendar = janeDoe.setDailyBound(LocalTime.of(8, 0), LocalTime.of(18,0));

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
        @DisplayName("MeetingOutOfTimeRangeExceptionHandling")
        void meetingOutOfRangeException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(19, 0)));
            assertEquals("Appointment @ 19:00 is outside of your working range", exception.getMessage());
        }

        @Test
        @DisplayName("DeleteAppointmentFromCalendar")
        void deleteAppointmentFromCalendar() {
            janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(16, 0));
            assertEquals(1, janeDoeCalendar.getAppointmentList().size());
            janeDoeCalendar.deleteAppointment(LocalTime.of(16, 0));
            assertEquals(0, janeDoeCalendar.getAppointmentList().size());
        }

        @Test
        @DisplayName("DeletingAppointmentInvalidRequest")
        void AppointmentDeletionInvalidException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    janeDoeCalendar.deleteAppointment(LocalTime.of(19, 0)));
            assertEquals("No such appointment in your calendar.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("CompareCalendarMethod")
    class CompareCalendarTestCases {

        private final Person janeDoe = new Person("Jane Doe");
        private final Calendar janeDoeCalendar = janeDoe.setDailyBound(LocalTime.of(9, 0), LocalTime.of(12,0));
        private final Person jeremySmith = new Person("Jeremy Smith");
        private final Calendar jeremySmithCalendar = jeremySmith.setDailyBound(LocalTime.of(9, 0), LocalTime.of(12, 0));

        @Test
        @DisplayName("throwExceptionContactNotInListOfContacts")
        void ContactInvalidException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 60));
            assertEquals("Coworker not in your contact list", exception.getMessage());
        }

        @Test
        @DisplayName("throwExceptionIntervalLengthShorterThanMeeting")
        void IntervalInvalidException() {
            jeremySmith.addContact(janeDoe);
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 0));
            assertEquals("The interval length cannot be shorter than the meeting length", exception.getMessage());
        }

        @Test
        @DisplayName("CompareCalendarMethod")
        void compareCalendarMethod() {
            jeremySmith.addContact(janeDoe);
            jeremySmithCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(10, 0));
            janeDoeCalendar.createAppointment(AppointmentLength.SIXTY, LocalTime.of(11, 0));
            assertEquals("Here are the time slots when everyone is available:" + "\n" + "09:00-10:00" + "\n", jeremySmith.compareCalendar(List.of(janeDoe), AppointmentLength.SIXTY, 60));
        }




    }
    
}