package com.haringeymobile.ukweather.data;

import java.net.MalformedURLException;
import java.net.URL;

public class OpenWeatherMapUrlBuilder {

	private static final String OPEN_WEATHER_MAP_URL_PREFIX = "http://api.openweathermap.org/data/2.5/";
	private static final String WEATHER = "weather";
	private static final String QUERY = "?q=";
	private static final String ID = "?id=";
	private static final String FIND = "find?q=";
	private static final String LIKE = "&type=like";

	// static final String OPEN_WEATHER_MAP_URL_PREFIX =
	// "http://api.openweathermap.org/data/2.5/weather?id=";

	public URL getCurrentWeatherByCityIdURL(int cityId) {
		return getURL(OPEN_WEATHER_MAP_URL_PREFIX + WEATHER + ID + cityId);
	}

	public URL getURL(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	public URL getAvailableCitiesListURL(String query) {
		return getURL(OPEN_WEATHER_MAP_URL_PREFIX + FIND + query + LIKE);
	}

}
