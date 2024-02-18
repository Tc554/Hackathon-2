package me.tastycake.hackathon_2.calendar;

import lombok.AllArgsConstructor;
import me.tastycake.hackathon_2.Main;
import me.tastycake.hackathon_2.suggestion.Suggestion;
import me.tastycake.hackathon_2.user.School;
import me.tastycake.hackathon_2.user.imple.Pupil;
import me.tastycake.hackathon_2.user.school.SchoolClass;

import java.util.*;

@AllArgsConstructor
public class DateActivity {
    private Date startDate;
    private Date endDate;
    private School school;
    private Pupil u1, u2;

    public static void pick(SchoolClass schoolClass) {
        List<Pupil> pickedPupils = new ArrayList<>();

        Pupil current = null;

        List<Pupil> pupils = new ArrayList<>(schoolClass.getPupils());
        Collections.shuffle(pupils);
        int timesToRepeat = 1;

        while (timesToRepeat > 0) {
            for (Pupil pupil : pupils) {
                if (pickedPupils.contains(pupil)) continue;

                if (current == null) current = pupil;
                else {
                    if (current == pupil) continue;
                    if (current.getSkipPupils() != null) {
                        if (current.getSkipPupils().contains(pupil)) {
                            timesToRepeat++;
                            continue;
                        }
                    }
                    Main.gpt.send(Suggestion.Prompt.builder()
                            .self(current)
                            .user2(pupil)
                            .build());
                    current.getPupilCalendar().createDateActivity(pupil);
                    pupil.getPupilCalendar().createDateActivity(current);
                    pickedPupils.add(pupil);
                    pickedPupils.add(current);
                    current = null;
                }
            }
            timesToRepeat--;
        }
    }

    public static DateActivity createDateActivity(Pupil u1, Pupil u2) {
        Calendar calendar = Calendar.getInstance();
        Date sd = calendar.getTime();
        Date ed = (Date) sd.clone();
        School s = u1.getSchoolClass().getSchoolFloor().getSchool();
        ed.setDate(sd.getDate() + s.getDefTimeToFinishActivity());

        return new DateActivity(sd, ed, s, u1, u2);
    }
}
