package me.tastycake.serializer;

import lombok.Getter;
import lombok.Setter;
import me.tastycake.utils.SortedMap;

import java.util.HashMap;
import java.util.Map;

public interface Serializable {
    Data data = new Data();
    SortedMap serialize();

    @Getter
    @Setter
    class Data {
        private Map<Object, Data> data = new HashMap<>();
        private String id = "NaN";
    }
}
