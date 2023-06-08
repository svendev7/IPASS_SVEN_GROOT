package nl.hu.sam.IPASS.tests;


import nl.hu.sam.IPASS.model.Nieuws;
import nl.hu.sam.IPASS.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NieuwsTest {
    private Nieuws n1;
    @BeforeEach
    public void setup() {
        Nieuws n1 = new Nieuws("2020-02-12", "nieuws", "niks doen");

    }

    @Test
    public void seeIfInformationIsSetRight() {
        assertEquals("nieuws", n1.getInformatie() );
    }
}
