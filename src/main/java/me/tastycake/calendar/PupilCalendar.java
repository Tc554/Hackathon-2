package me.tastycake.calendar;

import lombok.Getter;
import lombok.Setter;
import me.tastycake.user.imple.Pupil;

import java.util.*;

@Getter
@Setter
public class PupilCalendar {
    private Date requestedDate;
    private List<Activity> activities = new ArrayList<>();
    private Pupil pupil;
    private List<DateActivity> dateActivities = new ArrayList<>();

    public PupilCalendar(Pupil pupil) {
        this.pupil = pupil;
    }

    public void createDateActivity(Pupil u2) {
        dateActivities.add(DateActivity.createDateActivity(pupil, u2));
    }

    public void requestDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public void askForADate(Date date, Pupil pupil) {
        pupil.getPupilCalender().requestDate(date);
    }

    public Activity acceptDate(Pupil pupil, DateActivity dateActivity) {
        dateActivities.remove(dateActivity);
        pupil.getPupilCalender().getDateActivities().remove(dateActivity);
        Activity activity = new Activity(requestedDate, this.pupil, pupil);
        activities.add(activity);
        pupil.getPupilCalender().getActivities().add(activity);
        requestedDate = null;

        return activity;
    }
}