package me.tastycake.user.imple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.tastycake.calendar.Activity;
import me.tastycake.calendar.Upload;
import me.tastycake.user.School;
import me.tastycake.user.school.SchoolClass;
import me.tastycake.user.school.SchoolFloor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private School school;
    private SchoolFloor schoolFloor;
    private SchoolClass schoolClass;
    private List<Upload> uploads = new ArrayList<>();

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

        schoolClass.acceptActivity(activity);
    }
}
