package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

public class AttendanceRecycledItems {

    public String Month;
    public String AttendancePercent;

    public AttendanceRecycledItems(String Month, String AttendancePercent)
    {
        this.Month = Month;
        this.AttendancePercent = AttendancePercent;
    }

    public String getMonth() {
        return Month;
    }

    public String getAttendancePercent() {
        return AttendancePercent;
    }

}
