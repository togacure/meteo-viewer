package ru.meteo.services.util;

import java.text.DecimalFormat;

public class TemperatureFormatter implements IFormatter<Float> {

	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	@Override
	public String formatAsText(Float input) {
		return String.format("%s\u00b0", format.format(input));
	}

}
