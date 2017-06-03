package ru.meteo.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.meteo.orm.MeteoData;
import ru.meteo.orm.MeteoData_;
import ru.meteo.orm.MeteoService;
import ru.meteo.orm.MeteoService_;
import ru.meteo.orm.enums.MeteoServiceName;

@RepositoryRestResource
public interface MeteoDataRepository extends CrudRepository<MeteoData, Long>, JpaSpecificationExecutor<MeteoData> {

	default MeteoData findByService(final MeteoServiceName service, final Double latitude, final Double longitude) {
		return findOne(new Specification<MeteoData> (){
			@Override
			public Predicate toPredicate(Root<MeteoData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
							cb.equal(root.<MeteoService> get(MeteoData_.service).<MeteoServiceName> get(MeteoService_.name), service),
							cb.equal(root.<Double> get(MeteoData_.latitude), latitude),
							cb.equal(root.<Double> get(MeteoData_.longitude), longitude)
						);
			}});
	}
}
