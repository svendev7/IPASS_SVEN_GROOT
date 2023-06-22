package nl.hu.sam.IPASS.tests;


import nl.hu.sam.IPASS.model.Nieuws;
import nl.hu.sam.IPASS.model.RoosterInvulData;
import nl.hu.sam.IPASS.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoosterTest {
    private final String ROOSTER_FILE = "C:\\Users\\Groot\\IdeaProjects\\IPASS_SVEN_GROOT2\\src\\main\\webapp\\rooster.json";
    private RoosterInvulData r1;
    @BeforeEach
    public void setup() {
        RoosterInvulData r1 = new RoosterInvulData();
        r1.setUsername("username123");
        r1.setRooster("rooster: {\"Monday\":\"1\",\"Tuesday\":\"2\",\"Wednesday\":\"3\",\"Thursday\":\"4\",\"Friday\":\"5\",\"Saturday\":\"6\",\"Sunday\":\"7\"}");
    }

    @Test
    public void SeeIfRoosterIsProperlyWritten() throws IOException {
        assertEquals("rooster: {\"Monday\":\"1\",\"Tuesday\":\"2\",\"Wednesday\":\"3\",\"Thursday\":\"4\",\"Friday\":\"5\",\"Saturday\":\"6\",\"Sunday\":\"7\"}","username123", Files.readString(Paths.get(ROOSTER_FILE)));
    }
}
