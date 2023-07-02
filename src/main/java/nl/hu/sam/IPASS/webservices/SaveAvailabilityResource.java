package nl.hu.sam.IPASS.webservices;

import nl.hu.sam.IPASS.model.AvailabilityData;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Path("savebeschikbaarheid")
public class SaveAvailabilityResource {
    private static final String AVAILABILITY_JSON_PATH = "/home/site/wwwroot/data/beschikbaarheden.json";
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
}
