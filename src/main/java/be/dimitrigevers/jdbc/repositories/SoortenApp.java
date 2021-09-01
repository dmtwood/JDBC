package be.dimitrigevers.jdbc.repositories;

import be.dimitrigevers.jdbc.exceptions.SoortAlreadyExistsException;

import java.sql.SQLException;
import java.util.Scanner;

public class SoortenApp {

    public static void main(String[] args) {

        SoortenRepository soortenRepo = new SoortenRepository();

        Scanner scanner = new Scanner(System.in);
        System.out.println("***********************");
        System.out.println("Geef een soortnaam in. Gebruik maximale veiligheid (vertraagt systeem)");
        String inPut = scanner.nextLine();
        while ( inPut.length() > 10) {
            System.out.println("***********************");
            System.out.println("Geef een soortnaam in met maximaal 10 letters");
            inPut = scanner.nextLine();
        }

        try {
            soortenRepo.createWithMaxSecurity(inPut);
            System.out.println("Soort is toegevoegd");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (SoortAlreadyExistsException ex) {
            System.out.println( ex.getMessage() );
        }

        System.out.println("***********************");
        System.out.println("***********************");

        System.out.println("***********************");
        System.out.println("Geef een soortnaam in. Gebruik maximale veiligheid (vertraagt systeem)");
        inPut = scanner.nextLine();
        while ( inPut.length() > 10) {
            System.out.println("***********************");
            System.out.println("Geef een soortnaam in met maximaal 10 letters");
            inPut = scanner.nextLine();
        }

        try {
            soortenRepo.createWithMaxSecurity(inPut);
            System.out.println("Soort is toegevoegd");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (SoortAlreadyExistsException ex) {
            System.out.println( ex.getMessage() );
        }

    }
}
