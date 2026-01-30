package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseWriter {

    private static final String URL =
    "jdbc:postgresql://localhost:5432/log_analytics";


    private static final String USER = "postgres";
    private static final String PASSWORD = "";

    public void writeErrorsPerHour(Map<Integer, Long> data){
        String sql = "INSERT INTO errors_per_hour (hour ,error_count)" +
                        "VALUES (?, ?)" +
                        "ON CONFLICT (hour) " +
                        "DO UPDATE SET error_count = EXCLUDED.error_count;";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Map.Entry<Integer, Long> entry : data.entrySet()) {
                stmt.setInt(1, entry.getKey());
                stmt.setLong(2, entry.getValue());
                stmt.executeUpdate();
            }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void writeErrorsPerUser(Map<String, Long> data){
        String sql = "INSERT INTO errors_per_user (username ,error_count)" +
                        "VALUES (?, ?)" +
                        "ON CONFLICT (username) " +
                        "DO UPDATE SET error_count = EXCLUDED.error_count;";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Map.Entry<String, Long> entry : data.entrySet()) {
                stmt.setString(1, entry.getKey());
                stmt.setLong(2, entry.getValue());
                stmt.executeUpdate();
            }
    } catch (SQLException e) {
        e.printStackTrace();

    }
}
    public void writeUserActivity(Map<String, Long> data){
        String sql = "INSERT INTO user_activity (username ,activity_count)" +
                        "VALUES (?, ?)" +
                        "ON CONFLICT (username) " +
                        "DO UPDATE SET activity_count = EXCLUDED.activity_count;";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Map.Entry<String, Long> entry : data.entrySet()) {
                stmt.setString(1, entry.getKey());
                stmt.setLong(2, entry.getValue());
                stmt.executeUpdate();
            }
    } catch (SQLException e) {
        e.printStackTrace();

    }
}
}