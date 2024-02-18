package me.tastycake.hackathon_2.user.imple;

import lombok.*;
import me.tastycake.hackathon_2.calendar.Activity;
import me.tastycake.hackathon_2.calendar.PupilCalendar;
import me.tastycake.hackathon_2.hobbies.Food;
import me.tastycake.hackathon_2.hobbies.Hobby;
import me.tastycake.hackathon_2.user.User;
import me.tastycake.hackathon_2.user.school.SchoolClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class Pupil extends User implements Serializable {
    private List<Hobby> hobbies = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private PupilCalendar pupilCalendar;
    private List<Pupil> skipPupils = new ArrayList<>();

    private int points = 0;

    public Pupil(String name, String mail, String password, List<Hobby> hobbies, List<Food> foods, PupilCalendar pupilCalendar, SchoolClass schoolClass, List<Pupil> skipPupils, int points) {
        super(name, mail, password, schoolClass);

        this.hobbies = hobbies;
        this.foods = foods;
        this.pupilCalendar = pupilCalendar;
        // this.schoolClass = schoolClass;
        this.skipPupils = skipPupils;
        this.points = points;
    }

    public void finishedActivity(Activity activity, int bonus) {
        try {
            pupilCalendar.getActivities().remove(activity);
        } catch (Exception ignore) {
            return;
        }
        points += activity.getU1().getSchoolClass().getSchoolFloor().getSchool().getTotalPoint() + bonus;
    }

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//           put("name", name);
//           put("mail", mail);
//           put("hobbies", hobbies);
//           put("foods", foods);
//           put("pupilCalendar", pupilCalendar);
//           put("schoolClass", schoolClass);
//           put("schoolFloor", schoolFloor);
//           put("school", school);
//           put("skipPupils", skipPupils);
//           put("points", points);
//        }};
//    }
}
