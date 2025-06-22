package app.classeMorta.ClasseMorta.UI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
@ImportAutoConfiguration(UIAutoconfiguration.class)
public class UIApplication {

    public static void main(String[] args) {
        // Controlla il profilo attivo
        String activeProfile = System.getProperty("spring.profiles.active", "default");

        // Avvia Swing solo se nel profilo 'ui'
        if ("ui".equals(activeProfile)) {
            Toolkit.getDefaultToolkit();
        }

        SpringApplication.run(UIApplication.class, args);
    }
}