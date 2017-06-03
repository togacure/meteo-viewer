package ru.meteo.orm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.meteo.config.AppContextHolder;
import ru.meteo.services.IMeteoService;
import ru.meteo.services.MetMeteoService;
import ru.meteo.services.OwmMeteoService;

@AllArgsConstructor
public enum MeteoServiceName {
	OWM(AppContextHolder.getApplicationContext().getBean(OwmMeteoService.class)),
	MET(AppContextHolder.getApplicationContext().getBean(MetMeteoService.class)) 
	;
	
	@Getter private IMeteoService service;
	
}
