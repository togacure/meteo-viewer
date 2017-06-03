package ru.meteo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages="ru.meteo")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.meteo.dao")
@EnableAutoConfiguration
@EntityScan("ru.meteo.orm")
public class ApplicationConfiguration {

}
