package app.classeMorta.ClasseMorta.logic;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan(basePackages = "app.classeMorta.ClasseMorta.logic")
@EntityScan(basePackages = "app.classeMorta.ClasseMorta.logic.models")
@EnableJpaRepositories(basePackages = "app.classeMorta.ClasseMorta.logic.repository")
public class WebAutoconfiguration {
}
