package com.haringeymobile.ukweather.data;

public class Temperature {

	private static final double DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS = 273.15;

	private double main;
	private double minimum;
	private double maximum;

	public double getMain() {
		return main;
	}

	public void setMain(double main) {
		this.main = main - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public double getMinimum() {
		return minimum - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum - DIFFERENCE_BETWEEN_KELVIN_AND_CELCIUS;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

}
