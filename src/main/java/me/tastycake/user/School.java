package me.tastycake.user;

import lombok.*;
import me.tastycake.serializer.Serializable;
import me.tastycake.user.imple.SchoolAdmin;
import me.tastycake.user.school.SchoolFloor;
import me.tastycake.utils.SortedMap;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School implements Serializable {
    private List<SchoolFloor> schoolFloors = new ArrayList<>();
    private SchoolAdmin schoolAdmin;

    @NonNull
    private int defTimeToFinishActivity = 7;
    @NonNull
    private int pointsPerActivity = 5;

    public School(int defTimeToFinishActivity, int pointsPerActivity) {
        this.defTimeToFinishActivity = defTimeToFinishActivity;
        this.pointsPerActivity = pointsPerActivity;
    }

    public int getTotalPoint() {
        int totalPoints = 0;

        for (SchoolFloor schoolFloor : schoolFloors) {
            totalPoints += schoolFloor.getTotalPoints();
        }

        return totalPoints;
    }

    @Override
    public SortedMap serialize() {
        return new SortedMap() {{
            put("schoolFloors", schoolFloors);
            put("schoolAdmin", schoolAdmin);
            put("defTimeToFinishActivity", defTimeToFinishActivity);
            put("pointsPerActivity", pointsPerActivity);
        }};
    }
}
