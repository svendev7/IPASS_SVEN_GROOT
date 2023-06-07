package nl.hu.sam.IPASS.webservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.sam.IPASS.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;

class LoginRequest {
    public String username;
    public String password;
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

                String token = Jwts.builder()
                        .setSubject(req.username)
                        .setExpiration(expires.getTime())
                        .signWith(SignatureAlgorithm.HS512, key)
                        .compact();

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
            User user = new User(req.username, req.password);
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
                    return new User(username, password);
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

            // Append the new user to the JSON array
            jsonArray.put(newUserJson);

            // Write the updated JSON array back to the file
            Files.write(Paths.get(USERS_JSON_PATH), jsonArray.toString().getBytes());
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