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
        // Ottieni il profilo attivo (con fallback al profilo "default")
        String activeProfile = System.getProperty("spring.profiles.active", "default");
        System.out.println("Profilo attivo: " + activeProfile);

        // Avvia Swing solo se è stato esplicitamente configurato il profilo 'ui'
        if ("ui".equals(activeProfile)) {
            System.out.println("Attivazione dell'interfaccia Swing...");
            Toolkit.getDefaultToolkit();
            SpringApplication.run(UIApplication.class, args);
        }
        // Per gli altri profili, avvia semplicemente il contesto Spring
        else if ("docker".equals(activeProfile)) {
            System.out.println("Avvio in modalità Docker...");
            SpringApplication.run(WebApplication.class, args);
        }
        // Gestione di eventuali profili mancanti o non corretti
        else {
            System.out.println("Nessun profilo valido trovato. Impossibile avviare l'applicazione.");
        }

        System.out.println("Applicazione avviata con il profilo: " + activeProfile);
    }
}