package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class AbstractTuincentrumRepository {

    private static final String DB_URL = "jdbc:mysql://localhost/tuincentrum?" +
            "useSSL=false&" +
            "allowPublicKeyRetrieval=true" +
            "&serverTimezone=Europe/Brussels";

    private static final String DB_USERNAME = "cursist";

    private static final String DB_PASSWORD = "cursist";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
