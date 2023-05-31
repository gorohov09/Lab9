import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class DbConnector {
    private static final String _dbURL = "jdbc:sqlserver://localhost\\SQLEXPRESS;database=Lab9;user=guest;password=777;encrypt=true;trustServerCertificate=true";

    public static Connection connectToDb() throws SQLException {
        return DriverManager.getConnection(_dbURL);
    }
}
