package com.haringeymobile.ukweather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.haringeymobile.ukweather.WeatherInfoType.IllegalWeatherInfoTypeArgumentException;
import com.haringeymobile.ukweather.data.objects.CityDailyWeatherForecast;
import com.haringeymobile.ukweather.data.objects.CityThreeHourlyWeatherForecast;
import com.haringeymobile.ukweather.data.objects.SearchResponseForDailyForecastQuery;
import com.haringeymobile.ukweather.data.objects.SearchResponseForThreeHourlyForecastQuery;

public class WeatherForecastParentFragment extends Fragment {

	private static final String WEATHER_INFORMATION_TYPE = "weather info type";
	private static final String WEATHER_INFO_JSON_STRING = "forecast json";

	private FragmentActivity parentActivity;
	private PagerSlidingTabStrip pagerSlidingTabStrip;
	private ViewPager viewPager;
	private WeatherForecastPagerAdapter pagerAdapter;
	private WeatherInfoType weatherInfoType;
	private String cityName;
	private List<String> jsonStringsForChildFragments;

	public static WeatherForecastParentFragment newInstance(
			WeatherInfoType weatherInfoType, String jsonString) {
		WeatherForecastParentFragment fragment = new WeatherForecastParentFragment();
		Bundle args = new Bundle();
		args.putParcelable(WEATHER_INFORMATION_TYPE,
				(Parcelable) weatherInfoType);
		args.putString(WEATHER_INFO_JSON_STRING, jsonString);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (FragmentActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		weatherInfoType = args.getParcelable(WEATHER_INFORMATION_TYPE);
		setJsonStringsForChildFragments(args);
	}

	private void setJsonStringsForChildFragments(Bundle args) {
		String jsonString = args.getString(WEATHER_INFO_JSON_STRING);
		Gson gson = new Gson();
		jsonStringsForChildFragments = new ArrayList<>();
		if (weatherInfoType == WeatherInfoType.DAILY_WEATHER_FORECAST) {
			setDailyWeatherJsonStrings(jsonString, gson);
		} else if (weatherInfoType == WeatherInfoType.THREE_HOURLY_WEATHER_FORECAST) {
			setThreeHourlyWeatherJsonStrings(jsonString, gson);
		} else {
			throw new IllegalWeatherInfoTypeArgumentException(weatherInfoType);
		}
	}

	private void setDailyWeatherJsonStrings(String jsonString, Gson gson) {
		SearchResponseForDailyForecastQuery searchResponseForDailyForecastQuery = gson
				.fromJson(jsonString, SearchResponseForDailyForecastQuery.class);
		cityName = searchResponseForDailyForecastQuery.getCityInfo()
				.getCityName();
		List<CityDailyWeatherForecast> dailyForecasts = searchResponseForDailyForecastQuery
				.getDailyWeatherForecasts();
		for (CityDailyWeatherForecast forecast : dailyForecasts) {
			jsonStringsForChildFragments.add(gson.toJson(forecast));
		}
	}

	private void setThreeHourlyWeatherJsonStrings(String jsonString, Gson gson) {
		SearchResponseForThreeHourlyForecastQuery searchResponseForThreeHourlyForecastQuery = gson
				.fromJson(jsonString,
						SearchResponseForThreeHourlyForecastQuery.class);
		cityName = searchResponseForThreeHourlyForecastQuery.getCityInfo()
				.getCityName();
		List<CityThreeHourlyWeatherForecast> threeHourlyForecasts = searchResponseForThreeHourlyForecastQuery
				.getThreeHourlyWeatherForecasts();
		for (CityThreeHourlyWeatherForecast forecast : threeHourlyForecasts) {
			jsonStringsForChildFragments.add(gson.toJson(forecast));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sliding_tab_host, container,
				false);
		pagerSlidingTabStrip = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		viewPager = (ViewPager) view.findViewById(R.id.pager);
		pagerAdapter = new WeatherForecastPagerAdapter(
				parentActivity.getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		pagerSlidingTabStrip.setViewPager(viewPager);

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
	}

	private class WeatherForecastPagerAdapter extends FragmentStatePagerAdapter {

		public WeatherForecastPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (weatherInfoType == WeatherInfoType.DAILY_WEATHER_FORECAST) {
				long time = 1000 * new Gson().fromJson(
						jsonStringsForChildFragments.get(position),
						CityDailyWeatherForecast.class).getDate();
				return android.text.format.DateFormat.getDateFormat(
						getActivity()).format(new Date(time));
			} else if (weatherInfoType == WeatherInfoType.THREE_HOURLY_WEATHER_FORECAST) {
				long time = 1000 * new Gson().fromJson(
						jsonStringsForChildFragments.get(position),
						CityThreeHourlyWeatherForecast.class).getDate();
				return android.text.format.DateFormat.getTimeFormat(
						getActivity()).format(new Date(time));
			} else {
				throw new IllegalWeatherInfoTypeArgumentException(
						weatherInfoType);
			}
		}

		@Override
		public int getCount() {
			return jsonStringsForChildFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return WeatherInfoFragment.newInstance(weatherInfoType, cityName,
					jsonStringsForChildFragments.get(position));
		}

	}

}
