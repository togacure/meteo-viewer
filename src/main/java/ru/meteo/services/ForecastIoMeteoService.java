package ru.meteo.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.services.util.HumidityFormatter;
import ru.meteo.services.util.PrecipitationFormatter;
import ru.meteo.services.util.TemperatureFormatter;

@Slf4j
@Component
public class ForecastIoMeteoService extends AbstractMeteoService {

	@Autowired
	private ApplicationProperties properties;
	
	@Override
	public MeteoDataModel fetchNewInfo(Double latitude, Double longitude) {
		log.info("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
		if (Objects.isNull(latitude) || Objects.isNull(longitude)) {
			return null;
		}
		val fio = new ForecastIO(properties.getForecastIoSecretKey()); 
		if (!fio.getForecast(latitude.toString(), longitude.toString())) {
			return null;
		}
		val currently = new FIOCurrently(fio);
		val result = new MeteoDataModel(
				new TemperatureFormatter().formatAsText(currently.get().temperature().floatValue()),
				new HumidityFormatter().formatAsText(currently.get().humidity().floatValue()),
				currently.get().precipIntensity() > 0 ? new PrecipitationFormatter().formatAsText(currently.get().precipIntensity().floatValue()) : "has no rain");
		log.info("fetchNewInfo: result: {}", result);
		return result;
	}

}
