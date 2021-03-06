package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.exceptions.SoortAlreadyExistsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// set transaction level on serializable for max secure but slow as hell -> use rare & with caution !!!

// try select   >>   try resulset  .next() ? commit & throw ExistsException : try insert
public class SoortenRepository extends AbstractTuincentrumRepository {

    static String selectIdFromSoort = "select id from soorten where naam=?";
    static String insertSoort = "insert into soorten(naam) values (?)";

    public void createWithMaxSecurity(String newSoort) throws SQLException {

        String selectIdFromSoort = "select id from soorten where naam=?";
        String insertSoort = "insert into soorten(naam) values (?)";

        try (
                Connection connection = super.getConnection();
                PreparedStatement statementSelect = connection.prepareStatement(selectIdFromSoort)
        ) {
            statementSelect.setString(1, newSoort);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            try (
                    ResultSet resultSet = statementSelect.executeQuery()
            ) {

                if (resultSet.next()) {

                    connection.commit();
                    throw new SoortAlreadyExistsException(newSoort + " is already in system");

                } else {

                    try (
                            PreparedStatement statementInsert = connection.prepareStatement(insertSoort)
                    ) {
                        statementInsert.setString(1, newSoort);
                        statementInsert.executeUpdate();
                        connection.commit();
                        System.out.println("max sec added");
                    }
                }
            }
        }
    }


// try insert & commit      >> try { executeUpdate() }
// catch (SQLEx) { try selstatement     try resultset.executeQuery()        .next() ? throw cust exc }
    // when working with sql auto-numbered id's
    // >> return a long (no longer void) | prepare statement with to manage keys (2nd param) | Create ResultSet with .getGeneratedKeys()
    public long createBestPractice(String newSoort) throws SQLException {

        try (
                Connection connection = super.getConnection();
                PreparedStatement statementInsert = connection.prepareStatement(insertSoort, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statementInsert.setString(1, newSoort);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            try {
                statementInsert.executeUpdate();
                try (
                        ResultSet resultSet = statementInsert.getGeneratedKeys()
                ) {
                    resultSet.next();
                    long returnedId = resultSet.getLong(1);
                    connection.commit();
                    System.out.println("BP added");
                    return returnedId;
                }

            } catch (SQLException ex) {
                try (
                        PreparedStatement statementSelect = connection.prepareStatement(selectIdFromSoort)
                ) {
                    statementSelect.setString(1, newSoort);
                    try (
                            ResultSet resultSet = statementSelect.executeQuery()
                    ) {
                        if (resultSet.next())

                        connection.commit();
                        throw ex;
                    }
                }
            }
        }
    }


    private static final String namesBatchQuery = "insert into soorten(naam) values (?)";

    // use a batch file to bundle multiple calls
    public void create(List<String> names) throws SQLException {

        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(namesBatchQuery)
        ) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (String name : names) {
                statement.setString(1, name);
                // statement.executeUpdate();
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        }
    }


    public List<Long> createAndReadNewIds(List<String> names) throws SQLException {
        var idsCreated = new ArrayList<Long>();
        try( Connection connection = super.getConnection();
        PreparedStatement statement = connection.prepareStatement(namesBatchQuery, Statement.RETURN_GENERATED_KEYS)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            for (String name : names ) {
                statement.setString(1, name);
                statement.addBatch();
            }
            statement.executeBatch();
            try( ResultSet resultSet = statement.getGeneratedKeys()
            ) {
                while (resultSet.next()) {
                    idsCreated.add(resultSet.getLong(1)); //
                }
            }
            connection.commit();
            return idsCreated;
        }
    }
}

