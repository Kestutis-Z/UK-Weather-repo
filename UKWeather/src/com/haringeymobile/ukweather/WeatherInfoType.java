package com.haringeymobile.ukweather;

import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.OpenWeatherMapUrl;
import com.haringeymobile.ukweather.data.objects.CityCurrentWeather;
import com.haringeymobile.ukweather.data.objects.CityDailyWeatherForecast;
import com.haringeymobile.ukweather.data.objects.CityThreeHourlyWeatherForecast;
import com.haringeymobile.ukweather.data.objects.WeatherInformation;

public enum WeatherInfoType implements Parcelable {

	CURRENT_WEATHER(1, CityCurrentWeather.class,
			R.string.weather_info_type_label_current_weather,
			R.drawable.refresh),

	DAILY_WEATHER_FORECAST(2, CityDailyWeatherForecast.class,
			R.string.weather_info_type_label_daily_forecast,
			R.drawable.forecast_daily),

	THREE_HOURLY_WEATHER_FORECAST(3, CityThreeHourlyWeatherForecast.class,
			R.string.weather_info_type_label_three_hourly_forecast,
			R.drawable.forecast_three_hourly);

	private static final int DEFAULT_DAYS_COUNT_FOR_DAILY_FORECAST = 14;

	private int id;
	Class<? extends WeatherInformation> clazz;
	private int labelResourceId;
	private int iconResourceId;

	private WeatherInfoType(int id, Class<? extends WeatherInformation> clazz,
			int labelResourceId, int iconResourceId) {
		this.id = id;
		this.clazz = clazz;
		this.labelResourceId = labelResourceId;
		this.iconResourceId = iconResourceId;
	}

	public static WeatherInfoType getTypeById(int id) {
		switch (id) {
		case 1:
			return CURRENT_WEATHER;
		case 2:
			return DAILY_WEATHER_FORECAST;
		case 3:
			return THREE_HOURLY_WEATHER_FORECAST;
		default:
			throw new IllegalArgumentException("Unsupported id: " + id);
		}
	}

	public int getId() {
		return id;
	}

	public int getLabelResourceId() {
		return labelResourceId;
	}

	public int getIconResourceId() {
		return iconResourceId;
	}

	public URL getOpenWeatherMapUrl(int cityId) {
		OpenWeatherMapUrl openWeatherMapUrl = new OpenWeatherMapUrl();
		switch (this) {
		case CURRENT_WEATHER:
			return openWeatherMapUrl.generateCurrentWeatherByCityIdUrl(cityId);
		case DAILY_WEATHER_FORECAST:
			return openWeatherMapUrl.generateDailyWeatherForecastUrl(cityId,
					DEFAULT_DAYS_COUNT_FOR_DAILY_FORECAST);
		case THREE_HOURLY_WEATHER_FORECAST:
			return openWeatherMapUrl
					.generateThreeHourlyWeatherForecastUrl(cityId);
		default:
			throw new IllegalWeatherInfoTypeArgumentException(this);
		}
	}

	protected WeatherInformation getWeatherInformation(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, clazz);
	}

	public static final Parcelable.Creator<WeatherInfoType> CREATOR = new Parcelable.Creator<WeatherInfoType>() {

		@Override
		public WeatherInfoType createFromParcel(Parcel in) {
			WeatherInfoType weatherInfoType;
			try {
				weatherInfoType = valueOf(in.readString());
			} catch (IllegalArgumentException ex) {
				weatherInfoType = null;
			}
			return weatherInfoType;
		}

		@Override
		public WeatherInfoType[] newArray(int size) {
			return new WeatherInfoType[size];
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

	public static class IllegalWeatherInfoTypeArgumentException extends
			IllegalArgumentException {

		private static final long serialVersionUID = -3666143975910277111L;

		public IllegalWeatherInfoTypeArgumentException(
				WeatherInfoType weatherInfoType) {
			super("Unsupported weatherInfoType: " + weatherInfoType);
		}

	}

}
