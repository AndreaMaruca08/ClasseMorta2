package app.classeMorta.ClasseMorta.UI;

import app.classeMorta.ClasseMorta.logic.WebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
@ImportAutoConfiguration(UIAutoconfiguration.class)
public class UIApplication {

    public static void main(String[] args) {
        // Forza il profilo di default a "docker"
        System.setProperty("spring.profiles.default", "docker");
        System.setProperty("spring.profiles.active", System.getProperty("spring.profiles.active", "docker"));

        String activeProfile = System.getProperty("spring.profiles.active");
        System.out.println("Profilo attivo forzato: " + activeProfile);

        SpringApplication.run(UIApplication.class, args);
    }
}