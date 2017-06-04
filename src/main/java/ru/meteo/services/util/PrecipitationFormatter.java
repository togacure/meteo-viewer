package ru.meteo.services.util;

import lombok.val;

public class PrecipitationFormatter implements IFormatter<PrecipitationInfo> {

	@Override
	public String formatAsText(PrecipitationInfo input) {
		val result = new StringBuilder();
		if (input.getRain() == Integer.MIN_VALUE && input.getSnow() == Integer.MIN_VALUE) {
			result.append("has no rain or snow");
		}
		if (input.getRain() != Integer.MIN_VALUE) {
			result.append(String.format("raining %s mm/h ", input.getRain()));
		}
		if (input.getSnow() != Integer.MIN_VALUE) {
			result.append(String.format("snowing %s mm/h ", input.getRain()));
		}
		return result.toString();
	}

}
