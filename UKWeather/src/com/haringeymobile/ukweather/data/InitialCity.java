package com.haringeymobile.ukweather.data;

public enum InitialCity {

	LONDON(2643743, "London"),

	BIRMINGHAM(2655603, "Birmingham"),

	LEEDS(2644688, "Leeds"),

	GLASGOW(2648579, "Glasgow"),

	SHEFFIELD(2638077, "Sheffield"),

	BRADFORD(2654993, "Bradford"),

	EDINBURGH(2650225, "Edinburgh"),

	LIVERPOOL(2644210, "Liverpool"),

	MANCHESTER(2643123, "Manchester"),

	BRISTOL(2654675, "Bristol"),

	;

	private int openWeatherMapId;
	private String displayName;

	private InitialCity(int openWeatherMapId, String displayName) {
		this.openWeatherMapId = openWeatherMapId;
		this.displayName = displayName;
	}

	public int getOpenWeatherMapId() {
		return openWeatherMapId;
	}

	public String getDisplayName() {
		return displayName;
	}

}
