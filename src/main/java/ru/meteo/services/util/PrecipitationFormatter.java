package ru.meteo.services.util;

import java.text.DecimalFormat;

public class PrecipitationFormatter implements IFormatter<Float> {

	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	@Override
	public String formatAsText(Float input) {
		return String.format("raining %s mm/h", format.format(input));
	}

}
