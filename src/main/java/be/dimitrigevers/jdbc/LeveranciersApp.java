package be.dimitrigevers.jdbc;

import be.dimitrigevers.jdbc.domain.Leverancier;
import be.dimitrigevers.jdbc.repositories.LeveranciersRepository;

import java.sql.SQLException;

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

        } catch (SQLException sqlException) {
            sqlException.printStackTrace(System.err);
        }


    }
}