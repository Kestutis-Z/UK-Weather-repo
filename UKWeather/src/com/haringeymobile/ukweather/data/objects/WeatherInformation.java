package com.haringeymobile.ukweather.data.objects;

import com.haringeymobile.ukweather.data.objects.Temperature.TemperatureScale;

public interface WeatherInformation {

	public abstract String getDescription();

	public abstract String getType();

	public abstract String getIconName();

	public abstract double getDayTemperature(TemperatureScale temperatureScale);

	public abstract double getHumidity();

	public abstract double getPressure();

	public abstract Wind getWind();

}
