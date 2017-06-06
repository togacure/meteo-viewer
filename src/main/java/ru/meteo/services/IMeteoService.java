package ru.meteo.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import com.google.common.base.Preconditions;

import lombok.val;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.orm.MeteoData;

public interface IMeteoService {
	
	default MeteoDataModel fetchNewInfo(Double latitude, Double longitude) {
		Preconditions.checkNotNull(latitude);
		Preconditions.checkNotNull(longitude);
		return new MeteoDataModel();
	}
	
	default MeteoData fetchNew(Double latitude, Double longitude) {
		return Optional.ofNullable(fetchNewInfo(latitude, longitude)).map((info) -> {
			val result = new MeteoData();
			result.setLatitude(latitude);
			result.setLongitude(longitude);
			result.setTemperature(info.getTemperature());
			result.setHumidity(info.getHumidity());
			result.setPrecipitation(info.getPrecipitation());
			result.setFetchTimestamp(new Timestamp(new Date().getTime()));
			result.setStatus(info.getStatus());
			return result;
		}).orElse(null);
	}
	
	default float fahrenheitToCelsius(float c) {
		return ((c - 32) * 5) / 9;
	}
}
