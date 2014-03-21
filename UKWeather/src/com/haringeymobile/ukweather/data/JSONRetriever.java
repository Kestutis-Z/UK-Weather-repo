package com.haringeymobile.ukweather.data;

import java.net.MalformedURLException;
import java.net.URL;

public class JSONRetriever {

	public static String OPEN_WEATHER_MAP_URL_PREFIX = "http://api.openweathermap.org/data/2.5/weather?q=";
	public static String SUFFIX_UK = ",uk";

	private JSONRetrievingFromURLStrategy strategy;

	public void setHttpCallsHandlingStrategy(
			JSONRetrievingFromURLStrategy strategy) {
		this.strategy = strategy;
	}

	public String getJSONString(String openWeatherMapSearchName) {
		URL url = generateURL(openWeatherMapSearchName);
		return strategy.retrieveJSONString(url);
	}

	private URL generateURL(String openWeatherMapSearchName) {
		URL url = null;
		try {
			url = new URL(OPEN_WEATHER_MAP_URL_PREFIX
					+ openWeatherMapSearchName + SUFFIX_UK);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
