package be.dimitrigevers.jdbc.repositories;

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

//    De interface PreparedStatement stelt een SQL statement voor dat je naar de database stuurt.
//    Elke JDBC driver bevat een class die deze interface implementeert.
//    De Connection method prepareStatement geeft je een PreparedStatement.
//    Je voert een insert, update of delete statement uit met de PreparedStatement method
//    executeUpdate.
//    Je geeft als parameter een String mee met het uit te voeren SQL statement.
//    De method voert dit statement uit en geeft daarna een int terug.
//    Die int bevat bij een
//• insert statement het aantal toegevoegde records.
//            • update statement het aantal gewijzigde records.
//            • delete statement het aantal verwijderde records.

}
