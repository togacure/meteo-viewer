package ru.meteo.aspect;

import java.util.Calendar;
import java.util.Date;
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
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.enums.MeteoServiceName;

@Slf4j
@Aspect
@Component
public class MeteoDataRestRequestInterceptor {
	
	@Autowired
	private ApplicationProperties properties;
	
	@SneakyThrows
	@Around("execution(java.util.List<ru.meteo.orm.MeteoData> ru.meteo.dao.MeteoDataRepository.findByServiceNameIsAndLatitudeIsAndLongitudeIs(..)) && "
			+ "args(service, latitude, longitude)")
	public List<MeteoData> findByServiceInterceptor(ProceedingJoinPoint point, 
												MeteoServiceName service, 
												Double latitude, 
												Double longitude) {
		log.info("findByServiceInterceptor: service: {} latitude: {} longitude: {}", service, latitude, longitude);
		@SuppressWarnings("unchecked")
		List<MeteoData> result = (List<MeteoData>) point.proceed(new Object[] {service, latitude, longitude});
		if (result == null || result.size() < 1) {
			return Lists.<MeteoData> newArrayList(service.getService().fetchNew(service, latitude, longitude));
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(result.get(0).getFetchTimestamp().getTime());
		calendar.add(Calendar.MINUTE, properties.getMeteodataValidPeriod());
		if (calendar.after(new Date())) {
			return result;
		}
		return Lists.newArrayList(service.getService().fetchNew(service, latitude, longitude));
	}
}
