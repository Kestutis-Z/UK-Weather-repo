package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;

public class Temperature {

	static final double DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS = 273.15;

	@SerializedName("day")
	private double dayTemperature;

	@SerializedName("eve")
	private double eveningTemperature;

	@SerializedName("morn")
	private double morningTemperature;

	@SerializedName("night")
	private double nightTemperature;

	@SerializedName("max")
	private double maxTemperature;

	@SerializedName("min")
	private double minTemperature;

	public double getDayTemperature() {
		return dayTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public double getEveningTemperature() {
		return eveningTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public double getMorningTemperature() {
		return morningTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public double getNightTemperature() {
		return nightTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

}
