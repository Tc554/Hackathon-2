package me.tastycake.user.imple;

import lombok.*;
import me.tastycake.calendar.Activity;
import me.tastycake.calendar.PupilCalendar;
import me.tastycake.hobbies.Food;
import me.tastycake.hobbies.Hobby;
import me.tastycake.serializer.Serializable;
import me.tastycake.user.School;
import me.tastycake.user.school.SchoolClass;
import me.tastycake.user.school.SchoolFloor;
import me.tastycake.utils.SortedMap;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pupil implements Serializable {
    private String name;
    private String mail;
    private List<Hobby> hobbies = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private PupilCalendar pupilCalendar;
    private SchoolClass schoolClass;
    private SchoolFloor schoolFloor;
    private School school;
    private List<Pupil> skipPupils = new ArrayList<>();

    private int points = 0;


    public void finishedActivity(Activity activity, int bonus) {
        try {
            pupilCalendar.getActivities().remove(activity);
        } catch (Exception ignore) {
            return;
        }
        points += activity.getU1().getSchool().getTotalPoint() + bonus;
    }

    @Override
    public SortedMap serialize() {
        return new SortedMap() {{
            put("name", name);
            put("hobbies", hobbies);
            put("foods", foods);
            put("pupilCalendar", pupilCalendar);
            put("schoolClass", schoolClass);
            put("schoolFloor", schoolFloor);
            put("school", school);
            put("skipPupils", skipPupils);
            put("points", points);
        }};
    }
}
