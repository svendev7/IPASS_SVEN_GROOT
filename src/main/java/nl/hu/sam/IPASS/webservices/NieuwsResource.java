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
    @Produces(MediaType.TEXT_HTML)
    public String getNieuws() {
        Nieuws nieuws = new Nieuws("2023-06-07", "Some information", "Some actions");

        // Set attribute values in the HTML
        String html = "<html>...</html>";
        html = html.replace("<span id=\"acties\"></span>", "<span id=\"acties\">" + nieuws.getActies() + "</span>");
        html = html.replace("<span id=\"informatie\"></span>", "<span id=\"informatie\">" + nieuws.getInformatie() + "</span>");
        html = html.replace("<span id=\"nieuwsdatum\"></span>", "<span id=\"nieuwsdatum\">" + nieuws.getNieuwsDatum() + "</span>");

        return html;
    }
}
