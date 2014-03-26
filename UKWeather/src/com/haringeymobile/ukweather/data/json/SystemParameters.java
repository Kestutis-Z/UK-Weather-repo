package com.haringeymobile.ukweather.data.json;

import com.google.gson.annotations.SerializedName;

public class SystemParameters {

	@SerializedName("country")
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
