package nl.hu.sam.IPASS.webservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.sam.IPASS.model.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.Calendar;
import java.util.Map;

class LoginRequest {
    public String username, password;
}

@Path("/login")
public class AuthenticationResource {
    public static Key key = MacProvider.generateKey();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest req) {


        for (User user : User.getAllUsers()) {
            if (user.getUsername().equals(req.username)
                    && user.checkPassword(req.password) ) {
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
                }

