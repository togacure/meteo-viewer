package ru.meteo.services.util;

import lombok.Getter;
import lombok.Setter;

public class PrecipitationInfo {

	@Getter @Setter private int rain = Integer.MIN_VALUE;
	
	@Getter @Setter private int snow = Integer.MIN_VALUE;
}
