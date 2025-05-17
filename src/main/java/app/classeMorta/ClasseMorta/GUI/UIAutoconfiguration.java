package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.logic.WebAutoconfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ImportAutoConfiguration(WebAutoconfiguration.class)
@ComponentScan(basePackages = "app.classeMorta.ClasseMorta.GUI")
public class UIAutoconfiguration {
}
