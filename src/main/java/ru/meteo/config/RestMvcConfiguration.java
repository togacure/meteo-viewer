package ru.meteo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = { "ru.meteo.dao" })
public class RestMvcConfiguration extends RepositoryRestConfigurerAdapter  {

	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setBasePath("/api");
	}

}
