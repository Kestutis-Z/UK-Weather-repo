package com.haringeymobile.ukweather.data.objects;

public interface WeatherInformation {

	public abstract String getDescription();

	public abstract String getType();

	public abstract String getIconName();

	public abstract double getDayTemperature();

	public abstract double getHumidity();

	public abstract double getPressure();

	public abstract Wind getWind();

}
