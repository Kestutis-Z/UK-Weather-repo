package com.haringeymobile.ukweather.data;

import android.os.Parcel;
import android.os.Parcelable;

public enum CityUK implements Parcelable {

	LONDON(2643743, "London"),

	BIRMINGHAM(2655603, "Birmingham"),

	LEEDS(2644688, "Leeds"),

	GLASGOW(2648579, "Glasgow"),

	SHEFFIELD(2638077, "Sheffield"),

	BRADFORD(2654993, "Bradford"),

	EDINBURGH(2650225, "Edinburgh"),

	LIVERPOOL(2644210, "Liverpool"),

	MANCHESTER(2643123, "Manchester"),

	BRISTOL(2654675, "Bristol"),

	;

	private int openWeatherMapId;
	private String displayName;

	private CityUK(int openWeatherMapId, String displayName) {
		this.openWeatherMapId = openWeatherMapId;
		this.displayName = displayName;
	}

	public int getOpenWeatherMapId() {
		return openWeatherMapId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static final Parcelable.Creator<CityUK> CREATOR = new Parcelable.Creator<CityUK>() {

		@Override
		public CityUK createFromParcel(Parcel in) {
			CityUK city;
			try {
				city = valueOf(in.readString());
			} catch (IllegalArgumentException ex) {
				city = null;
			}
			return city;
		}

		@Override
		public CityUK[] newArray(int size) {
			return new CityUK[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this == null ? "" : name());
	}

}
