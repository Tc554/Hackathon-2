package me.tastycake.hackathon_2.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tastycake.hackathon_2.user.imple.Pupil;

import javax.swing.*;
import java.util.Date;

@Getter
@AllArgsConstructor
public class Activity {
    private Date date;
    private Pupil u1;
    private Pupil u2;

    public void finish(int bonus) {
        u1.finishedActivity(this, bonus);
        u2.finishedActivity(this, bonus);
    }

    public void requestUpload() {
        // TODO: Open camera, get image
        // replace ImageIcon with Bitmap

        ImageIcon imageIcon = new ImageIcon();

        u1.getSchoolClass().getTeacher().addUpload(new Upload(imageIcon, this));
    }
}
