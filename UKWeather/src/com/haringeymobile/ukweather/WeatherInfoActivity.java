package com.haringeymobile.ukweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class WeatherInfoActivity extends ActionBarActivity {

	private static final String DUAL_PANE = "dual_pane";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isDualPane = DUAL_PANE.equals(getResources().getString(
				R.string.weather_info_frame_layout_pane_number_tag)) ? true
				: false;
		if (isDualPane) {
			finish();
			return;
		}

		setContentView(R.layout.activity_weather_info);

		Intent intent = getIntent();
		WeatherInfoType weatherInfoType = intent
				.getParcelableExtra(MainActivity.WEATHER_INFORMATION_TYPE);
		setActionBar(weatherInfoType);

		String jsonString = intent
				.getStringExtra(MainActivity.WEATHER_INFO_JSON_STRING);
		addRequiredFragment(weatherInfoType, jsonString);
	}

	private void setActionBar(WeatherInfoType weatherInfoType) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(weatherInfoType.getLabelResourceId());
		actionBar.setIcon(weatherInfoType.getIconResourceId());
	}

	private void addRequiredFragment(WeatherInfoType weatherInfoType,
			String jsonString) {
		Fragment fragment;
		if (weatherInfoType == WeatherInfoType.CURRENT_WEATHER) {
			fragment = WeatherInfoFragment.newInstance(weatherInfoType, null,
					jsonString);
		} else {
			fragment = WeatherForecastParentFragment.newInstance(
					weatherInfoType, jsonString);
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.weather_info_container, fragment);
		fragmentTransaction.commit();
	}

}
