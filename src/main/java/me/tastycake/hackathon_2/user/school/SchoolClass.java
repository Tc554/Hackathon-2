package me.tastycake.hackathon_2.user.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.tastycake.hackathon_2.calendar.Activity;
import me.tastycake.hackathon_2.user.imple.Pupil;
import me.tastycake.hackathon_2.user.imple.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass implements Serializable {
    private List<Pupil> pupils = new ArrayList<>();
    private List<Activity> activeActivities = new ArrayList<>();
    private SchoolFloor schoolFloor;
    private Teacher teacher;
    private String teacherMail;

    public int getTotalScore() {
        int totalScore = 0;

        for (Pupil pupil : pupils) {
            totalScore += pupil.getPoints();
        }

        return totalScore;
    }

    public void acceptActivity(Activity activity, int... bonus) {
        try {
            activeActivities.remove(activity);
        } catch (Exception ignore) {
            return;
        }

        int b = bonus.length > 0 ? bonus[0] : 0;

        activity.finish(b);
    }

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//           put("pupils", pupils);
//           put("activeActivities", activeActivities);
//           put("teacher", teacher);
//        }};
//    }
}