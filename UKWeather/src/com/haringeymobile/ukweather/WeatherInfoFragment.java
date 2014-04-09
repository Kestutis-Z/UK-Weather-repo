package com.haringeymobile.ukweather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haringeymobile.ukweather.WeatherInfoType.IllegalWeatherInfoTypeArgumentException;
import com.haringeymobile.ukweather.data.objects.Weather;
import com.haringeymobile.ukweather.data.objects.WeatherInformation;
import com.haringeymobile.ukweather.data.objects.Wind;
import com.haringeymobile.ukweather.utils.MiscMethods;

public abstract class WeatherInfoFragment extends Fragment {

	protected static final String SEPARATOR = ": ";
	protected static final String PERCENT_SIGN = "%";
	protected static final String HECTOPASCAL = "hPa";
	protected static final String JSON_STRING = "json string";
	protected static final String CITY_NAME = "city name";

	protected TextView extraInfoTextView;
	protected TextView conditionsTextView;
	protected ImageView conditionsImageView;
	protected TextView temperatureTextView;
	protected TextView pressureTextView;
	protected TextView humidityTextView;
	protected TextView windTextView;

	protected Resources res;

	public static WeatherInfoFragment newInstance(
			WeatherInfoType weatherInfoType, String cityName, String jsonString) {
		WeatherInfoFragment weatherInfoFragment;
		switch (weatherInfoType) {
		case CURRENT_WEATHER:
			weatherInfoFragment = new WeatherCurrentInfoFragment();
			break;
		case DAILY_WEATHER_FORECAST:
			weatherInfoFragment = new WeatherDailyWeatherForecastChildFragment();
			break;
		case THREE_HOURLY_WEATHER_FORECAST:
			weatherInfoFragment = new WeatherThreeHourlyForecastChildFragment();
			break;
		default:
			throw new IllegalWeatherInfoTypeArgumentException(weatherInfoType);
		}
		Bundle args = new Bundle();
		args.putString(CITY_NAME, cityName);
		args.putString(JSON_STRING, jsonString);
		weatherInfoFragment.setArguments(args);
		return weatherInfoFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		res = activity.getResources();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_common_weather_info,
				container, false);
		initializeCommonViews(view);
		return view;
	}

	protected void initializeCommonViews(View view) {
		extraInfoTextView = (TextView) view
				.findViewById(R.id.city_extra_info_text_view);
		conditionsTextView = (TextView) view
				.findViewById(R.id.weather_conditions_text_view);
		conditionsImageView = (ImageView) view
				.findViewById(R.id.weather_conditions_image_view);
		temperatureTextView = (TextView) view
				.findViewById(R.id.temperature_text_view);
		pressureTextView = (TextView) view
				.findViewById(R.id.atmospheric_pressure_text_view);
		humidityTextView = (TextView) view
				.findViewById(R.id.humidity_text_view);
		windTextView = (TextView) view.findViewById(R.id.wind_text_view);
	}

	public void displayWeather(WeatherInformation weatherInformation) {
		displayExtraInfo(weatherInformation);
		displayConditions(weatherInformation);
		displayWeatherNumericParametersText(weatherInformation);
		displayWindInfo(weatherInformation);
	}

	protected abstract void displayExtraInfo(
			WeatherInformation weatherInformation);

	private void displayConditions(WeatherInformation weatherInformation) {
		String weatherDescription = weatherInformation.getType() + " ("
				+ weatherInformation.getDescription() + ")";
		conditionsTextView.setText(weatherDescription);
		new SetIconDrawableTask().execute(weatherInformation.getIconName());
	}

	private void displayWeatherNumericParametersText(
			WeatherInformation weatherInformation) {
		displayTemperatureText(weatherInformation);
		displayAtmosphericPressureText(weatherInformation);
		displayHumidity(weatherInformation);
	}

	private void displayTemperatureText(WeatherInformation weatherInformation) {
		String temperatureInfo = MiscMethods
				.formatDoubleValue(weatherInformation.getDayTemperature())
				+ res.getString(R.string.weather_info_degree_celcius);
		temperatureTextView.setText(temperatureInfo);
	}

	private void displayAtmosphericPressureText(
			WeatherInformation weatherInformation) {
		String pressureInfo = res
				.getString(R.string.weather_info_atmospheric_pressure)
				+ SEPARATOR
				+ weatherInformation.getPressure()
				+ " "
				+ HECTOPASCAL;
		pressureTextView.setText(pressureInfo);
	}

	private void displayHumidity(WeatherInformation weatherInformation) {
		String humidityInfo = res.getString(R.string.weather_info_humidity)
				+ SEPARATOR + weatherInformation.getHumidity() + PERCENT_SIGN;
		humidityTextView.setText(humidityInfo);
	}

	private void displayWindInfo(WeatherInformation weatherInformation) {
		Wind wind = weatherInformation.getWind();
		String windInfo = res.getString(R.string.weather_info_wind_speed)
				+ SEPARATOR + wind.getSpeedInMilesPerSecond() + " "
				+ res.getString(R.string.miles_per_second);
		windInfo += "\n" + res.getString(R.string.weather_info_wind_direction)
				+ SEPARATOR + wind.getDirectionInDegrees()
				+ res.getString(R.string.weather_info_degree);
		windInfo += " ("
				+ res.getString(wind.getCardinalDirectionStringResource())
				+ ")";
		windTextView.setText(windInfo);
	}

	private class SetIconDrawableTask extends AsyncTask<String, Void, Drawable> {

		@Override
		protected Drawable doInBackground(String... args) {
			String iconUrl = Weather.ICON_URL_PREFIX + args[0];
			try {
				URL url = new URL(iconUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				if (getActivity() == null) {
					return null;
				}
				Drawable drawable = new BitmapDrawable(getActivity()
						.getResources(), myBitmap);
				return drawable;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			if (conditionsImageView != null) {
				conditionsImageView.setImageDrawable(result);
			}
		}

	}

}
