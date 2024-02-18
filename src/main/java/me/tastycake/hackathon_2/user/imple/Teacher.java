package me.tastycake.hackathon_2.user.imple;

import lombok.Getter;
import me.tastycake.hackathon_2.calendar.Activity;
import me.tastycake.hackathon_2.calendar.Upload;
import me.tastycake.hackathon_2.user.User;
import me.tastycake.hackathon_2.user.school.SchoolClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Teacher extends User implements Serializable {
    private List<Upload> uploads = new ArrayList<>();

    public Teacher(String name, String mail, String password, SchoolClass schoolClass, List<Upload> uploads) {
        super(name, mail, password, schoolClass);

        this.uploads = uploads;
    }

    public void addUpload(Upload upload) {
        // TODO: Notify teacher and give the upload data

        uploads.add(upload);
    }

    public void acceptUpload(Activity activity, Upload upload) {
        try {
            uploads.remove(upload);
        } catch (Exception ignore) {
            return;
        }

        getSchoolClass().acceptActivity(activity);
    }
}
