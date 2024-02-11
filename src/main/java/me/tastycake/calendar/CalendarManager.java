package me.tastycake.calendar;

import lombok.Getter;
import me.tastycake.user.imple.Pupil;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CalendarManager {
    private static Map<Pupil, PupilCalendar> calendars = new HashMap<>();

    public static void add(Pupil pupil, PupilCalendar pupilCalendar) {
        calendars.put(pupil, pupilCalendar);
    }
}
