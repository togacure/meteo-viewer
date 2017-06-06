package ru.meteo.orm;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Getter;
import lombok.Setter;
import ru.meteo.orm.enums.MeteoDataStatus;

@Entity
@Table(name = "METEO_DATA")
@RestResource(path = "meteo-data", rel = "meteo-datas")
public class MeteoData {

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "SQ_METEO_DATA", allocationSize = 1)
	@Getter @Setter private BigDecimal id;
	
	@ManyToOne
    @JoinColumn(name = "SERVICE_ID")
	@Getter @Setter private MeteoService service;
	
	@Column(name = "LATITUDE")
	@Getter @Setter private Double latitude;
	
	@Column(name = "LONGITUDE")
	@Getter @Setter private Double longitude;
	
	@Column(name = "TEMPERATURE")
	@Getter @Setter private String temperature;
	
	@Column(name = "HUMIDITY")
	@Getter @Setter private String humidity;
	
	@Column(name = "PRECIPITATION")
	@Getter @Setter private String precipitation;
	
	@Column(name = "FETCH_TIMESTAMP")
	@Getter @Setter private Timestamp fetchTimestamp;
	
	@Transient
	@Getter @Setter private MeteoDataStatus status = MeteoDataStatus.UNKNOWN;
}
