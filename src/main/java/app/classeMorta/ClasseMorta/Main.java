package app.classeMorta.ClasseMorta;

import app.classeMorta.ClasseMorta.GUI.SwingGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = "app.classeMorta.ClasseMorta")
public class Main {
	public static void main(String[] args) {
		// Imposta l'app in modalità non headless per permettere la grafica
		System.setProperty("java.awt.headless", "false");

		// Avvia Spring Boot
		var context = SpringApplication.run(Main.class, args);

		// Ottieni il bean e chiama il metodo per creare la GUI
		SwingGUI gui = context.getBean(SwingGUI.class);
	}
}


