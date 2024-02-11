package me.tastycake.mysql;

import com.mysql.cj.MysqlType;
import lombok.Getter;
import me.tastycake.json.OrderedJSONObject;
import me.tastycake.serializer.Serializable;
import me.tastycake.serializer.Serializer;
import me.tastycake.user.imple.Pupil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

@Getter
public class MySQL {
    private String url = "jdbc:mysql://localhost/hackathon-2";
    private String username = "root";
    private String password = "";

    private Connection connection;


    public MySQL() throws Exception {
        connection = getConnection();
    }

    public Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    public void saveToKey(String primaryId, String id, String key, JSONObject jsonObject, Serializable... serializables) {
        try {
            if (keyExists(primaryId, id)) {
                Field[] fields = serializables.getClass().getDeclaredFields();

                if (key.equalsIgnoreCase("")) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        updateKey(primaryId, id, name, jsonObject);
                    }
                } else {
                    updateKey(primaryId, id, key, jsonObject);
                }
            } else if (serializables.length > 0) {
                insertKey(primaryId, id, serializables[0]);
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

    private boolean keyExists(String primaryId, String id) throws SQLException {
        String sql = "SELECT * FROM user_data WHERE " + primaryId + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            boolean result = statement.executeQuery().next();
            statement.close();
            return result;
        }
    }

