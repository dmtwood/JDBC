package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.exceptions.SoortAlreadyExistsException;
import be.dimitrigevers.jdbc.repositories.SoortenRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SoortenApp {

    public static void main(String[] args) {

        SoortenRepository soortenRepo = new SoortenRepository();

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("***********************");
//        System.out.println("Geef een soortnaam in. Gebruik maximale veiligheid (vertraagt systeem)");
//        String inPut = scanner.nextLine();
//        while ( inPut.length() > 10) {
//            System.out.println("***********************");
//            System.out.println("Geef een soortnaam in met maximaal 10 letters");
//            inPut = scanner.nextLine();
//        }
//
//        try {
//            soortenRepo.createWithMaxSecurity(inPut);
//            System.out.println("Soort is toegevoegd");
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        } catch (SoortAlreadyExistsException ex) {
//            System.out.println( ex.getMessage() );
//        }
//
//        System.out.println("***********************");
//        System.out.println("***********************");
//
//        System.out.println("***********************");
//        System.out.println("Geef een soortnaam in. Gebruik read committed veiligheidsniveau (good performance");
//        inPut = scanner.nextLine();
//        while ( inPut.length() > 10) {
//            System.out.println("***********************");
//            System.out.println("Geef een soortnaam in met maximaal 10 letters");
//            inPut = scanner.nextLine();
//        }
//
//        try {
//            long newId = soortenRepo.createBestPractice(inPut);
//            System.out.println("Soort is toegevoegd. Het id in SQL is " + newId);
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        } catch (SoortAlreadyExistsException ex) {
//            System.out.println( ex.getMessage() );
//        }
//        System.out.println();


        System.out.println("*********************");
        System.out.println("Geef een nieuwe naam in. Geeft stop in als je wil stoppen.");
        Scanner scanner1 = new Scanner(System.in);
        String nameInput = scanner1.nextLine();
        List<String> names = new ArrayList<>();
        while ( ! "STOP".equalsIgnoreCase(nameInput) ) {
            names.add(nameInput);
            System.out.println("Geef een nieuwe naam in. Geeft stop in als je wil stoppen.");
            nameInput = scanner1.nextLine();
        }
        try {
//            soortenRepo.create(names);
            List<Long> generatedIds = soortenRepo.createAndReadNewIds(names);
            generatedIds.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
