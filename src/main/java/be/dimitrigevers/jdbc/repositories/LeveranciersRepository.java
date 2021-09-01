package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.domain.Leverancier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
