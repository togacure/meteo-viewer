package ru.meteo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.meteo.orm.enums.MeteoDataStatus;

@ToString
public class MeteoDataModel {

	@Getter @Setter private String temperature;
	
	@Getter @Setter private String humidity;
	
	@Getter @Setter private String precipitation;
	
	@Getter @Setter private MeteoDataStatus status;
}
