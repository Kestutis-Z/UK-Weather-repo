package com.haringeymobile.ukweather.database;

import android.app.IntentService;
import android.content.Intent;

import com.haringeymobile.ukweather.MainActivity;
import com.haringeymobile.ukweather.WeatherInfoType;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class GeneralDatabaseService extends IntentService {

	private static final String APP_PACKAGE = "com.haringeymobile.ukweather";
	public static final String ACTION_UPDATE_WEATHER_INFO = APP_PACKAGE	+ ".update_weather_info_records";
	public static final String ACTION_DELETE_CITY_RECORDS = APP_PACKAGE	+ ".delete_city_records";

	private static final String WORKER_THREAD_NAME = "General database service thread";

	public GeneralDatabaseService() {
		super(WORKER_THREAD_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String action = intent.getAction();
		if (ACTION_UPDATE_WEATHER_INFO.equals(action)) {
			int cityId = SharedPrefsHelper.getCityIdFromSharedPrefs(this);
			String jsonString = intent
					.getStringExtra(MainActivity.WEATHER_INFO_JSON_STRING);
			WeatherInfoType weatherInfoType = intent
					.getParcelableExtra(MainActivity.WEATHER_INFORMATION_TYPE);
			new SqlOperation(this, weatherInfoType).updateWeatherInfo(cityId,
					jsonString);
		} else if (ACTION_DELETE_CITY_RECORDS.equals(action)) {
			int cityId = intent.getIntExtra(MainActivity.CITY_ID,
					CityTable.CITY_ID_DOES_NOT_EXIST);
			new SqlOperation(this, WeatherInfoType.CURRENT_WEATHER)
					.deleteCity(cityId);
		} else {
			throw new IllegalArgumentException("Unsupported action: " + action);
		}
	}

}
