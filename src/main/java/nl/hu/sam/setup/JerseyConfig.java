package nl.hu.sam.setup;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("sam")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("nl.hu.sam.model.webservices");
    }
}

