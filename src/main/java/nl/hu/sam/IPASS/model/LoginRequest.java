package nl.hu.sam.IPASS.model;

public class LoginRequest {
    public String username;
    public String password;
    public String teamleiderPassword;
    public boolean teamleider = false;

    public String getUsername() {
        return username;
    }
}
