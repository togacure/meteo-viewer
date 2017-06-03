package ru.meteo.services;

import java.sql.Timestamp;
import java.util.Date;

import lombok.val;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.orm.MeteoData;
import ru.meteo.orm.enums.MeteoServiceName;

public interface IMeteoService {
	
	MeteoDataModel fetchNewInfo(Double latitude, Double longitude);
	void persist(MeteoServiceName name, MeteoData data);
	
	default MeteoData fetchNew(MeteoServiceName name, Double latitude, Double longitude) {
		val info = fetchNewInfo(latitude, longitude);
		if (info == null) {
			return null;
		}
		val result = new MeteoData();
		persist(name, result, info);
		return result;
	}

	default void persist(MeteoServiceName name, MeteoData data, MeteoDataModel info) {
		data.setTemperature(info.getTemperature());
		data.setHumidity(info.getHumidity());
		data.setPrecipitation(info.getPrecipitation());
		data.setFetchTimestamp(new Timestamp(new Date().getTime()));
		persist(name, data);
	}
}
