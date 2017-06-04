package ru.meteo.services;

import java.io.IOException;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.services.util.HumidityFormatter;
import ru.meteo.services.util.PrecipitationFormatter;
import ru.meteo.services.util.PrecipitationInfo;
import ru.meteo.services.util.TemperatureFormatter;

@Slf4j
@Component
public class OwmMeteoService extends AbstractMeteoService {
	
	@Autowired
	private ApplicationProperties properties;
	
	@Override
	public MeteoDataModel fetchNewInfo(Double latitude, Double longitude) {
		val client = getClient();
		try {
			val currentWeather = client.currentWeatherAtCityCircle(latitude.floatValue(), longitude.floatValue(), properties.getMeteodataGeoRadius());
			final WeatherData weather  = currentWeather.getWeatherStatus().get(0);
			val precipitation = new PrecipitationInfo();
			if (weather.hasRain()) {
				precipitation.setRain(weather.getRain());
			} 
			if (weather.hasSnow()) {
				precipitation.setSnow(weather.getSnow());
			}
			val result = new MeteoDataModel(
					new TemperatureFormatter().formatAsText(weather.getTemp()),
					new HumidityFormatter().formatAsText(weather.getHumidity()),
					new PrecipitationFormatter().formatAsText(precipitation));
			log.info("fetchNewInfo: result: {}", result);
			return result;
		} catch (IOException e) {
			log.error("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
			log.error("fetchNewInfo: ", e);
		} catch (JSONException e) {
			log.error("fetchNewInfo: latitude: {} longitude: {}", latitude, longitude);
			log.error("fetchNewInfo: ", e);
		}
		return null;
	}

	protected OwmClient getClient() {
		val owm = new OwmClient ();
		owm.setAPPID(properties.getOwmAPPID());
		return owm;
	}
	
}
