package com.haringeymobile.ukweather.data;

import java.net.URL;

public class JSONRetriever {

	private JSONRetrievingFromURLStrategy strategy;

	public void setHttpCallsHandlingStrategy(
			JSONRetrievingFromURLStrategy strategy) {
		this.strategy = strategy;
	}

	public String getJSONString(int cityId) {
		URL url = new OpenWeatherMapURLBuilder()
				.getCurrentWeatherByCityIdURL(cityId);
		return strategy.retrieveJSONString(url);
	}

	public String getJSONString(URL url) {
		return strategy.retrieveJSONString(url);
	}

}
