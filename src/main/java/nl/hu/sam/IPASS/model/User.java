package nl.hu.sam.IPASS.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private static List<User> allUsers = new ArrayList<>();
    private boolean teamleider;
    public User(String username, String password, boolean teamleider) {
        this.username = username;
        this.password = password;
        this.teamleider = teamleider;
        allUsers.add(this);
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isTeamleider() {
        return teamleider;
    }
    public void setTeamleider(boolean teamleider) {
        this.teamleider = teamleider;
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