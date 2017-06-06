package ru.meteo.services;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.OpenWeatherMap;
import ru.meteo.config.ApplicationProperties;
import ru.meteo.dto.MeteoDataModel;
import ru.meteo.orm.enums.MeteoDataStatus;
import ru.meteo.services.util.Coordinates;
import ru.meteo.services.util.HumidityFormatter;
import ru.meteo.services.util.PrecipitationFormatter;
import ru.meteo.services.util.TemperatureFormatter;
import ru.meteo.util.SynchronizedExecutor;

@Slf4j
@Component
public class OwmMeteoService extends SynchronizedExecutor<Coordinates, MeteoDataModel> implements IMeteoService {
	
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
		return executeOnce(new Coordinates(latitude, longitude), (o) -> {
			val client = new OpenWeatherMap(properties.getOwmAPPID());
			try {
				val currentWeather = client.currentWeatherByCoordinates(latitude.floatValue(), longitude.floatValue());
				if (!currentWeather.hasRawResponse()) {
					info.setStatus(MeteoDataStatus.ERROR);
					return info;
				}
				Float rain = Optional.ofNullable(currentWeather.getRainInstance()).map((r) -> {return r.getRain();}).orElse(Float.NaN);
				rain = currentWeather.hasRainInstance() && Objects.equal(rain, Float.NaN) ? fixupParseRain(currentWeather.getRawResponse()) : rain;
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
		}, (o) -> {
			info.setStatus(MeteoDataStatus.PENDING);
			log.info("fetchAndFill: info: {}", info);
			return info;
		});
		
	}
	
	/**
	 * FIXME update vendor to 2.5.0.5 on central maven, see {@link https://github.com/akapribot/OWM-JAPIs}
	 */
	private Float fixupParseRain(String rawResponse) throws JSONException {
		return Optional.ofNullable(new JSONObject(rawResponse).getJSONObject("rain")).map((rain) -> {
			return rain.optDouble("3h", Double.NaN);
		}).orElse(Double.NaN).floatValue();
	}
}
