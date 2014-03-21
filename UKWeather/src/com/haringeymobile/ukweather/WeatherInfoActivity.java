package com.haringeymobile.ukweather;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.haringeymobile.ukweather.data.CityUK;

public class WeatherInfoActivity extends ActionBarActivity {

	private static final String DUAL_PANE = "dual_pane";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isDualPane = DUAL_PANE.equals(getResources().getString(
				R.string.weather_info_fragment_pane_number_tag)) ? true : false;
		if (isDualPane) {
			finish();
			return;
		}

		setContentView(R.layout.activity_weather_info);
		CityUK city = getIntent().getParcelableExtra(MainActivity.CITY);
		WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) getSupportFragmentManager()
				.findFragmentById(R.id.weather_info_container);
		weatherInfoFragment.updateWeatherInfo(city);
	}

}
