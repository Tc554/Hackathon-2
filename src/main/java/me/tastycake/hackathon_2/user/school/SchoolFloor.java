package me.tastycake.hackathon_2.user.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.tastycake.hackathon_2.user.School;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolFloor implements Serializable {
    private School school;
    private List<SchoolClass> schoolClasses = new ArrayList<>();

    public int getTotalPoints() {
        int totalPoints = 0;

        for (SchoolClass schoolClass : schoolClasses) {
            totalPoints += schoolClass.getTotalScore();
        }

        return totalPoints;
    }

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//            put("schoolClasses", schoolClasses);
//        }};
//    }
}
