package com.haringeymobile.ukweather;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivityPreHoneycomb extends PreferenceActivity {

	public static final String PREF_TEMPERATURE_SCALE = "temperature_scale";
	public static final String PREF_WIND_SPEED_MEASUREMENT_UNIT = "wind_speed_measurement_unit";
	public static final String PREF_DATA_CACHE_PERIOD = "data_cache_period";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}
}
