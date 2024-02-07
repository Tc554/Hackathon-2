package me.tastycake.user.school;

import lombok.Getter;
import lombok.Setter;
import me.tastycake.calendar.Activity;
import me.tastycake.serializer.Serializable;
import me.tastycake.user.imple.Pupil;
import me.tastycake.user.imple.Teacher;
import me.tastycake.utils.SortedMap;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SchoolClass implements Serializable {
    private List<Pupil> pupils = new ArrayList<>();
    private List<Activity> activeActivities = new ArrayList<>();
    private Teacher teacher;

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

    @Override
    public SortedMap serialize() {
        return SortedMap.autoCreate(this);
    }
}