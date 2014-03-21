package com.haringeymobile.ukweather.data;

public class LocationCoordinates {

	private double latitude;
	private double longitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LocationCoordinates [latitude=").append(latitude)
				.append(", longitude=").append(longitude).append("]");
		return builder.toString();
	}

}
