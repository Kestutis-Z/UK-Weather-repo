package com.haringeymobile.ukweather.data;

import java.net.MalformedURLException;
import java.net.URL;

public class OpenWeatherMapUrl {

	private static final String OPEN_WEATHER_MAP_URL_PREFIX = "http://api.openweathermap.org/data/2.5/";
	private static final String WEATHER = "weather";
	private static final String ID = "?id=";
	private static final String FIND = "find?q=";
	private static final String LIKE = "&type=like";
	private static final String FORECAST = "forecast";
	private static final String FORECAST_DAILY = "forecast/daily";
	private static final String COUNT = "&cnt=";

	public URL generateCurrentWeatherByCityIdUrl(int cityId) {
		return getUrl(OPEN_WEATHER_MAP_URL_PREFIX + WEATHER + ID + cityId);
	}

	public URL getUrl(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	public URL getAvailableCitiesListUrl(String query) {
		return getUrl(OPEN_WEATHER_MAP_URL_PREFIX + FIND + query + LIKE);
	}

	public URL generateDailyWeatherForecastUrl(int cityId, int days) {
		return getUrl(OPEN_WEATHER_MAP_URL_PREFIX + FORECAST_DAILY + ID
				+ cityId + COUNT + days);
	}

	public URL generateThreeHourlyWeatherForecastUrl(int cityId) {
		return getUrl(OPEN_WEATHER_MAP_URL_PREFIX + FORECAST + ID + cityId);
	}

}
