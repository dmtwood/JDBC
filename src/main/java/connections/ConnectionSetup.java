package connections;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSetup {

    private static final String DB_URL = "jdbc:mysql://localhost/tuincentrum?" +
            "useSSL=false&" +
            "allowPublicKeyRetrieval=true" +
            "&serverTimezone=Europe/Brussels";

    private static final String DB_USERNAME = "cursist";

    private static final String DB_PASSWORD = "cursist";

    public static void main(String[] args) {

        try (
                Connection myConnection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)
        ) {
            System.out.println("De connectie is geslaagd.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
