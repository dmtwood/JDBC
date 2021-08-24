package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.repositories.PlantenRepository;

import java.sql.SQLException;

public class PlantenApp {
    public static void main(String[] args) {

        PlantenRepository plantenRepository = new PlantenRepository();

        try {
            System.out.println(plantenRepository.raisePrices(10));
            System.out.println( " prijzen werden verhoogd.");
        } catch (SQLException sqlException) {sqlException.printStackTrace(System.err);}
    }
}
