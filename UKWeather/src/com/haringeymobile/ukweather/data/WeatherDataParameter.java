package com.haringeymobile.ukweather.data;

public enum WeatherDataParameter {

	CITY_NAME("name"),

	COORDINATES("coord"),

	LOCATION_LONGITUDE("lon"),

	LOCATION_LATITUDE("lat"),

	MAIN_INFO("main"),

	TEMPERATURE("temp"),

	TEMPERATURE_MIN("temp_min"),

	TEMPERATURE_MAX("temp_max"),

	PRESSURE("pressure"),

	HUMIDITY("humidity"),

	WEATHER_CONDITION("weather"),

	WEATHER_MAIN("main"),

	WEATHER_ICON("icon"),

	;

	private String openWeatherMapId;

	private WeatherDataParameter(String id) {
		this.openWeatherMapId = id;
	}

	public String getId() {
		return openWeatherMapId;
	}

}
