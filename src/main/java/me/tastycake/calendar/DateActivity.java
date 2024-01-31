package me.tastycake.calendar;

import lombok.AllArgsConstructor;
import me.tastycake.user.School;
import me.tastycake.user.imple.Pupil;

import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
public class DateActivity {
    private Date startDate;
    private Date endDate;
    private School school;
    private Pupil u1, u2;

    public static DateActivity createDateActivity(Pupil u1, Pupil u2) {
        Calendar calendar = Calendar.getInstance();
        Date sd = calendar.getTime();
        Date ed = (Date) sd.clone();
        School s = u1.getSchool();
        ed.setDate(sd.getDate() + s.getDefTimeToFinishActivity());

        return new DateActivity(sd, ed, s, u1, u2);
    }
}
