package dk.easv.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

// This class makes connection to the database
public class ConnectionManager {
    private final SQLServerDataSource ds;

    public ConnectionManager()
    {
        ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.34");
        ds.setDatabaseName("MyTunesDB123");
        ds.setPortNumber(1433);
        ds.setUser("CS2025b_e_18");
        ds.setPassword("CS2025bE18#23");
        ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }
}
