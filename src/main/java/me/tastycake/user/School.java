package me.tastycake.user;

import lombok.Builder;
import lombok.Getter;
import me.tastycake.user.imple.SchoolAdmin;
import me.tastycake.user.school.SchoolFloor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class School {
    private List<SchoolFloor> schoolFloors = new ArrayList<>();
    private SchoolAdmin schoolAdmin;
    private int defTimeToFinishActivity = 7;

    private int pointsPerActivity = 5;

    public int getTotalPoint() {
        int totalPoints = 0;

        for (SchoolFloor schoolFloor : schoolFloors) {
            totalPoints += schoolFloor.getTotalPoints();
        }

        return totalPoints;
    }
}
