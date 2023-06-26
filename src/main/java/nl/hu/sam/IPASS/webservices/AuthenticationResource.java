package nl.hu.sam.IPASS.webservices;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.*;
import nl.hu.sam.IPASS.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;


    @Path("/auth")
    public class AuthenticationResource {
        public static Key key = MacProvider.generateKey();
        private static final String USERS_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\data\\users.json";
        private static final String CURRENT_USER_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\data\\currentuser.json";
        private static final String AVAILABILITY_JSON_PATH = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\data\\beschikbaarheden.json";

        private void clearCurrentUserFile() {
            try {
//                empty the CURRENTUSER.json file
                Files.write(Paths.get(CURRENT_USER_JSON_PATH), "".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @POST
        @Path("/saveavailability")
        @Consumes(MediaType.APPLICATION_JSON)
//       sla ingevoerde beschikbaarheid op in daarvoor bestemde json file
        public void saveAvailability(AvailabilityData availabilityData) {
            try {

                String jsonData = Files.readString(Paths.get(AVAILABILITY_JSON_PATH));

                JSONArray jsonArray = new JSONArray(jsonData);
                boolean usernameExists = false;
//                als username al bestaat, vervang bestaande informatie met nieuwe informatie anders nieuwe informatie toevoegen
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("username").equals(availabilityData.getUsername())) {

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

                Files.write(Paths.get(AVAILABILITY_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @POST
        @Path("/login")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response login(LoginRequest req) {

//            retrieve all registered usernames and check password existence, then create jwts key and save user to currentuser file then login
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

                    clearCurrentUserFile();
                    saveCurrentUserToJson(user);

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

//            let user login if username does not already exist, make teamleider if they know the password
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
//                retrieve all usernames from users database json file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                JSONArray jsonArray = new JSONArray(jsonContent);

                List<String> usernames = new ArrayList<>();

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
//                get specific user from user data base json file
                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                JSONArray jsonArray = new JSONArray(jsonContent);

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
//            save registered user to json file database
            try {

                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                JSONArray jsonArray = new JSONArray(jsonContent);

                JSONObject newUserJson = new JSONObject();
                newUserJson.put("username", user.getUsername());
                newUserJson.put("password", user.getPassword());
                newUserJson.put("teamleider", user.isTeamleider());

                jsonArray.put(newUserJson);

                Files.write(Paths.get(USERS_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void saveCurrentUserToJson(User user) {
//            save the user that just logged in to the currentuser json file
            try {

                JSONObject userJson = new JSONObject();
                userJson.put("username", user.getUsername());
                userJson.put("password", user.getPassword());
                userJson.put("teamleider", user.isTeamleider());

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(userJson);

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
//            allow user to change password if they have their original password and username
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
//            update the users' password in database
            try {

                String jsonContent = Files.readString(Paths.get(USERS_JSON_PATH));

                JSONArray jsonArray = new JSONArray(jsonContent);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonUsername = jsonObject.getString("username");

                    if (jsonUsername.equals(user.getUsername())) {
                        jsonObject.put("password", user.getPassword());
                        break;
                    }
                }

                Files.write(Paths.get(USERS_JSON_PATH), jsonArray.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
