package com.haringeymobile.ukweather.data;

import java.net.URL;

public class JsonParser {

	private JsonParsingFromUrlStrategy strategy;

	public void setHttpCallsHandlingStrategy(
			JsonParsingFromUrlStrategy strategy) {
		this.strategy = strategy;
	}

	public String getJSONString(int cityId) {
		URL url = new OpenWeatherMapUrlBuilder()
				.getCurrentWeatherByCityIdURL(cityId);
		return strategy.retrieveJSONString(url);
	}

	public String getJSONString(URL url) {
		return strategy.retrieveJSONString(url);
	}

}
