package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;

public class Clouds {

	@SerializedName("all")
	private int cloudinessPercentage;

	public int getCloudinessPercentage() {
		return cloudinessPercentage;
	}

	public void setCloudinessPercentage(int cloudinessPercentage) {
		this.cloudinessPercentage = cloudinessPercentage;
	}

}
