package me.tastycake.json;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@Getter
public class OrderedJSONObject extends JSONObject {
    private final JSONObject jsonObject;
    private final List<String> keys = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    public OrderedJSONObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public JSONObject put(String key, Object value) throws JSONException {
        keys.add(key);
        values.add(value);
        return super.put(key, value);
    }
}
