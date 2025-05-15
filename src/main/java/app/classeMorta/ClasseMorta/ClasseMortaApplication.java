package app.classeMorta.ClasseMorta;

import app.classeMorta.ClasseMorta.GUI.SwingGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ClasseMortaApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        // Avvia Spring Boot
        var context = SpringApplication.run(ClasseMortaApplication.class, args);
        // Ottieni il bean e chiama il metodo per creare la GUI
        if (!Arrays.asList(args).contains("--test")) {
            context.getBean(SwingGUI.class);
        }

    }
}


