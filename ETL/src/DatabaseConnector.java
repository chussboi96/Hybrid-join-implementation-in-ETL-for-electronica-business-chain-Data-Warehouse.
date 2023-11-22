import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String url = "jdbc:mysql://localhost:3306/transaction";
    private static final String masterDataUrl = "jdbc:mysql://localhost:3306/master_data";
    private static final String dwhUrl = "jdbc:mysql://localhost:3306/ELECTRONICA-DW";
    private static final String user = "root";
    private static final String password = "chussboi96";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static Connection getMDConnection() throws SQLException {
        return DriverManager.getConnection(masterDataUrl, user, password);
    }

    public static Connection getDWHConnection() throws SQLException {
        return DriverManager.getConnection(dwhUrl, user, password);
    }

}