package me.tastycake.hackathon_2.hobbies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Hobby implements Serializable {
    private String hobby;
    private boolean likeIt;

//    @Override
//    public SortedMap serialize() {
//        return new SortedMap() {{
//            put("hobby", hobby);
//            put("likeIt", likeIt);
//        }};
//    }
}
