package nl.hu.sam.IPASS.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private static List<User> allUsers = new ArrayList<>();
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        allUsers.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public static List<User> getAllUsers() {
        return allUsers;
    }
    public boolean checkPassword(String passwordToCheck) {
        return passwordToCheck.equals(password);
    }
}









