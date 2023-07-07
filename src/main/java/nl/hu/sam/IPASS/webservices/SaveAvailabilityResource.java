package nl.hu.sam.IPASS.webservices;

import nl.hu.sam.IPASS.model.AvailabilityData;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("savebeschikbaarheid")
public class SaveAvailabilityResource {
    private static final String AVAILABILITY_JSON_PATH = "/home/site/wwwroot/data/beschikbaarheden.json";
    @GET
    @Path("/availabilitydata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailabilityData() {
        try {
            String jsonData = Files.readString(Paths.get(AVAILABILITY_JSON_PATH));
            return Response.ok(jsonData).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/saveavailability")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveAvailability(AvailabilityData availabilityData) {
        try {
            String jsonData = Files.readString(Paths.get(AVAILABILITY_JSON_PATH));

            JSONArray jsonArray = new JSONArray(jsonData);
            boolean usernameExists = false;

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
}