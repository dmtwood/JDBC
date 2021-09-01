package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.repositories.PlantenRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class PlantenApp {
    public static void main(String[] args) {

        PlantenRepository plantenRepository = new PlantenRepository();
        Scanner scanner = new Scanner(System.in);

        System.out.println("*****************************************");
        System.out.println("Doorzoek de plantnamen. Geef een zoekterm in.");
        String zoekterm = scanner.nextLine();
        try {
            plantenRepository.plantNamesContainingSubstring(zoekterm).stream().forEach(System.out::println);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
        }
        System.out.println();

        System.out.println("*****************************************");
        System.out.println("geef een plantnaam in");
        String plantName = scanner.nextLine();
        System.out.println("Geef een percentage in om de prijs van " + plantName + " te verhogen");
        int percentage = scanner.nextInt();

        try {
            System.out.println(plantenRepository.raisePriceByName(plantName, percentage) == 1 ? "OK" : "Problemo!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
        System.out.println();


        System.out.println("*****************************************");
        System.out.println("raise all prices with 10 percent");
        try {
            System.out.println(plantenRepository.raisePrices(10));
            System.out.println( " prijzen werden verhoogd.");
        } catch (SQLException sqlException) {sqlException.printStackTrace(System.err);}


        System.out.println("*****************************************");
        System.out.println("raise all prices under 100€ with 5% and all others with 10 %");
        try {
            plantenRepository.raisePricesOverAndUnder100();
            System.out.println( " prijzen werden verhoogd.");
        } catch (SQLException sqlException) {sqlException.printStackTrace(System.err);}

    }


}
