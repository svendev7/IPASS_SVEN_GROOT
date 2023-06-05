package nl.hu.sam.IPASS.model;

public class Nieuws {
    private String nieuwsDatum;
    private String informatie;
    private String acties;
    public Nieuws(String nieuwsDatum, String informatie, String acties) {
        this.nieuwsDatum = nieuwsDatum;
        this.informatie = informatie;
        this.acties = acties;
    }
    public String getNieuwsDatum() {
        return nieuwsDatum;
    }
    public void setNieuwsDatum(String nD) {
        this.nieuwsDatum = nD;
    }
    public String getInformatie() {
        return informatie;
    }
    public void setInformatie(String info) {
        this.informatie = info;
    }
    public String getActies() {
        return acties;
    }
    public void setActies(String acties) {
        this.acties = acties;
    }
}
