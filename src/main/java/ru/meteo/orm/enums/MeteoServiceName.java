package ru.meteo.orm.enums;

import lombok.AllArgsConstructor;
import ru.meteo.config.AppContextHolder;
import ru.meteo.services.IMeteoService;
import ru.meteo.services.MetMeteoService;
import ru.meteo.services.OwmMeteoService;

@AllArgsConstructor
public enum MeteoServiceName {
	OWM,
	MET 
	;
	
	public IMeteoService getService() {
		switch(this) {
		case OWM:
			return AppContextHolder.getApplicationContext().getBean(OwmMeteoService.class);
		case MET:
			return AppContextHolder.getApplicationContext().getBean(MetMeteoService.class);
		}
		throw new RuntimeException("unknown meteo service name enum");
	}
}
