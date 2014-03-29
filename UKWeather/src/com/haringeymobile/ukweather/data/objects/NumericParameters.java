package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;

public class NumericParameters {

	private static final double DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS = 273.15;

	@SerializedName("humidity")
	private double humidity;

	@SerializedName("pressure")
	private double pressure;

	@SerializedName("temp")
	private double temperature;

	@SerializedName("temp_max")
	private double maxTemperature;

	@SerializedName("temp_min")
	private double minTemperature;

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getTemperature() {
		return temperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getMaxTemperature() {
		return maxTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public double getMinTemperature() {
		return minTemperature - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

}
