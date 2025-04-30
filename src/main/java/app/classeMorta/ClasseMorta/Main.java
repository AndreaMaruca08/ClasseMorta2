package app.classeMorta.ClasseMorta;

import app.classeMorta.ClasseMorta.GUI.SwingGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		// Imposta l'app in modalità non headless per permettere la grafica
		System.setProperty("java.awt.headless", "false");

		// Controlla se tra gli argomenti c'è "grafica"
		boolean avviaGrafica = Arrays.asList(args).contains("grafica");

		// Avvia sempre Spring Boot
		var context = SpringApplication.run(Main.class, args);

		// Se richiesto, avvia anche la grafica
		if (avviaGrafica) {
			context.getBean(SwingGUI.class);
		} else {
			System.out.println("Avviata solo la parte web.");
		}
	}

}
