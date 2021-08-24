package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.domain.Leverancier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
