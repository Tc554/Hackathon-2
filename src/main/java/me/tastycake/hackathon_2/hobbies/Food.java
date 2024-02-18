package me.tastycake.hackathon_2.hobbies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Food implements Serializable {
    private String food;
    private boolean likeIt;

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//            put("food", food);
//            put("likeIt", likeIt);
//        }};
//    }
}
