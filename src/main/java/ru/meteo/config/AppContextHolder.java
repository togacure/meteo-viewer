package ru.meteo.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class AppContextHolder implements ApplicationContextAware {
    
	private volatile static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContextHolder.applicationContext = applicationContext;
    }

    @SneakyThrows
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new Exception("Too early access to applicationContext");
        }
        return applicationContext;
    }

}