package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.logic.WebApplication;
import app.classeMorta.ClasseMorta.logic.WebAutoconfiguration;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ImportAutoConfiguration(WebAutoconfiguration.class)
@ComponentScan(basePackages = "app.classeMorta.ClasseMorta.GUI")
public class UIAutoconfiguration {
}
