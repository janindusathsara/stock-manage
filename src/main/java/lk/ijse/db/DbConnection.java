package lk.ijse.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private static DbConnection dbConnection;
    private static Connection connection;

    private static final String url = "jdbc:mysql://localhost:3306/kade";
    private static Properties prop = new Properties();

    static {
        prop.setProperty("user","root");
        prop.setProperty("password","1234");
    }

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(url,prop);
    }

    public static DbConnection getInstance() throws SQLException {
        return (dbConnection == null) ? dbConnection = new DbConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
