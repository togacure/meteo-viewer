package ru.meteo.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dao.MeteoDataRepository;
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.enums.MeteoServiceName;

@Slf4j
@Aspect
@Component
public class MeteoDataRestRequestInterceptor {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private MeteoDataRepository meteoDataRepository;
	
	@SneakyThrows
	@Around("execution(java.util.List<ru.meteo.orm.MeteoData> ru.meteo.dao.MeteoDataRepository.findByServiceNameIsAndLatitudeIsAndLongitudeIs(..)) && "
			+ "args(service, latitude, longitude)")
	public List<MeteoData> findByServiceInterceptor(ProceedingJoinPoint point, 
												MeteoServiceName service, 
												Double latitude, 
												Double longitude) {
		log.info("findByServiceInterceptor: original: {} service: {} latitude: {} longitude: {}", point.toShortString(), service, latitude, longitude);
		List<MeteoData> result = meteoDataRepository.findActuals(service, latitude, longitude, properties.getMeteodataValidPeriod());
		if (result.size() > 0) {
			return result;
		}
		return Lists.newArrayList(service.getService().fetchNew(service, latitude, longitude));
	}
}
