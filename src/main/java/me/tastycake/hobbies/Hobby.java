package me.tastycake.hobbies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tastycake.serializer.Serializable;
import me.tastycake.utils.SortedMap;

@Getter
@AllArgsConstructor
public class Hobby implements Serializable {
    private String hobby;
    private boolean likeIt;

    @Override
    public SortedMap serialize() {
        return SortedMap.autoCreate(this);
    }
}
