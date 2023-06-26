package nl.hu.sam.IPASS.webservices;

import nl.hu.sam.IPASS.model.RoosterInvulData;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Path("/rooster")
public class RoosterInvulResource {
    private final String ROOSTER_FILE = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\data\\rooster.json";

    @POST
    @Path("/roosterinvul")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveRooster(RoosterInvulData r) {
//       method to save rooster to json file
        if (r != null && r.getRooster() != null) {
            String selectedUser = r.getUsername();
            saveRoosterToJson(r, selectedUser);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void saveRoosterToJson(RoosterInvulData r, String selectedUser) {
        try {
//            retrieve json file and write new contents
            String jsonContent = Files.readString(Paths.get(ROOSTER_FILE));

            JSONArray jsonArray = new JSONArray(jsonContent);

            // Check if the username already exists in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject roosterJson = jsonArray.getJSONObject(i);
                if (roosterJson.getString("username").equals(selectedUser)) {
                    // Overwrite the existing rooster data for the user
                    roosterJson.put("rooster", r.getRooster());
                    Files.write(Paths.get(ROOSTER_FILE), jsonArray.toString().getBytes());
                    return;
                }
            }

            // Username does not exist, create a new entry
            JSONObject newRoosterJson = new JSONObject();
            newRoosterJson.put("username", selectedUser);
            newRoosterJson.put("rooster", r.getRooster());

            jsonArray.put(newRoosterJson);

            Files.write(Paths.get(ROOSTER_FILE), jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



