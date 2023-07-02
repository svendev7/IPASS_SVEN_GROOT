package nl.hu.sam.IPASS.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.hu.sam.IPASS.model.VrijAanvraag;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Path("/vrij")
public class VrijAanvraagResource {
    private final String VRIJAANVRAAG_FILE = "/home/site/wwwroot/data/vrijaanvraag.json";

    @POST
    @Path("/vrijaanvraag")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveVrijaanvraag(VrijAanvraag v) {
//        method to write vrijaanvraag to json file
        if (v != null && v.getvrijaanvraag() != null) {
            String selectedUser = v.getUsername();
            saveVrijaanvraagToJson(v, selectedUser);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void saveVrijaanvraagToJson(VrijAanvraag v, String selectedUser) {
//        method to write new contents into json file
        try {
            String jsonContent = Files.readString(Paths.get(VRIJAANVRAAG_FILE));
            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject vrijaanvraagJson = jsonArray.getJSONObject(i);
                if (vrijaanvraagJson.getString("username").equals(selectedUser)) {
                    vrijaanvraagJson.put("vrijaanvraag", v.getvrijaanvraag());
                    Files.write(Paths.get(VRIJAANVRAAG_FILE), jsonArray.toString().getBytes());
                    return;
                }
            }


            JSONObject newVrijAanvraagJson = new JSONObject();
            newVrijAanvraagJson.put("username", selectedUser);
            newVrijAanvraagJson.put("vrijaanvraag", v.getvrijaanvraag());

            jsonArray.put(newVrijAanvraagJson);

            Files.write(Paths.get(VRIJAANVRAAG_FILE), jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}