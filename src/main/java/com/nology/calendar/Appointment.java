package com.nology.calendar;

import java.time.LocalTime;

public class Appointment {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Appointment(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return  startTime + "-" + endTime;
    }
}
