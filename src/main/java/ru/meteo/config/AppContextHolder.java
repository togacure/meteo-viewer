package ru.meteo.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
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
    
    @SuppressWarnings("unchecked")
	public static final <T> T autowire(final Class<T> clazz) {
    	return (T) getApplicationContext().getAutowireCapableBeanFactory().autowire(clazz, AUTOWIRE_BY_TYPE, true);
    }

}