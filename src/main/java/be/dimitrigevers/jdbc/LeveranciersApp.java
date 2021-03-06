package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.domain.Leverancier;
import be.dimitrigevers.jdbc.repositories.LeveranciersRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

public class LeveranciersApp {

    public static void main(String[] args) {

        LeveranciersRepository leveranciersRepo = new LeveranciersRepository();

        try {
            System.out.println("*************************");
            System.out.println("Names of all Leveranciers.");
            System.out.println("*************************");
            for (String levName : leveranciersRepo.findAllNames()) {
                System.out.println(levName);
            }
            System.out.println();
            System.out.println("*************************");
            System.out.println("Total nr of Leveranciers: ");
            System.out.println(leveranciersRepo.nrOfLeveranciers());
            System.out.println("*************************");

            System.out.println();
            System.out.println("*************************");
            System.out.println("Summary of Leveranciers: ");
            for (Leverancier leverancier : leveranciersRepo.findAll()) {
                System.out.println(leverancier);
            }

            System.out.println();
            System.out.println("*************************");
            System.out.println("Summary of Leveranciers using lambda: ");
            leveranciersRepo.findAll().forEach(System.out::println);

            System.out.println();
            System.out.println("*************************");
            System.out.println("Search a leverancier by woonplaats: ");
            Scanner scanner = new Scanner(System.in);
            String in = scanner.nextLine();
            System.out.println(leveranciersRepo.findByWoonplaats(in));


            System.out.println();
            System.out.println("*************************");
            System.out.println("Search a leverancier by id: ");
            long idInput = scanner.nextLong();
            Optional<Leverancier> levId = leveranciersRepo.findById(idInput);
            if (levId.isPresent()) {
                Leverancier thisLev = levId.get();
                System.out.println(thisLev);
            } else {
                System.out.println("not found");
            }

            System.out.println();
            System.out.println("*************************");
            System.out.println("Alle leveranciers van voor 2001: ");
            leveranciersRepo.leveranciersBefore2001().forEach(System.out::println);


            System.out.println();
            System.out.println("*************************");
            System.out.println("Alle leveranciers gestart in 2000: ");
            leveranciersRepo.leveranciersStartedInYear2000().forEach(System.out::println);


            System.out.println();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y") ;
            System.out.println("*************************");
            System.out.println("Zoek alle leveranciers vanaf (dd/mm/jjjj) : ");
            Scanner scanner1 = new Scanner(System.in);
            LocalDate filterDate = LocalDate.parse( scanner1.nextLine(), formatter);
            leveranciersRepo.leveranciersSince(filterDate).forEach(System.out::println);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
        }

    }

}
