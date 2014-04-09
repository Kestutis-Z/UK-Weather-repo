package com.haringeymobile.ukweather.data;

import java.net.URL;

public class JsonParser {

	private JsonParsingFromUrlStrategy strategy;

	public void setJsonParsingStrategy(JsonParsingFromUrlStrategy strategy) {
		this.strategy = strategy;
	}

	public String getJsonString(URL url) {
		return strategy.parseJsonString(url);
	}

}
