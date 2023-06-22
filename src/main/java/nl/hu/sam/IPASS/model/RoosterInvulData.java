package nl.hu.sam.IPASS.model;


import org.eclipse.jetty.util.StringUtil;

public class RoosterInvulData {
    private String rooster;
    private String username;
    public RoosterInvulData() {
        // Default constructor
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRooster() {
        return rooster;
    }
    public void setRooster(String rooster) {
        this.rooster = rooster;
    }

}
