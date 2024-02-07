package me.tastycake.utils;

import lombok.Getter;
import me.tastycake.serializer.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
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

    public static SortedMap autoCreate(Serializable serializable) {
        SortedMap sortedMap = new SortedMap();

        Field[] fields = serializable.getClass().getDeclaredFields();

        List<String> types = new ArrayList<>();

        for (Constructor c : serializable.getClass().getConstructors()) {
            if (c.getParameterTypes().length > 0) {
                Collections.addAll(types, c.getParameterTypes().getClass().getSimpleName().toLowerCase());
            }
        }

        for (Field field : fields) {
            field.setAccessible(true);

            if (!types.contains(field.getName().toLowerCase())) continue;

            try {
                sortedMap.put(field.getName(), field.get(serializable));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return sortedMap;
    }
}
