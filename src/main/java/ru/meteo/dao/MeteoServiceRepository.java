package ru.meteo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.meteo.orm.MeteoService;

@RepositoryRestResource
public interface MeteoServiceRepository extends CrudRepository<MeteoService, Long>, JpaSpecificationExecutor<MeteoService> {

}
