package ru.meteo.services.util;

import java.text.DecimalFormat;

public class HumidityFormatter implements IFormatter<Float> {

	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	@Override
	public String formatAsText(Float input) {
		return String.format("%s%", format.format(input));
	}

}
