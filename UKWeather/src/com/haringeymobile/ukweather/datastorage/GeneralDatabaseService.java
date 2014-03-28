package com.haringeymobile.ukweather.datastorage;

import android.app.IntentService;
import android.content.Intent;

import com.haringeymobile.ukweather.MainActivity;

public class GeneralDatabaseService extends IntentService {

	private static final String APP_PACKAGE = "com.haringeymobile.ukweather";
	public static final String ACTION_DELETE_CITY_RECORDS = APP_PACKAGE
			+ ".delete_city_records";

	private static final String WORKER_THREAD_NAME = "General database service thread";

	public GeneralDatabaseService() {
		super(WORKER_THREAD_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (ACTION_DELETE_CITY_RECORDS.equals(action)) {
			int cityId = intent.getIntExtra(MainActivity.CITY_ID,
					CityTable.CITY_ID_DOES_NOT_EXIST);
			new SQLOperation(this).deleteCity(cityId);
		} else {
			throw new IllegalArgumentException("Unsupported action: " + action);
		}
	}

}