    private void updateKey(String primaryId, String id, String key, JSONObject jsonObject) throws SQLException {
        String sql = "UPDATE user_data SET ? = ? WHERE ? = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            statement.setString(2, jsonObject.toString());
            statement.setString(3, primaryId);
            statement.setString(4, id);

            statement.executeUpdate();
        }
    }

    private void insertKey(String primaryId, String id, Serializable object) throws SQLException {
        Class<? extends Serializable> clazz = object.getClass();

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();

        List<String> paramsNames = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            for (Class<?> paramType : constructor.getParameterTypes()) {
                paramsNames.add(paramType.getSimpleName().toLowerCase());
            }
        }

        try {
//            Constructor constructor = Class.forName(clazz.getName()).getConstructor(params.toArray(new Class<?>[params.size()]));
//
//            Serializable newInstance = (Serializable) constructor.newInstance();

            List<Object> values = new ArrayList<>();

            for (Field field : fields) {
                fieldNames.add(field.getName());
                // if (!paramsNames.contains(field.getName().toLowerCase())) System.out.println(field.getName());
                field.setAccessible(true);

                try {
                    values.add(field.get(object));
                } catch (IllegalAccessException e) {
                    handleError(e);
                }
            }

            insertValue(primaryId, id, values.toArray(), fieldNames.toArray(new String[fieldNames.size()]));
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void insertValue(String primaryId, String id, Object[] values, String... fields) throws Exception {
        StringBuilder sql = new StringBuilder("INSERT INTO user_data(id, ");

        int i = 0;
        for (String field : fields) {
            if (i + 1 >= fields.length) {
                sql.append(field);
                break;
            }

            sql.append(field).append(", ");

            i++;
        }

        sql.append(") VALUES (?, ");

        int idIndex = 0;

        for (String field : fields) {
            if (field.equalsIgnoreCase(id)) break;

            idIndex++;
        }

        i = 0;
        for (Object value : values) {
            if (!(value instanceof Integer)) sql.append("'");

            if (value instanceof List<?>) {
                JSONObject main = new JSONObject();

                // JSONArray array = new JSONArray((List<?>) value);

                // List<Object> l = array.toList();

                JSONArray n = new JSONArray();

                String d = UUID.randomUUID().toString();

                String className = "";

                for (Object o : (List<?>) value) {
                    if (o instanceof Serializable) {
                        n.put(Serializer.serialize((Serializable) o));
                        className = ArrayList.class.getName();
                    } else {
                        n.put(o + "");
                    }
                }

                if (className.equalsIgnoreCase("")) className = value.getClass().getName();
                main.put(className + "=" + d, n);

                // System.out.println(main);

                sql.append(main);
            }
            else if (value instanceof Serializable) {
                JSONObject jsonObject;
                sql.append(jsonObject = Serializer.serialize((Serializable) value));
            } else {
                sql.append(value + "");
            }

            if (!(value instanceof Integer)) sql.append("'");

            if (i + 1 < values.length) {
                sql.append(", ");
            }

            i++;
        }

        sql.append(")");

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.executeUpdate();
        }
    }

    public Serializable getByMail(String mail) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user_data WHERE mail = '" + mail + "'");

            List<ResultObject> values = new ArrayList<>();

            String id = "";

            int i = 0;
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnType = metaData.getColumnType(i);
                String columnName = metaData.getColumnName(i);

                if (columnName.equalsIgnoreCase("id")) {
                    id = resultSet.getString(i);
                    i++;
                    continue;
                }

                switch (metaData.getColumnType(i)) {
                    case Types.INTEGER:
                        values.add(new ResultObject(resultSet.getInt(i), columnName, i, columnType, false, false));
                        break;
                    case Types.VARCHAR: // TINY
                        int columnSize = metaData.getColumnDisplaySize(i);

                        if (columnSize <= 255) {
                            id = resultSet.getString(i); // Tiny
                        } else if (columnSize <= 65535) {
                            values.add(new ResultObject(resultSet.getString(i), columnName, i, columnType, false, false)); // Normal
                        } else if (columnSize <= 16777215) {
                            values.add(new ResultObject(resultSet.getString(i), columnName, i, columnType, true, false)); // Medium
                        } else {
                            values.add(new ResultObject(resultSet.getString(i), columnName, i, columnType, true, true)); // Large
                        }
                        break;
                }

                i++;
            }

            Map<Object, ResultObject> result = new HashMap<>();

            JSONObject main = new JSONObject();

            JSONObject jsonObject = new JSONObject();

            for (ResultObject v : values) {
                boolean isSerializable = v.isSerializable();
                boolean isList = v.isList();



                if (isList) {
                    List<Serializable> l = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray((String) v.getValue());
                    jsonArray.forEach((o) -> {
                        try {
                            Serializable serializable = Serializer.deserialize(new JSONObject((String) o));
                            l.add(serializable);
                        } catch (Exception e) {
                            handleError(e);
                        }
                    });

                    result.put(l, v);

                    continue;
                }

                if (isSerializable) {
                    Serializable serializable = Serializer.deserialize(new JSONObject((String) v.getValue()));
                    result.put(serializable, v);
                    continue;
                }

                result.put(v.getValue(), v);
            }

            for (Object o : result.keySet()) {
                if (o instanceof List<?>) {
                    JSONArray jsonArray = new JSONArray((List<?>) o);

                    jsonObject.put(result.get(o).getColumnName(), jsonArray);
                    continue;
                }

                jsonObject.put(result.get(o).getColumnName(), o);
            }

            main.put(Pupil.class.getName() + "=" + id, jsonObject);

            statement.close();
            resultSet.close();

            return Serializer.deserialize(main);
        } catch (Exception e) {
            handleError(e);
        }

        return null;
    }

    public JSONObject getJSON(String primary, String primaryId, Class<?> c) {
        String sql = "SELECT * FROM user_data WHERE " + primary + " = '" + primaryId + "'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            OrderedJSONObject main = new OrderedJSONObject(new JSONObject());

            OrderedJSONObject data = new OrderedJSONObject(new JSONObject());

            Field[] fields = c.getDeclaredFields();

            List<String> params = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);

                params.add(field.getType().getName());
            }

            data.put("params", new JSONArray(params));

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String id = "";

            if (resultSet.next()) {
                for (int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                    Object obj = resultSet.getObject(i);

                    if (resultSetMetaData.getColumnName(i).equalsIgnoreCase("id")) {
                        id = resultSet.getString("id");

                        i++;
                        continue;
                    } else {
                        if (id.equalsIgnoreCase("")) {
                            if (resultSet.getString("id") != null) id = resultSet.getString("id");
                        }
                    }

                    try {
                        int parsed = Integer.parseInt(obj + "");
                        data.put(resultSetMetaData.getColumnName(i), parsed);

                        i++;
                        continue;
                    } catch (Exception ignore) {
                    }

                    data.put(resultSetMetaData.getColumnName(i), obj);
                }
            }

            statement.close();
            resultSet.close();

            main.put(c.getName() + "=" + id, data);

            return main;
        } catch (Exception e) {
            handleError(e);
        }

        return null;
    }

    public JSONObject getJSONByKey(String primary, String primaryId, String key) {
        String sql = "SELECT " + key + " FROM user_data WHERE " + primary + " = " + primaryId;

        try {
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(sql);

            statement.close();

           if (resultSet.next()) {
               return new JSONObject(resultSet.getString(0));
           }
        } catch (Exception e) {
            handleError(e);
        }

        return null;
    }

    public void handleError(Exception e) {
        e.printStackTrace();
    }
}
