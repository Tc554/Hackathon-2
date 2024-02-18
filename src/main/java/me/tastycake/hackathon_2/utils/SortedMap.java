package me.tastycake.hackathon_2.utils;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SortedMap {
    private final List<String> keys = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    public void put(String key, Object value) {
        keys.add(key);
        values.add(value);
    }

    public Object get(String key) {
        int i = 0;
        for (String k : keys) {
            if (k.equalsIgnoreCase(key)) {
                break;
            }
            i++;
        }

        return values.get(i);
    }

    public boolean containsKey(String key) {
        return keys.contains(key);
    }

    public boolean containsValue(String value) {
        return values.contains(value);
    }
}
