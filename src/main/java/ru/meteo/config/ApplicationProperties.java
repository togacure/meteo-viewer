package ru.meteo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("custom")
public class ApplicationProperties {
	
	@Getter @Setter private String jeoqueryUsername;

	@Getter @Setter private Integer meteodataValidPeriod = 5;
}
