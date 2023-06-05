package nl.hu.sam.setup;

import nl.hu.sam.IPASS.model.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        make roles, objects not strings!!! TODO
        System.out.println("Initializing application");
        TeamLeider tl1 = new TeamLeider("Pieter");
        User tl1user = new User("pieterpiet", "pieter123", "teamleider");
        Medewerker mw1 = new Medewerker("Piet", "Maandag, 12-18, Donderdag 9-20, Zaterdag 14-22", "Vakkenvullen");
        User mw1user = new User("pieteliet", "geheim", "medewerker");
        Medewerker mw2 = new Medewerker("jan", "Dinsdag, 13-21, Donderdag 12-22, Zondag 12-20", "Vakkenvullen, La/Lo");
        User mw2user = new User("jandepan", "geheim123", "medewerker");
        Nieuws n1 = new Nieuws("12-6-2023", "tomaten zijn niet meer goed", "haal alle tomaten uit de winkel asap");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Terminating application");
    }

}
