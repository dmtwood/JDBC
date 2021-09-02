package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.domain.Leverancier;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeveranciersRepository extends AbstractTuincentrumRepository {

    private static final String LEVERANCIERS_TABLENAME = "leveranciers";


    public List<String> findAllNames() throws SQLException {
        String mySqlQuery = "select naam from " + LEVERANCIERS_TABLENAME;
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(mySqlQuery);
                ResultSet resultSet = statement.executeQuery()
        ) {
            var leveranciersNamen = new ArrayList<String>();
            while (resultSet.next()) {
                leveranciersNamen.add(resultSet.getString("naam"));
            }
            return leveranciersNamen;
        }
    }


    public int nrOfLeveranciers() throws SQLException {

        String mySqlQuery = "select count(*) as nrOfLeveranciers from " + LEVERANCIERS_TABLENAME;
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(mySqlQuery);
                ResultSet resultSet = statement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getInt("nrOfLeveranciers");
        }
    }


    public List<Leverancier> findAll() throws SQLException {

        String mySqlQuery = "select id,naam,adres,postcode,woonplaats,sinds from " + LEVERANCIERS_TABLENAME;

        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(mySqlQuery);
                ResultSet resultSet = statement.executeQuery()
        ) {
            var allLeveranciers = new ArrayList<Leverancier>();

            while (resultSet.next()) {
                allLeveranciers.add(convertSetToLeverancierObject(resultSet));
            }
            return allLeveranciers;
        }
    }


    public List<Leverancier> findByWoonplaats(String woonplaats) throws SQLException {
        String mySqlQuery = "select id, naam, adres, postcode, woonplaats, sinds from " + LEVERANCIERS_TABLENAME
                + " where woonplaats = ?";
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(mySqlQuery)

        ) {
            statement.setString(1, woonplaats);

            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                var leveranciersVanGemeente = new ArrayList<Leverancier>();
                while (resultSet.next()) {
                    leveranciersVanGemeente.add(convertSetToLeverancierObject(resultSet));
                }
                return leveranciersVanGemeente;
            }
        }
    }


    public Optional<Leverancier> findById(long id) throws SQLException {

        String findByIdQuery = "select id, naam, adres, postcode, woonplaats, sinds from "
                + LEVERANCIERS_TABLENAME + " where id = ?";
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(findByIdQuery)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(convertSetToLeverancierObject(resultSet));
            }
            return Optional.empty();
        }
    }


    public List<Leverancier> leveranciersSince(LocalDate sinceDate) throws SQLException {
        var sqlQuery = "select id,naam,adres,postcode, woonplaats, sinds from leveranciers where sinds >= ?";
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)
        ) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement.setDate(1, Date.valueOf(sinceDate));
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                var leveranciersSince = new ArrayList<Leverancier>();
                while (resultSet.next()) leveranciersSince.add(convertSetToLeverancierObject(resultSet));
                connection.commit();
                return leveranciersSince;
            }
        }
    }


    public List<Leverancier> leveranciersBefore2001() throws SQLException {

        var sqlQuery = "select id,naam,adres,postcode, woonplaats, sinds from leveranciers where sinds <= {d '2001-01-01'}";
        return getLeveranciers(sqlQuery);
    }


    // use {fn } to use JDBC DateTime methods >> curdate, curtime(), now(), dayof...(), month(myLocalDate),...
    public List<Leverancier> leveranciersStartedInYear2000() throws SQLException {

        var mySQLQuery = "select id,naam,adres,postcode,woonplaats,sinds from leveranciers where {fn year(sinds)} = 2000";
        return getLeveranciers(mySQLQuery);
    }

    private Leverancier convertSetToLeverancierObject(ResultSet resultSet) throws SQLException {
        return new Leverancier(
                resultSet.getInt("id"),
                resultSet.getString("naam"),
                resultSet.getString("adres"),
                resultSet.getInt("postcode"),
                resultSet.getString("woonplaats"),
                resultSet.getDate("sinds").toLocalDate()
        );
    }

    private List<Leverancier> getLeveranciers(String sqlQuery) throws SQLException {
        try (
                Connection connection = super.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            try (
                    ResultSet resultSet = statement.executeQuery()) {

                var leveranciersSince2001 = new ArrayList<Leverancier>();

                while (resultSet.next()) {
                    leveranciersSince2001.add(convertSetToLeverancierObject(resultSet));
                }

                connection.commit();
                return leveranciersSince2001;
            }
        }
    }

}




























