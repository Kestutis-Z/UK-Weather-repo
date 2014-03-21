package com.haringeymobile.ukweather.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.haringeymobile.ukweather.R;

public enum CityUK implements Parcelable {

	LONDON("london", R.string.city_name_london),

	BIRMINGHAM("birmingham", R.string.city_name_birmingham),

	MANCHESTER("manchester", R.string.city_name_manchester),

	LUTON("luton", R.string.city_name_luton),

	;

	private String openWeatherMapSearchName;
	private int displayNameStringResource;

	private CityUK(String openWeatherMapSearchName,
			int displayNameStringResource) {
		this.openWeatherMapSearchName = openWeatherMapSearchName;
		this.displayNameStringResource = displayNameStringResource;
	}

	public String getOpenWeatherMapSearchName() {
		return openWeatherMapSearchName;
	}

	public int getDisplayNameStringResource() {
		return displayNameStringResource;
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
