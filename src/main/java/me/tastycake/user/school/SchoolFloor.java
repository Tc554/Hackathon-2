package me.tastycake.user.school;

import java.util.ArrayList;
import java.util.List;

public class SchoolFloor {
    private List<SchoolClass> schoolClasses = new ArrayList<>();

    public int getTotalPoints() {
        int totalPoints = 0;

        for (SchoolClass schoolClass : schoolClasses) {
            totalPoints += schoolClass.getTotalScore();
        }

        return totalPoints;
    }
}
