package com.haringeymobile.ukweather.data;

import java.net.MalformedURLException;
import java.net.URL;

public class JSONRetriever {

	public static String OPEN_WEATHER_MAP_URL_PREFIX = "http://api.openweathermap.org/data/2.5/weather?id=";

	private JSONRetrievingFromURLStrategy strategy;

	public void setHttpCallsHandlingStrategy(
			JSONRetrievingFromURLStrategy strategy) {
		this.strategy = strategy;
	}

	public String getJSONString(int cityId) {
		URL url = generateURL(cityId);
		return strategy.retrieveJSONString(url);
	}

	private URL generateURL(int cityId) {
		URL url = null;
		try {
			url = new URL(OPEN_WEATHER_MAP_URL_PREFIX + cityId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
