package ru.meteo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.meteo.orm.MeteoData;
import ru.meteo.orm.enums.MeteoServiceName;

@RepositoryRestResource
public interface MeteoDataRepository  extends CrudRepository<MeteoData, Long> {

	public MeteoData findByServiceNameIsAndLatitudeIsAndLongitudeIs(
						@Param("service") final MeteoServiceName service, 
						@Param("latitude") final Double latitude, 
						@Param("longitude") final Double longitude);
}
