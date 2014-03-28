package com.haringeymobile.ukweather.data.json;

import com.google.gson.annotations.SerializedName;

public class Temperature {

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
		return dayTemperature;
	}

	public void setDayTemperature(double dayTemperature) {
		this.dayTemperature = dayTemperature;
	}

	public double getEveningTemperature() {
		return eveningTemperature;
	}

	public void setEveningTemperature(double eveningTemperature) {
		this.eveningTemperature = eveningTemperature;
	}

	public double getMorningTemperature() {
		return morningTemperature;
	}

	public void setMorningTemperature(double morningTemperature) {
		this.morningTemperature = morningTemperature;
	}

	public double getNightTemperature() {
		return nightTemperature;
	}

	public void setNightTemperature(double nightTemperature) {
		this.nightTemperature = nightTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

}
