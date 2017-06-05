package ru.meteo.orm.enums;

import lombok.AllArgsConstructor;
import ru.meteo.config.AppContextHolder;
import ru.meteo.services.IMeteoService;
import ru.meteo.services.ForecastIoMeteoService;
import ru.meteo.services.OwmMeteoService;

@AllArgsConstructor
public enum MeteoServiceName {
	OWM,
	FCAST 
	;
	
	public IMeteoService getService() {
		switch(this) {
		case OWM:
			return AppContextHolder.<OwmMeteoService> autowire(OwmMeteoService.class);
		case FCAST:
			return AppContextHolder.<ForecastIoMeteoService> autowire(ForecastIoMeteoService.class);
		}
		throw new RuntimeException("unknown meteo service name enum");
	}
}
