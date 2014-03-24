package com.haringeymobile.ukweather.data.json;

import com.google.gson.annotations.SerializedName;

public class Wind {

	@SerializedName("deg")
	private int directionInDegrees;

	private double speed;

	public int getDirectionInDegrees() {
		return directionInDegrees;
	}

	public void setDirectionInDegrees(int directionInDegrees) {
		this.directionInDegrees = directionInDegrees;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
