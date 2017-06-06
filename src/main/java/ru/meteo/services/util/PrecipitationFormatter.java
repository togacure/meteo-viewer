package ru.meteo.services.util;

import java.text.DecimalFormat;

import com.google.common.base.Objects;

public class PrecipitationFormatter implements IFormatter<Float> {
	
	public static final String MISSING_PRECITITATIONS_MESSAGE = "has no any precipitations";

	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	@Override
	public String formatAsText(Float input) {
		return Objects.equal(input, Float.NaN) || input <= 0 ? MISSING_PRECITITATIONS_MESSAGE : String.format("intencity is %s mm/h", format.format(input));
	}

}
