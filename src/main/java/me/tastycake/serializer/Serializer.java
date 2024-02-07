package me.tastycake.serializer;

import me.tastycake.Main;
import me.tastycake.json.OrderedJSONObject;
import me.tastycake.utils.SortedMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.*;

public class Serializer {
    private static List<Serializable> serializables = new ArrayList<>();

    private static File mainConfig;
    private static File serializablesConfig;

    public static void newSerializable(Serializable serializable) {
        Serializable.Data data = new Serializable.Data();
        data.setId(UUID.randomUUID().toString());
        serializable.data.getData().put(serializable, data);
        serializables.add(serializable);
    }

    public static void setup() throws Exception {
        mainConfig = new File("main.ser");
        serializablesConfig = new File("serializables.ser");

        if (!mainConfig.exists()) mainConfig.createNewFile();
        if (!serializablesConfig.exists()) serializablesConfig.createNewFile();
    }

    public static JSONObject serialize(String primaryId, String key, Serializable serializable) {
        if (serializable.data.getData().get(serializable) == null) {
            Serializable.Data data = new Serializable.Data();
            data.setId(UUID.randomUUID().toString());
            serializable.data.getData().put(serializable, data);
        }
        String id = serializable.data.getData().get(serializable).getId();

        OrderedJSONObject data = new OrderedJSONObject(new JSONObject());
        JSONArray params = new JSONArray();

        Constructor<?>[] constructors = serializable.getClass().getConstructors();

        List<String> insertedNames = new ArrayList<>();

        for (Constructor constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();

            for (Parameter parameter : parameters) {
                if (insertedNames.contains(parameter.getName())) continue;
                insertedNames.add(parameter.getName());
                params.put(parameter.getType().getName());
            }
        }

        data.put("params", params);

        SortedMap result = serializable.serialize();

        for (String s : result.getKeys()) {
            if (result.get(s) instanceof List<?>) {
                JSONArray array = new JSONArray();
                for (Object object : (List<?>) result.get(s)) {
                    array.put(object);
                }
                data.put(s, array);
                continue;
            }

            data.put(s, result.get(s));
        }

        JSONObject main = new JSONObject();
        main.put(serializable.getClass().getName() + "=" + id, data);
        return main;
    }

    public static JSONObject serializeAndSave(String primaryId, String key, Serializable serializable) {
        JSONObject main = serialize(primaryId, key, serializable);
        Main.mySQL.saveToKey(primaryId, key, main, serializable);
        return main;
    }

    public static Serializable deserialize(JSONObject json) throws Exception {
        JSONObject value = json;

        int index = 0;

        Serializable serializable;

        List<Object> values = new ArrayList<>();
        Constructor constructor = null;
        String id = "";

        String next = value.keys().next();

        OrderedJSONObject data = (OrderedJSONObject) value.getJSONObject(next);

        StringTokenizer tokenizer = new StringTokenizer(next, "=");

        JSONArray params = value.getJSONObject(next).getJSONArray("params");

        String clazz = tokenizer.nextToken();
        id = tokenizer.nextToken();

        List<Class<?>> classes = new ArrayList<>();

        if (Class.forName(clazz).newInstance() instanceof Serializable) {
            List<Object> types = params.toList();

            for (Object type : types) {
                if (type instanceof String) {
                    s:
                    switch ((String) type) {
                        case "int":
                            classes.add(int.class);
                            break s;
                        case "char":
                            classes.add(char.class);
                            break s;
                        default:
                            classes.add(Class.forName((String) type));
                            break s;
                    }
                }
            }

            Class<?>[] t = classes.toArray(new Class<?>[classes.size()]);

            constructor = Class.forName(clazz).getConstructor(t);
        }

        for (String s : data.getKeys()) {
            if (s.equalsIgnoreCase("params")) continue;

            Object obj = data.get(s);

            if (obj instanceof JSONArray) {
                values.add(((JSONArray) obj).toList());
                continue;
            }

            values.add(obj);
        }

        if (constructor == null) return null;

        serializable = (Serializable) constructor.newInstance(values.toArray());
        Serializable.Data d = new Serializable.Data();
        d.setId(id);
        serializable.data.getData().put(serializable, d);

        return serializable;
    }

    public interface SerializerResult {
        void result(List<Serializable> r);
        void result(Serializable r);
    }
}
