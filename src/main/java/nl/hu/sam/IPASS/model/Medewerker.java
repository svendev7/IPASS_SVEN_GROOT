package nl.hu.sam.IPASS.model;

public class Medewerker {
        private String naam;
        private String beschikbaarheid;
        private String kwalificaties;
        private User user;
        public Medewerker(String naam, String beschikbaarheid, String kwalificaties) {
            this.naam = naam;
            this.beschikbaarheid = beschikbaarheid;
            this.kwalificaties = kwalificaties;
        }
        public String getNaam() {
            return naam;
        }
        public void setNaam(String nm) {
            this.naam = nm;
        }
        public String getBeschikbaarheid() {
            return beschikbaarheid;
        }
        public void setBeschikbaarheid(String bescheid) {
            this.beschikbaarheid = bescheid;
        }
        public String getKwalificaties() {
            return kwalificaties;
        }
        public void setKwalificaties(String kw) {
            this.kwalificaties = kw;
        }
}
