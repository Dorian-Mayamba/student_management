package students;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection databaseLink;
    public static Connection connectDb () {
        String databaseName = "student_management";
        String url = "jdbc:mysql://localhost/" + databaseName;
        String databaseUser = "dorian";
        String databasePassword = "dorian01";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            return databaseLink;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
