package ru.meteo.services;

import java.util.Optional;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.OpenWeatherMap;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.orm.enums.MeteoDataStatus;
import ru.meteo.services.util.HumidityFormatter;
import ru.meteo.services.util.PrecipitationFormatter;
import ru.meteo.services.util.TemperatureFormatter;

@Slf4j
@Component
public class OwmMeteoService implements IMeteoService {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Override
	public MeteoDataModel fetchNewInfo(Double latitude, Double longitude) {
		log.info("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
		return Optional.ofNullable(IMeteoService.super.fetchNewInfo(latitude, longitude)).
				map((result) -> {
					return fetchAndFill(result, latitude, longitude);
				}).get();
	}

	private MeteoDataModel fetchAndFill(MeteoDataModel info, Double latitude, Double longitude) {
		val client = new OpenWeatherMap(properties.getOwmAPPID());
		try {
			val currentWeather = client.currentWeatherByCoordinates(latitude.floatValue(), longitude.floatValue());
			final Float rain = currentWeather.hasRainInstance() ? currentWeather.getRainInstance().getRain() : Float.NaN;
			info.setHumidity(new HumidityFormatter().formatAsText(currentWeather.getMainInstance().getHumidity()));
			info.setPrecipitation(new PrecipitationFormatter().formatAsText(rain));
			info.setTemperature(new TemperatureFormatter().formatAsText(fahrenheitToCelsius(currentWeather.getMainInstance().getTemperature())));
			info.setStatus(MeteoDataStatus.SUCCESS);
			log.info("fetchAndFill: info: {}", info);
		} catch (JSONException e) {
			log.error("fetchAndFill: latitude: {} longitude: {}", latitude, longitude);
			log.error("fetchAndFill: ", e);
			info.setStatus(MeteoDataStatus.ERROR);
		}
		return info;
	}
}
