package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.exceptions.PlantNotFoundException;
import be.dimitrigevers.jdbc.exceptions.PrijsToLowException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


    public int raisePriceByName(String plantName, int percentage) throws SQLException {

        String mySqlQuery = "update " + PLANTEN_TABLE + " set prijs = prijs + prijs/100* ? where naam = ?";

        try (
                Connection connection = super.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(mySqlQuery)
        ) {
            preparedStatement.setInt(1, percentage);
            preparedStatement.setString(2, plantName);

            // returns the nr of rows affected by the update
            return preparedStatement.executeUpdate();
        }
    }


    // call a procedure stored on sql-db  -> { call myProc(?) }
    public List<String> plantNamesContainingSubstring(String mySubstring) throws SQLException {

        String callMyProcedure = "{call PlantsWithSubstring(?)}";
        try (
                Connection connection = super.getConnection();
                CallableStatement statement = connection.prepareCall(callMyProcedure)
        ) {
            statement.setString(1, '%' + mySubstring + '%');
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                var namesContainingMySubstring = new ArrayList<String>();
                while (resultSet.next()) {
                    namesContainingMySubstring.add(resultSet.getString("naam"));
                }
                return namesContainingMySubstring;
            }
        }
    }


    // bundle statements into one transaction -> autocommit false, execute all, commit
    public void raisePricesOverAndUnder100() throws SQLException {
        String over100 = "update planten set prijs = prijs * 1.1 where prijs > 100";
        String under100 = "update planten set prijs = prijs * 1.05 where prijs <= 100";

        try (
                Connection connection = super.getConnection();
                PreparedStatement over100Statement = connection.prepareStatement(over100);
                PreparedStatement under100Statement = connection.prepareStatement(under100)
        ) {
            connection.setAutoCommit(false);
            over100Statement.executeUpdate();
            under100Statement.executeUpdate();
            connection.commit();
        }
    }


    public void lowerPrice(long id, BigDecimal newPrice) throws SQLException {
        String sqlQuery = "select prijs from planten where id = ? for update";
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)
        ) {
            statement.setLong(1, id);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                if (resultSet.next()) {
                    BigDecimal priceInDB = resultSet.getBigDecimal("prijs");

                    if (priceInDB.divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP).compareTo(newPrice) < 1) {
                        String updateQuery = "update planten set prijs = ? where id = ?";
                        try (
                                PreparedStatement statement1 = connection.prepareStatement(updateQuery);
                        ) {
                            statement1.setLong(1, id);
                            statement1.setBigDecimal(2, newPrice);
                            statement1.executeQuery();
                            connection.commit();
                            return;
                        }
                    }
                    connection.rollback();
                    throw new PrijsToLowException();
                }
                connection.rollback();
                throw new PlantNotFoundException();
            }
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




















