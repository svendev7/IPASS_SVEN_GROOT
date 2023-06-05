package nl.hu.sam.IPASS.model;

public class TeamLeider {
    private String naam;
    private User user;
    public TeamLeider(String naam) {
        this.naam = naam;
    }
    public String getNaam() {
        return naam;
    }
    public void setNaam(String nm) {
        this.naam = nm;
    }
}
