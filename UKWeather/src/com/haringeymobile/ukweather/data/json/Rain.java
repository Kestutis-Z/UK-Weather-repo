package com.haringeymobile.ukweather.data.json;

import com.google.gson.annotations.SerializedName;

public class Rain {

	@SerializedName("3h")
	private int precipitationVolumePer3HhoursInMm;

	public int getPrecipitationVolumePer3HhoursInMm() {
		return precipitationVolumePer3HhoursInMm;
	}

	public void setPrecipitationVolumePer3HhoursInMm(
			int precipitationVolumePer3HhoursInMm) {
		this.precipitationVolumePer3HhoursInMm = precipitationVolumePer3HhoursInMm;
	}

}
