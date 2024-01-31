package me.tastycake.user.imple;

import lombok.Builder;
import lombok.Getter;
import me.tastycake.calendar.Activity;
import me.tastycake.calendar.PupilCalendar;
import me.tastycake.hobbies.Food;
import me.tastycake.hobbies.Hobby;
import me.tastycake.user.School;
import me.tastycake.user.User;
import me.tastycake.user.school.SchoolClass;
import me.tastycake.user.school.SchoolFloor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
public class Pupil extends User {
    private String name;
    private List<Hobby> hobbies = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private PupilCalendar pupilCalender;
    private SchoolClass schoolClass;
    private SchoolFloor schoolFloor;
    private School school;

    private int points = 0;

    public void finishedActivity(Activity activity, int bonus) {
        try {
            pupilCalender.getActivities().remove(activity);
        } catch (Exception ignore) {
            return;
        }
        points += activity.getU1().getSchool().getTotalPoint() + bonus;
    }
}
