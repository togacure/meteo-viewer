package ru.meteo.orm;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Getter;
import lombok.Setter;
import ru.meteo.orm.enums.MeteoServiceName;

@Entity
@Table(name = "METEO_SERVICE")
@RestResource(path = "meteo-service", rel = "meteo-services")
public class MeteoService {

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "SQ_METEO_SERVICE", allocationSize = 1)
	@Getter @Setter private BigDecimal id;
	
	@Column(name = "LABEL")
	@Getter @Setter private String label;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "NAME", insertable = false, updatable = false)
	@Getter @Setter private MeteoServiceName name;
	
	@Column(name = "CREATE_DATE", insertable = true, updatable = false)
	@Getter @Setter private Date createDate;
	
	@Column(name = "DELETE_DATE", insertable = true, updatable = false)
	@Getter @Setter private Date deleteDate;
}
