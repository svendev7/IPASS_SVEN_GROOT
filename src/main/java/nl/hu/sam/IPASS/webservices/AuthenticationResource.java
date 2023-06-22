package nl.hu.sam.IPASS.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.sam.IPASS.model.User;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;
class AvailabilityData {
    private String username;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    // Default constructor
    public AvailabilityData() {
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    // Helper method to convert the object to JSON string
    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}

    class LoginRequest {
        public String username;
        public String password;
        public String teamleiderPassword;
        public boolean teamleider = false;
    }

    class ChangePasswordRequest {
        public String username;
        public String currentPassword;
        public String newPassword;
    }

    @Path("/auth")
    public class AuthenticationResource {
        public static Key key = MacProvider.generateKey();
        private static final String USERS_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\users.json";
        private static final String CURRENT_USER_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\currentuser.json";
        private static final String AVAILABILITY_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\beschikbaarheden.json";

        private void clearCurrentUserFile() {
            try {
                Files.write(Paths.get(CURRENT_USER_JSON_PATH), "".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @POST
        @Path("/saveavailability")
        @Consumes(MediaType.APPLICATION_JSON)
        public void saveAvailability(AvailabilityData availabilityData) {
            try {
                // Read the existing availability data
                String jsonData = Files.readString(Paths.get(AVAILABILITY_JSON_PATH));

                // Update the availability data
                JSONArray jsonArray = new JSONArray(jsonData);
                boolean usernameExists = false;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("username").equals(availabilityData.getUsername())) {
                        // If the username exists, update the availability
                        jsonObject.put("monday", availabilityData.getMonday());
                        jsonObject.put("tuesday", availabilityData.getTuesday());
                        jsonObject.put("wednesday", availabilityData.getWednesday());
                        jsonObject.put("thursday", availabilityData.getThursday());
                        jsonObject.put("friday", availabilityData.getFriday());
                        jsonObject.put("saturday", availabilityData.getSaturday());
                        jsonObject.put("sunday", availabilityData.getSunday());
                        usernameExists = true;
                        break;
                    }
                }

                if (!usernameExists) {
                    // If the username does not exist, add the availability to the array
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", availabilityData.getUsername());
                    jsonObject.put("monday", availabilityData.getMonday());
                    jsonObject.put("tuesday", availabilityData.getTuesday());
                    jsonObject.put("wednesday", availabilityData.getWednesday());
                    jsonObject.put("thursday", availabilityData.getThursday());
                    jsonObject.put("friday", availabilityData.getFriday());
                    jsonObject.put("saturday", availabilityData.getSaturday());
                    jsonObject.put("sunday", availabilityData.getSunday());
                    jsonArray.put(jsonObject);
                }

                // Write the updated data to the file
                Files.write(Paths.get(AVAILABILITY_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private boolean validateToken(String token) {
            try {
                // Parse the token and extract the claims
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Perform any additional validation if required

                return true; // Token is valid
            } catch (JwtException e) {
                e.printStackTrace();
                return false; // Token is invalid
            }
        }
        @POST
        @Path("/login")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response login(LoginRequest req) {
            List<String> usernames = getAllUsernamesFromJson();

            if (usernames.contains(req.username)) {
                User user = getUserFromJson(req.username);

                if (user != null && user.checkPassword(req.password)) {
                    Calendar expires = Calendar.getInstance();
                    expires.add(Calendar.HOUR, 1);

                    // Generate a JWT token
                    String token = Jwts.builder()
                            .setSubject(req.username)
                            .setExpiration(expires.getTime())
                            .signWith(SignatureAlgorithm.HS512, key)
                            .compact();

                    clearCurrentUserFile();
                    saveCurrentUserToJson(user);

                    // Return the token as a JSON response
                    return Response.ok(Map.of("token", token)).build();
                }
            }

            return Response.status(406).build();
        }

        @POST
        @Path("/register")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response register(LoginRequest req) {
            List<String> usernames = getAllUsernamesFromJson();

            if (!usernames.contains(req.username)) {
                User user;
                if (req.teamleiderPassword != null && req.teamleiderPassword.equals("teamleider123")) {
                    user = new User(req.username, req.password, true);
                } else {
                    user = new User(req.username, req.password, false);
                }
                saveUserToJson(user);
                return Response.status(200).build();
            }

            return Response.status(400).build();
        }

        private List<String> getAllUsernamesFromJson() {
            try {
                // Read the contents of the JSON file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                // Parse the JSON content
                JSONArray jsonArray = new JSONArray(jsonContent);

                // Create a list to store usernames
                List<String> usernames = new ArrayList<>();

                // Extract usernames from JSON objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    usernames.add(username);
                }

                return usernames;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Collections.emptyList();
        }

        private User getUserFromJson(String username) {
            try {
                // Read the contents of the JSON file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                // Parse the JSON content
                JSONArray jsonArray = new JSONArray(jsonContent);

                // Find the user with the specified username
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonUsername = jsonObject.getString("username");

                    if (jsonUsername.equals(username)) {
                        String password = jsonObject.getString("password");
                        boolean teamleider = jsonObject.getBoolean("teamleider");
                        return new User(username, password, teamleider);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void saveUserToJson(User user) {
            try {
                // Read the contents of the JSON file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                // Parse the JSON content
                JSONArray jsonArray = new JSONArray(jsonContent);

                // Create a JSON object for the new user
                JSONObject newUserJson = new JSONObject();
                newUserJson.put("username", user.getUsername());
                newUserJson.put("password", user.getPassword());
                newUserJson.put("teamleider", user.isTeamleider());

                // Add the new user object to the JSON array
                jsonArray.put(newUserJson);

                // Write the updated JSON array back to the file
                Files.write(Paths.get(USERS_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void saveCurrentUserToJson(User user) {
            try {
                // Create a JSON object for the user
                JSONObject userJson = new JSONObject();
                userJson.put("username", user.getUsername());
                userJson.put("password", user.getPassword());
                userJson.put("teamleider", user.isTeamleider());

                // Create a JSON array and add the user object
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(userJson);

                // Write the JSON array to the file
                Files.write(Paths.get(CURRENT_USER_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @POST
        @Path("/changepassword")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response changePassword(ChangePasswordRequest req) {
            List<String> usernames = getAllUsernamesFromJson();

            if (usernames.contains(req.username)) {
                User user = getUserFromJson(req.username);

                if (user != null && user.checkPassword(req.currentPassword)) {
                    user.setPassword(req.newPassword);
                    updateUserInJson(user);

                    return Response.status(200).build();
                }
            }

            return Response.status(400).build();
        }

        private void updateUserInJson(User user) {
            try {
                // Read the contents of the JSON file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                // Parse the JSON content
                JSONArray jsonArray = new JSONArray(jsonContent);

                // Find the user with the specified username and update the password
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonUsername = jsonObject.getString("username");

                    if (jsonUsername.equals(user.getUsername())) {
                        jsonObject.put("password", user.getPassword());
                        break;
                    }
                }

                // Write the updated JSON array back to the file
                Files.write(Paths.get(USERS_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
