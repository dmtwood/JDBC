package be.dimitrigevers.jdbc.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Leverancier {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final String naam;
    private final String adres;
    private final int postcode;
    private final String woonplaats;
    private final LocalDate sinds;

    public Leverancier(int id, String naam, String adres, int postcode, String woonplaats, LocalDate sinds) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
        this.sinds = sinds;
    }

    @Override
    public String toString() {
        return "Leverancier " + naam + " met id " + id + ", met standplaats "
                + woonplaats + " levert sinds " + sinds.format(dateTimeFormatter) ;
    }
}
