package ru.meteo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
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
public class ForecastIoMeteoService extends SynchronizedExecutor<Coordinates, MeteoDataModel> implements IMeteoService {

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
		log.info("fetchAndFill: latitude: {} longitude: {}", latitude, longitude);
		return executeOnce(new Coordinates(latitude, longitude), (o) -> {
			val fio = new ForecastIO(properties.getForecastIoSecretKey()); 
			if (!fio.getForecast(latitude.toString(), longitude.toString())) {
				info.setStatus(MeteoDataStatus.ERROR);
				info.setStatus(MeteoDataStatus.SUCCESS);
				log.info("fetchAndFill: info: {}", info);
				return info;
			}
			val currently = new FIOCurrently(fio);
			info.setHumidity(new HumidityFormatter().formatAsText(currently.get().humidity().floatValue() * 100));
			info.setPrecipitation(new PrecipitationFormatter().formatAsText(currently.get().precipIntensity().floatValue()));
			info.setTemperature(new TemperatureFormatter().formatAsText(currently.get().temperature().floatValue()));
			info.setStatus(MeteoDataStatus.SUCCESS);
			log.info("fetchAndFill: info: {}", info);
			return info;
		}, (o) -> {
			info.setStatus(MeteoDataStatus.PENDING);
			log.info("fetchAndFill: info: {}", info);
			return info;
		});
	}

}
