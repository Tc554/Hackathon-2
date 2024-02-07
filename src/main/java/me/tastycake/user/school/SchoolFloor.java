package me.tastycake.user.school;

import lombok.Getter;
import lombok.Setter;
import me.tastycake.serializer.Serializable;
import me.tastycake.utils.SortedMap;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SchoolFloor implements Serializable {
    private List<SchoolClass> schoolClasses = new ArrayList<>();

    public int getTotalPoints() {
        int totalPoints = 0;

        for (SchoolClass schoolClass : schoolClasses) {
            totalPoints += schoolClass.getTotalScore();
        }

        return totalPoints;
    }

    @Override
    public SortedMap serialize() {
        return SortedMap.autoCreate(this);
    }
}
