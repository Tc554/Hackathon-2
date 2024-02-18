package me.tastycake.hackathon_2.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.tastycake.hackathon_2.user.imple.Pupil;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class PupilCalendar implements Serializable {
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
        pupil.getPupilCalendar().requestDate(date);
    }

    public Activity acceptDate(Pupil pupil, DateActivity dateActivity) {
        dateActivities.remove(dateActivity);
        pupil.getPupilCalendar().getDateActivities().remove(dateActivity);
        Activity activity = new Activity(requestedDate, this.pupil, pupil);
        activities.add(activity);
        pupil.getPupilCalendar().getActivities().add(activity);
        requestedDate = null;

        return activity;
    }

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//
//        }};
//    }
}