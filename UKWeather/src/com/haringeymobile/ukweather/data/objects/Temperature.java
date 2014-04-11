package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;
import com.haringeymobile.ukweather.R;

public class Temperature {

	public enum TemperatureScale {

		CELSIUS(10, R.string.weather_info_degree_celcius) {

			@Override
			double convertTemperature(double kelvins) {
				return kelvins - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
			}

		},

		FAHRENHEIT(20, R.string.weather_info_degree_fahrenheit) {

			@Override
			double convertTemperature(double kelvins) {
				double celsius = CELSIUS.convertTemperature(kelvins);
				return celsius * 9 / 5 + 32;
			}

		};

		private int id;
		private int displayResourceId;

		private TemperatureScale(int id, int displayResourceId) {
			this.id = id;
			this.displayResourceId = displayResourceId;
		}

		public int getId() {
			return id;
		}

		public int getDisplayResourceId() {
			return displayResourceId;
		}

		abstract double convertTemperature(double kelvins);

		public static TemperatureScale getTemperatureScaleById(int id) {
			switch (id) {
			case 10:
				return CELSIUS;
			case 20:
				return FAHRENHEIT;
			default:
				throw new IllegalArgumentException(
						"Unsupported temperatureScaleId: " + id);
			}
		}
		
	}

	private static final double DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS = 273.15;

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

	public double getDayTemperature(TemperatureScale temperatureScale) {
		return temperatureScale.convertTemperature(dayTemperature);
	}

	public double getEveningTemperature(TemperatureScale temperatureScale) {
		return temperatureScale.convertTemperature(eveningTemperature);
	}

	public double getMorningTemperature(TemperatureScale temperatureScale) {
		return temperatureScale.convertTemperature(morningTemperature);
	}

	public double getNightTemperature(TemperatureScale temperatureScale) {
		return temperatureScale.convertTemperature(nightTemperature);
	}

}
