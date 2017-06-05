package ru.meteo.services;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.OpenWeatherMap;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.services.util.HumidityFormatter;
import ru.meteo.services.util.PrecipitationFormatter;
import ru.meteo.services.util.TemperatureFormatter;

@Slf4j
@Component
public class OwmMeteoService extends AbstractMeteoService {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Override
	public MeteoDataModel fetchNewInfo(Double latitude, Double longitude) {
		log.info("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
		if (java.util.Objects.isNull(latitude) || java.util.Objects.isNull(longitude)) {
			return null;
		}
		val client = new OpenWeatherMap(properties.getOwmAPPID());
		try {
			val currentWeather = client.currentWeatherByCoordinates(latitude.floatValue(), longitude.floatValue());
			final Float rain = currentWeather.hasRainInstance() ? currentWeather.getRainInstance().getRain() : Float.NaN;
			val result = new MeteoDataModel(
					new TemperatureFormatter().formatAsText(fahrenheitToCelsius(currentWeather.getMainInstance().getTemperature())),
					new HumidityFormatter().formatAsText(currentWeather.getMainInstance().getHumidity()),
					new PrecipitationFormatter().formatAsText(rain));
			log.info("fetchNewInfo: result: {}", result);
			return result;
		} catch (JSONException e) {
			log.error("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
			log.error("fetchNewInfo: ", e);
		}
		return null;
	}

}
