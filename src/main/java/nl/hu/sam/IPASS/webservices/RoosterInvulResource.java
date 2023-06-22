package nl.hu.sam.IPASS.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.hu.sam.IPASS.model.RoosterInvulData;
import nl.hu.sam.IPASS.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.JsonObjectBuilder;
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
import java.nio.file.StandardOpenOption;
import java.util.List;

@Path("/rooster")
public class RoosterInvulResource {
    private final String ROOSTER_FILE = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\rooster.json";

    private void writeJsonFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/roosterinvul")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveRooster(RoosterInvulData r) {

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
            String jsonContent = Files.readString(Paths.get(ROOSTER_FILE));

            // Parse the JSON content
            JSONArray jsonArray = new JSONArray(jsonContent);

            // Create a JSON object for the new user
            JSONObject newRoosterJson = new JSONObject();
            newRoosterJson.put("username", selectedUser);
            newRoosterJson.put("rooster", r.getRooster());

            // Add the new user object to the JSON array
            jsonArray.put(newRoosterJson);

            // Write the updated JSON array back to the file
            Files.write(Paths.get(ROOSTER_FILE), jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



