package com.haringeymobile.ukweather.utils;

import com.haringeymobile.ukweather.WeatherInfoType;
import com.haringeymobile.ukweather.database.CityTable;

import android.app.Activity;
import android.content.Context;

public class SharedPrefsHelper {

	public static final String SHARED_PREFS_KEY = "com.haringeymobile.ukweather.PREFERENCE_FILE_KEY";

	private static final String LAST_SELECTED_CITY_ID = "city id";
	private static final String LAST_SELECTED_WEATHER_INFO_TYPE = "weather info type";

	public static int getCityIdFromSharedPrefs(Context context) {
		return context.getSharedPreferences(SHARED_PREFS_KEY,
				Activity.MODE_PRIVATE).getInt(LAST_SELECTED_CITY_ID,
				CityTable.CITY_ID_DOES_NOT_EXIST);
	}

	public static void putCityIdIntoSharedPrefs(Context context, int cityId) {
		context.getSharedPreferences(SHARED_PREFS_KEY, Activity.MODE_PRIVATE)
				.edit().putInt(LAST_SELECTED_CITY_ID, cityId).apply();
	}

	public static WeatherInfoType getLastWeatherInfoTypeFromSharedPrefs(
			Context context) {
		int lastSelectedWeatherInfoTypeId = context.getSharedPreferences(
				SHARED_PREFS_KEY, Activity.MODE_PRIVATE).getInt(
				LAST_SELECTED_WEATHER_INFO_TYPE,
				WeatherInfoType.CURRENT_WEATHER.getId());
		return WeatherInfoType.getTypeById(lastSelectedWeatherInfoTypeId);
	}

	public static void putLastWeatherInfoTypeIntoSharedPrefs(Context context,
			WeatherInfoType weatherInfoType) {
		context.getSharedPreferences(SHARED_PREFS_KEY, Activity.MODE_PRIVATE)
				.edit()
				.putInt(LAST_SELECTED_WEATHER_INFO_TYPE,
						weatherInfoType.getId()).apply();
	}

}
