package com.haringeymobile.ukweather.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.haringeymobile.ukweather.R;

public enum CityUK implements Parcelable {

	LONDON(R.string.city_name_london),

	BIRMINGHAM(R.string.city_name_birmingham),

	LEEDS(R.string.city_name_leeds),

	GLASGOW(R.string.city_name_glasgow),

	SHEFFIELD(R.string.city_name_sheffield),

	BRADFORD(R.string.city_name_bradford),

	EDINBURGH(R.string.city_name_edinburgh),

	LIVERPOOL(R.string.city_name_liverpool),

	MANCHESTER(R.string.city_name_manchester),

	BRISTOL(R.string.city_name_bristol),

	;

	private int displayNameStringResource;

	private CityUK(int displayNameStringResource) {
		this.displayNameStringResource = displayNameStringResource;
	}

	public String getOpenWeatherMapSearchName() {
		return this.name();
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
