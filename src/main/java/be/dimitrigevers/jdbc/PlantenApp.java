package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.exceptions.PrijsToLowException;
import be.dimitrigevers.jdbc.exceptions.SoortNotFoundException;
import be.dimitrigevers.jdbc.repositories.PlantenRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class PlantenApp {
    public static void main(String[] args) throws SQLException {

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

        System.out.println();


        System.out.println("*****************************************");
        System.out.println("Wijzig de prijs van en plant. Geef eerst het id in en daarna de nieuwe prijs.");
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Welk id? ");
        var idInput = scanner.nextLong();
        System.out.println("Nieuwe prijs?");
        var newPrice = scanner1.nextBigDecimal();
        try {
            plantenRepository.lowerPrice(idInput, newPrice);
            System.out.println("Prijs is aangepast");
        } catch (SoortNotFoundException exception) {
            System.out.println("Er is geen plant gekoppeld aan het ingegeven id");
        } catch (PrijsToLowException exception) {
            System.out.println("De nieuwe prijs moet minstens de helft zijn van de bestaande prijs");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        System.out.println("****************************");
        System.out.println("Geef een id in om op te zoeken. 0 om te stoppen");
        Set<Long> ids = new HashSet<>();
        Scanner scanner2 = new Scanner(System.in);
        long idsInput = scanner.nextLong();
        while (idsInput != 0) {
            ids.add(idsInput);
            idsInput = scanner.nextLong();
        }

        // short hand
        plantenRepository.plantNamesfromIds(ids).forEach(plant -> System.out.println(plant) );

        // long hand
        try {
            for (String naam : plantenRepository.plantNamesfromIds(ids)) {
                System.out.println(naam);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

    }


}




















