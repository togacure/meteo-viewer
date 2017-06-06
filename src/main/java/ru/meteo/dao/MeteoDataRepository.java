package ru.meteo.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import lombok.val;
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.MeteoData_;
import ru.meteo.orm.MeteoService;
import ru.meteo.orm.MeteoService_;
import ru.meteo.orm.enums.MeteoServiceName;

@RepositoryRestResource
public interface MeteoDataRepository  extends CrudRepository<MeteoData, Long>, JpaSpecificationExecutor<MeteoData> {

	public List<MeteoData> findByServiceNameIsAndLatitudeIsAndLongitudeIs(
						@Param("service") final MeteoServiceName service, 
						@Param("latitude") final Double latitude, 
						@Param("longitude") final Double longitude);
	
	public default List<MeteoData> findActuals(final MeteoServiceName service, final Double latitude, final Double longitude, int expire) {
		return findAll(new Specification<MeteoData> (){
						@Override
						public Predicate toPredicate(Root<MeteoData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
							val current = new Date();
							val from = Calendar.getInstance();
							from.setTime(current);
							from.set(Calendar.MINUTE, from.get(Calendar.MINUTE) - expire);
							val to = Calendar.getInstance();
							to.setTime(current);
							return cb.and(
										cb.equal(root.<MeteoService> get(MeteoData_.service).<MeteoServiceName> get(MeteoService_.name), service),
										cb.equal(root.<Double> get(MeteoData_.latitude), latitude),
										cb.equal(root.<Double> get(MeteoData_.longitude), longitude),
										cb.between(
												root.<Timestamp> get(MeteoData_.fetchTimestamp), 
												new Timestamp(from.getTime().getTime()), 
												new Timestamp(to.getTime().getTime()))
									);
						}});
	}
}
