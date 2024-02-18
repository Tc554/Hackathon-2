package me.tastycake.hackathon_2.mysql;

import lombok.Getter;
import me.tastycake.hackathon_2.serializer.Serializer;

import java.sql.*;

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

    public void saveToKey(String primaryId, String id, byte[] bytes) {
        try {
            if (keyExists(primaryId, id)) {
                updateKey(primaryId, id, bytes);
            } else {
                insertValue(primaryId, id, bytes);
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

    public boolean keyExists(String primaryId, String id) throws SQLException {
        String sql = "SELECT * FROM user_data WHERE " + primaryId + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            boolean result = statement.executeQuery().next();
            statement.close();
            return result;
        }
    }

    private void updateKey(String primaryId, String id, Object object) throws SQLException {
        String sql = "UPDATE user_data SET data = ? WHERE " + primaryId + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // statement.setString(1, key);
            statement.setObject(1, object);
            // statement.setString(3, primaryId);
            statement.setString(2, id);

            System.out.println(sql);

            statement.executeUpdate();
        }
    }

    private void insertValue(String primaryId, String id, byte[] bytes) throws Exception {
        Object d = Serializer.byteDeserialize(bytes);

        String sql = "INSERT INTO user_data(" + primaryId + ", data) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setObject(2, bytes);
            statement.executeUpdate();
        }
    }

    public Object getById(String primaryId, String id, String table) {
        String sql = "SELECT * FROM " + table + " WHERE " + primaryId + " = '" + id + "'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return Serializer.byteDeserialize((byte[]) resultSet.getObject("data"));
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            handleError(e);
        }

        return null;
    }

    public void handleError(Exception e) {
        e.printStackTrace();
    }
}
