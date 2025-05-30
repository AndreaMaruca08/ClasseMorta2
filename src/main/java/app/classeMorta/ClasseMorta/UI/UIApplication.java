package app.classeMorta.ClasseMorta.UI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
@ImportAutoConfiguration(UIAutoconfiguration.class)
public class UIApplication {

    public static void main(String[] args) {
        Toolkit.getDefaultToolkit();
        SpringApplication.run(UIApplication.class, args);
    }

}


