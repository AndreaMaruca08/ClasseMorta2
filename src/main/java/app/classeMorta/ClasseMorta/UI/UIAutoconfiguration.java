package app.classeMorta.ClasseMorta.UI;

import app.classeMorta.ClasseMorta.logic.WebAutoconfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ImportAutoConfiguration(WebAutoconfiguration.class)
@ComponentScan(basePackages = "app.classeMorta.ClasseMorta.UI")
public class UIAutoconfiguration {
}
