package nl.hu.sam.IPASS.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String role;
    private static List<User> allUsers = new ArrayList<>();

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        allUsers.add(this);
    }
    public String getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }
    public static List<User> getAllUsers() {
        return allUsers;
    }
    public boolean checkPassword(String passwordToCheck) {
        return passwordToCheck.equals(password);
    }

}