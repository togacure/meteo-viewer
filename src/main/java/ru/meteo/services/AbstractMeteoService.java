package ru.meteo.services;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import ru.meteo.dao.MeteoDataRepository;
import ru.meteo.dao.MeteoServiceRepository;
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.MeteoService;
import ru.meteo.orm.MeteoService_;
import ru.meteo.orm.enums.MeteoServiceName;

public abstract class AbstractMeteoService implements IMeteoService {

	@Autowired
	private MeteoServiceRepository meteoServiceRepository;
	
	@Autowired
	private MeteoDataRepository meteoDataRepository;
	
	@Transactional
	@Override
	public void persist(final MeteoServiceName name, final MeteoData data) {
		data.setService(meteoServiceRepository.findOne(new Specification<MeteoService>(){
			@Override
			public Predicate toPredicate(Root<MeteoService> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.<MeteoServiceName> get(MeteoService_.name), name);
			}}));
		meteoDataRepository.save(data);
	}

}
