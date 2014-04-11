package com.haringeymobile.ukweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class CityListFragmentWithWeatherButtons extends
		BaseCityListFragmentWithButtons {

	public interface Listener {

		public void onCityWeatherInfoRequested(int cityId,
				WeatherInfoType weatherInfoType);

	}

	private Listener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Listener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		boolean isDualPane = (FrameLayout) parentActivity
				.findViewById(R.id.weather_info_container) != null;
		if (isDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			int lastCityId = SharedPrefsHelper
					.getCityIdFromSharedPrefs(parentActivity);
			if (lastCityId != CityTable.CITY_ID_DOES_NOT_EXIST) {
				WeatherInfoType lastWeatherInfoType = SharedPrefsHelper
						.getLastWeatherInfoTypeFromSharedPrefs(parentActivity);
				listener.onCityWeatherInfoRequested(lastCityId,
						lastWeatherInfoType);
			}
		}
	}

	protected int getRowLayoutId() {
		return R.layout.row_city_list_with_weather_buttons;
	}

	protected BaseCityCursorAdapter getCityCursorAdapter() {
		return new CityWeatherCursorAdapter(parentActivity, getRowLayoutId(),
				null, COLUMNS_TO_DISPLAY, TO, 0, this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(View view) {
		int position = getListView().getPositionForView(view);
		int cityId = cursorAdapter.getCityId(position);
		WeatherInfoType weatherInfoType;
		int viewId = view.getId();
		switch (viewId) {
		case R.id.city_current_weather_button:
			weatherInfoType = WeatherInfoType.CURRENT_WEATHER;
			break;
		case R.id.city_daily_weather_forecast_button:
			weatherInfoType = WeatherInfoType.DAILY_WEATHER_FORECAST;
			break;
		case R.id.city_three_hourly_weather_forecast_button:
			weatherInfoType = WeatherInfoType.THREE_HOURLY_WEATHER_FORECAST;
			break;
		default:
			throw new IllegalArgumentException("Not supported view ID: "
					+ viewId);
		}
		SharedPrefsHelper.putCityIdIntoSharedPrefs(parentActivity, cityId);
		SharedPrefsHelper.putLastWeatherInfoTypeIntoSharedPrefs(parentActivity,
				weatherInfoType);
		listener.onCityWeatherInfoRequested(cityId, weatherInfoType);
	}

}
