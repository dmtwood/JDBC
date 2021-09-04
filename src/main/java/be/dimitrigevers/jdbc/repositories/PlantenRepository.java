package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.exceptions.PlantNotFoundException;
import be.dimitrigevers.jdbc.exceptions.PrijsToLowException;
import be.dimitrigevers.jdbc.exceptions.SoortNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    // append sql statement with    'for update'     to lock the record
    // this is not best practice because it always uses 2 sql statement
    public void lowerPriceNoBestPractice(long id, BigDecimal newPrice) throws SQLException {
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
                                PreparedStatement statement1 = connection.prepareStatement(updateQuery)
                        ) {
                            statement1.setLong(1, id);
                            statement1.setBigDecimal(2, newPrice);
                            statement1.executeUpdate();
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

    // best practice, when first statements is successfully, the seconds isn't executed -> lesser workload db
    public void lowerPrice(long id, BigDecimal newPrice) throws SQLException {

        var updateQuery = "update " + PLANTEN_TABLE + " set prijs = ? where id = ? and prijs / 2 < ? ";

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)
        ) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            statement.setBigDecimal(1, newPrice);
            statement.setLong(2, id);
            statement.setBigDecimal(3, newPrice);
            int updateReturn = statement.executeUpdate();
            if (updateReturn == 1) {
                connection.commit();
                return;
            }
            // only processed when update had no effect > filter out wich condition/parameter was fault
            var selectQuery = "select count(*) as aantal from planten where id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(selectQuery)) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                updateStatement.setLong(1, id);
                try (ResultSet resultSet = updateStatement.executeQuery()) {
                    resultSet.next();
                    if (resultSet.getLong("aantal") == 0) {
                        connection.rollback();
                        throw new SoortNotFoundException("Er is geen plant aan " + id + " gelinkt");
                    }
                    throw new PrijsToLowException();
                }
            }

        }
    }

    // best performance to search a set op records -> where .... in ( ..., ..., ...)
    public List<String> plantNamesfromIds(Set<Long> ids) throws SQLException {

        var queryNamesWithIds = new StringBuilder( "select id,naam from planten where id in (" );
        ids.forEach(id -> queryNamesWithIds.append("?,"));
        queryNamesWithIds.setCharAt(queryNamesWithIds.length() - 1, ')' );

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.valueOf(queryNamesWithIds))
            ) {
            int i = 1;
            for (long id : ids) {
                statement.setLong(i, id);
                i++;
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery() ) {
                var plantNamesWithIds = new ArrayList<String>();
                while (resultSet.next() ) {
                    plantNamesWithIds.add(resultSet.getString("naam"));
                }
                connection.commit();
                return plantNamesWithIds;
            }

        }
    };


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




















