package ru.meteo.aspect;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dao.MeteoDataRepository;
import ru.meteo.dao.MeteoServiceRepository;
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.MeteoService;
import ru.meteo.orm.MeteoService_;
import ru.meteo.orm.enums.MeteoDataStatus;
import ru.meteo.orm.enums.MeteoServiceName;

@Slf4j
@Aspect
@Component
public class MeteoDataRestRequestInterceptor {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Autowired
	private MeteoDataRepository meteoDataRepository;
	
	@Autowired
	private MeteoServiceRepository meteoServiceRepository;
	
	@SneakyThrows
	@Around("execution(java.util.List<ru.meteo.orm.MeteoData> ru.meteo.dao.MeteoDataRepository.findByServiceNameIsAndLatitudeIsAndLongitudeIs(..)) && "
			+ "args(service, latitude, longitude)")
	public List<MeteoData> findByServiceInterceptor(ProceedingJoinPoint point, 
												MeteoServiceName service, 
												Double latitude, 
												Double longitude) {
		log.info("findByServiceInterceptor: original: {} service: {} latitude: {} longitude: {}", point.toShortString(), service, latitude, longitude);
		final List<MeteoData> result = Lists.newArrayList(
				meteoDataRepository.findActuals(service, latitude, longitude, properties.getMeteodataValidPeriod()));
		result.forEach((md) -> {
			md.setStatus(MeteoDataStatus.SUCCESS);
		});
		if (result.size() < 1) {
			result.add(Optional.ofNullable(service.getService().fetchNew(latitude, longitude)).
					map((md) -> {
						md.setService(meteoServiceRepository.findOne(new Specification<MeteoService>(){
							@Override
							public Predicate toPredicate(Root<MeteoService> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
								return cb.equal(root.<MeteoServiceName> get(MeteoService_.name), service);
							}}));
						return md;
					}).
					map(this::persist).
					orElseGet(MeteoDataRestRequestInterceptor::createError));
		}
		return result;
	}
	
	@Transactional
	private MeteoData persist(MeteoData md) {
		val result = meteoDataRepository.save(md);
		return result;
	}
	
	private static final MeteoData createError() {
		final MeteoData error = new MeteoData();
		error.setStatus(MeteoDataStatus.ERROR);
		return error;
	}
}
