package me.tastycake.hackathon_2.calendar;

import lombok.Getter;
import me.tastycake.hackathon_2.user.imple.Pupil;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CalendarManager {
    private static Map<Pupil, PupilCalendar> calendars = new HashMap<>();

    public static void add(Pupil pupil, PupilCalendar pupilCalendar) {
        calendars.put(pupil, pupilCalendar);
    }
}
