package ru.meteo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class MeteoDataModel {

	@Getter @Setter private String temperature;
	
	@Getter @Setter private String humidity;
	
	@Getter @Setter private String precipitation;
}
