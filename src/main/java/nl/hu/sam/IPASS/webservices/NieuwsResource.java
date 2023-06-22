package nl.hu.sam.IPASS.webservices;

import nl.hu.sam.IPASS.model.Nieuws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/nieuws")
public class NieuwsResource {

    @GET
    @Path("/nieuws")
    @Produces(MediaType.APPLICATION_JSON)
    public Nieuws getNieuws() {
        Nieuws n1 = new Nieuws("12-6-2023", "tomaten zijn niet meer goed", "haal alle tomaten uit de winkel asap");
        return n1;
    }
}
