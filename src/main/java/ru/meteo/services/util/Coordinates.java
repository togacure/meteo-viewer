package ru.meteo.services.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public class Coordinates {

	@Getter @Setter private Double latitude;
	
	@Getter @Setter private Double longitude;
	
}
