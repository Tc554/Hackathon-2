package me.tastycake.serializer;

import me.tastycake.Main;
import me.tastycake.json.OrderedJSONObject;
import me.tastycake.user.imple.Pupil;
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

    public static JSONObject serialize(Serializable serializable) {
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

            if (result.get(s) instanceof Serializable se) {
                SortedMap serialized;

                if ((serialized = se.serialize()).containsKey("table")) {
                    String sql = "SELECT * FROM table WHERE " + serialized.get("primaryId") + " = '" + serialized.get("id") + "'";
                    data.put(s, sql);
                    continue;
                }

                data.put(s, serialize(se));
                continue;
            }

            data.put(s, result.get(s));
        }

        JSONObject main = new JSONObject();
        main.put(serializable.getClass().getName() + "=" + id, data);
        return main;
    }

    public static JSONObject serializeAndSave(String primaryId, String id, String key, Serializable serializable) {
        JSONObject main = serialize(serializable);
        Main.mySQL.saveToKey(primaryId, id, key, main, serializable);
        return main;
    }

    public static Serializable deserialize(Serializable serializable) throws Exception {
        return deserialize("id", serializable.data.getData().get(serializable).getId(), serializable.getClass());
    }

    public static Serializable deserialize(String primary, String primaryId, Class<?> clazz) throws Exception {
        JSONObject jsonObject = Main.mySQL.getJSON(primary, primaryId, clazz);

        return deserialize(jsonObject);
    }

    public static Serializable deserialize(JSONObject json) throws Exception {
        String next = "";

        if (json.keys().hasNext()) {
            next = json.keys().next();
        } else {
            return null;
        }

        // OrderedJSONObject data = new OrderedJSONObject(json.getJSONObject(next));

        OrderedJSONObject data = (OrderedJSONObject) json.getJSONObject(next);

        StringTokenizer tokenizer = new StringTokenizer(next, "=");

        String className = "";
        String id = "";

        try {
            className = tokenizer.nextToken();
            id = tokenizer.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        JSONArray params = data.getJSONArray("params");

        boolean found = false;
        for (Class<?> i : Class.forName(className).getInterfaces()) {
            if (i.getName().equalsIgnoreCase(Serializable.class.getName())) {
                found = true;
                break;
            }
        }

        List<Class<?>> classes = new ArrayList<>();

        Constructor constructor;

        if (found) {
            params.forEach((param) -> {
                if (param instanceof String) {
                    switch ((String) param) {
                        case "int":
                            classes.add(int.class);
                            break;
                        case "char":
                            classes.add(char.class);
                            break;
                        default:
                            try {
                                classes.add(Class.forName(((String) param)));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                break;
                            }
                            break;
                    }
                } else if (param instanceof Integer) {
                    classes.add(int.class);
                }
            });
        }

        constructor = Class.forName(className).getConstructor(classes.toArray(new Class<?>[classes.size()]));

        List<Object> values = new ArrayList<>();

        for (String key : data.getKeys()) {
            System.out.println("Key: " + key);
            if (key.equalsIgnoreCase("params")) continue;

            try {
                JSONArray value = data.getJSONArray(key);
                values.add(value.toList());
                continue;
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

            try {
                JSONObject value = data.getJSONObject(key);
                Serializable deserialized = Serializer.deserialize(value);
                values.add(deserialized);
                continue;
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

            values.add(data.get(key));
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i) + ": " + values.get(i));
        }

        Serializable serializable = (Serializable) constructor.newInstance(values.toArray());
        Serializable.Data d = new Serializable.Data();
        d.setId(id);
        serializable.data.getData().put(serializable, d);

        return serializable;
    }

    /* public static Serializable deserialize(JSONObject json) throws Exception {
        JSONObject value = new JSONObject(json.toString());

        Serializable serializable;

        List<Object> values = new ArrayList<>();
        Constructor constructor = null;
        String id = "";

        if (!value.keys().hasNext()) {
            return null;
        }

        String next = "";

        try {
            next = value.keys().next();
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return null;
        }

        OrderedJSONObject data = null;

        try {
            data = (OrderedJSONObject) value.getJSONObject(next);
        } catch (Exception e) {
            try {
                data = new OrderedJSONObject(value.getJSONObject(next));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        StringTokenizer tokenizer = new StringTokenizer(next, "=");

        JSONArray params = data.getJSONArray("params");

        String clazz = tokenizer.nextToken();
        id = tokenizer.nextToken();

        List<Class<?>> classes = new ArrayList<>();

        Class<?> asClass = Class.forName(clazz);

        boolean found = false;
        for (Class<?> i : asClass.getInterfaces()) {
            if (i.getName().equalsIgnoreCase(Serializable.class.getName())) {
                found = true;
                break;
            }
        }

        if (found) {
            List<Object> types = params.toList();

            for (Object type : types) {
                if (type instanceof String) {
                    switch ((String) type) {
                        case "int":
                            classes.add(int.class);
                            break;
                        case "char":
                            classes.add(char.class);
                            break;
                        default:
                            classes.add(Class.forName(((String) type)));
                            break;
                    }
                } else if (type instanceof Integer) {
                    classes.add(int.class);
                }
            }

            Class<?>[] t = classes.toArray(new Class<?>[classes.size()]);

            constructor = Class.forName(clazz).getConstructor(t);
        }

        for (String s : data.getKeys()) {
            if (s.equalsIgnoreCase("params")) continue;

            Object obj = data.get(s);

            try {
                OrderedJSONObject orderedJSONObject = new OrderedJSONObject(new JSONObject(obj + ""));
                values.add(orderedJSONObject);
                continue;
            } catch (Exception ignore) {

            }

            values.add(obj);
        }


        if (constructor == null) return null;

        List<Object> deserialized = new ArrayList<>();

        for (Object v : values) {
            if (v instanceof Integer) {
                deserialized.add(v);
                continue;
            }

            if (v instanceof JSONArray) {
                List<Object> toList = ((JSONArray) v).toList();

                List<Object> fixed = new ArrayList<>();

                for (Object object : toList) {
                    if (object instanceof OrderedJSONObject) {
                        fixed.add(Serializer.deserialize((OrderedJSONObject) object));
                    } else {
                        fixed.add(object);
                    }
                }

                deserialized.add(fixed);

                continue;
            }

            if (v instanceof OrderedJSONObject || v instanceof JSONObject) {
                OrderedJSONObject jsonObject = new OrderedJSONObject(new JSONObject(v + ""));

                JSONObject clone = new JSONObject(jsonObject.toString());

                if (clone.keys().hasNext()) {
                    String n = clone.keys().next();

                    try {
                        clone.getJSONObject(n);
                    } catch (Exception e) {
                        deserialized.add(v);
                        continue;
                    }
                }

                Serializable s = Serializer.deserialize(jsonObject);

                deserialized.add(s);
                continue;
            }

            deserialized.add(v);
        }

        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < classes.size(); i++) {
            if (i + 1 >= deserialized.size()) {
                objects.add(null);
            } else {
                objects.add(deserialized.get(i));
            }
        }

        Object[] asArray = objects.toArray(new Object[objects.size()]);

        for (Object o : asArray) {
            System.out.println(o);
        }


        serializable = (Serializable) constructor.newInstance(asArray);
        Serializable.Data d = new Serializable.Data();
        d.setId(id);
        serializable.data.getData().put(serializable, d);

        return serializable;
    } */

    public interface SerializerResult {
        void result(List<Serializable> r);
        void result(Serializable r);
    }
}
