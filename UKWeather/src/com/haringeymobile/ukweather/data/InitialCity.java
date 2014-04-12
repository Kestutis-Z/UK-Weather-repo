package com.haringeymobile.ukweather.data;

public enum InitialCity {

	LONDON(2643743, "London"),

	MEXICO_CITY(3530597, "Mexico City"),

	RIO_DE_JANEIRO(3451190, "Rio de Janeiro"),

	CAIRO(360630, "Cairo"),

	MOSCOW(524901, "Moscow"),

	SEOUL(1835848, "Seoul"),

	BEIJING(1816670, "Beijing"),

	LOS_ANGELES(5368361, "Los Angeles"),

	ISTANBUL(745044, "Istanbul"),

	TOKYO(1850147, "Tokyo"),
	
	KOLKATA(1275004, "Kolkata"),

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
