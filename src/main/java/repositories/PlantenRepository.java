package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlantenRepository extends AbstractTuincentrumRepository {

    private static final String PLANTEN_TABLE = "planten";

    public int raisePrices(int percentage) throws SQLException {
        String mySQLQuery = "update " + PLANTEN_TABLE + " set prijs = prijs + prijs / 100 * " + percentage;

        try (
                Connection connection = super.getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(mySQLQuery)

        ) {
                return preparedStatement.executeUpdate();

        }
    }

}
