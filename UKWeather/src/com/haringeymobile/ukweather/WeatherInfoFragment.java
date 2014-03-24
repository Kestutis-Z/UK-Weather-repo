package com.haringeymobile.ukweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haringeymobile.ukweather.data.CityUK;
import com.haringeymobile.ukweather.data.JSONConverter;
import com.haringeymobile.ukweather.data.JSONRetriever;
import com.haringeymobile.ukweather.data.JSONRetrievingFromURLStrategy_1;
import com.haringeymobile.ukweather.data.LocationCoordinates;
import com.haringeymobile.ukweather.data.Temperature;
import com.haringeymobile.ukweather.data.WeatherConditions;
import com.haringeymobile.ukweather.data.WeatherInformation;
import com.haringeymobile.ukweather.data.WeatherNumericParameters;
import com.haringeymobile.ukweather.datastorage.CityTable;
import com.haringeymobile.ukweather.utils.MiscMethods;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class WeatherInfoFragment extends Fragment {

	private static final String SEPARATOR = ": ";
	private static final String DASH = " - ";
	private static final String PERCENT_SIGN = "%";
	private Activity parentActivity;
	private TextView nameTextView;
	private TextView locationTextView;
	private TextView conditionsTextView;
	private ImageView conditionsImageView;
	private TextView temperatureTextView;
	private TextView temperatureRangeTextView;
	private TextView pressureTextView;
	private TextView humidityTextView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weather_info, container,
				false);
		nameTextView = (TextView) view.findViewById(R.id.city_name_text_view);
		locationTextView = (TextView) view
				.findViewById(R.id.city_location_text_view);
		conditionsTextView = (TextView) view
				.findViewById(R.id.weather_conditions_text_view);
		conditionsImageView = (ImageView) view
				.findViewById(R.id.weather_conditions_image_view);
		temperatureTextView = (TextView) view
				.findViewById(R.id.temperature_text_view);
		temperatureRangeTextView = (TextView) view
				.findViewById(R.id.temperature_range_text_view);
		pressureTextView = (TextView) view
				.findViewById(R.id.atmospheric_pressure_text_view);
		humidityTextView = (TextView) view
				.findViewById(R.id.humidity_text_view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int cityId = SharedPrefsHelper.getIntFromSharedPrefs(parentActivity,
				CityListFragment.LAST_SELECTED_CITY_ID,
				CityUK.LONDON.getOpenWeatherMapId());
		if (cityId != CityTable.CITY_ID_DOES_NOT_EXIST) {
			updateWeatherInfo(cityId);
		}
	}

	public void updateWeatherInfo(int cityId) {
		new WeatherInformationRetrieverTask().execute(cityId);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
	}

	private class WeatherInformationRetrieverTask extends
			AsyncTask<Integer, Integer, WeatherInformation> {

		ProgressDialog progressDialog;
		Resources res = parentActivity.getResources();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parentActivity);
			progressDialog.setMessage(parentActivity.getResources().getString(
					R.string.loading_message));
			progressDialog.setIndeterminate(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected WeatherInformation doInBackground(Integer... params) {
			int cityId = params[0];
			JSONRetriever jsonRetriever = new JSONRetriever();
			jsonRetriever
					.setHttpCallsHandlingStrategy(new JSONRetrievingFromURLStrategy_1());
			String jsonString = jsonRetriever.getJSONString(cityId);
			if (jsonString == null) {
				return null;
			} else {
				WeatherInformation weatherInformation = new JSONConverter()
						.convert(jsonString, res);
				return weatherInformation;
			}
		}

		@Override
		protected void onPostExecute(WeatherInformation weatherInformation) {
			super.onPostExecute(weatherInformation);
			progressDialog.dismiss();
			if (weatherInformation == null) {
				if (parentActivity != null) {
					Toast.makeText(parentActivity, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
				return;
			}
			nameTextView.setText(weatherInformation.getCityName());
			displayLocationText(weatherInformation.getCoordinates());
			displayConditions(weatherInformation.getWeatherConditions());
			displayWeatherNumericParametersText(weatherInformation
					.getWeatherNumericParameters());
			if (parentActivity != null) {
				ScrollView weatherInfoScrollView = ((ScrollView) parentActivity
						.findViewById(R.id.weather_info_scroll_view));
				if (weatherInfoScrollView != null) {
					weatherInfoScrollView.setVisibility(View.VISIBLE);
				}
			}
		}

		private void displayLocationText(LocationCoordinates coordinates) {
			String locationInfo = res
					.getString(R.string.weather_info_longitude)
					+ SEPARATOR
					+ coordinates.getLongitude();
			locationInfo += ", ";
			locationInfo += res.getString(R.string.weather_info_latitude)
					+ SEPARATOR + coordinates.getLatitude();
			locationTextView.setText(locationInfo);
		}

		private void displayConditions(WeatherConditions weatherConditions) {
			String conditionInfo =
			/*
			 * res.getString(R.string.weather_info_conditions) + SEPARATOR +
			 */
			weatherConditions.getConditionName();
			conditionsTextView.setText(conditionInfo);
			Drawable icon = weatherConditions.getIcon();
			conditionsImageView.setImageDrawable(icon);
		}

		private void displayWeatherNumericParametersText(
				WeatherNumericParameters weatherNumericParameters) {
			displayTemperatureText(weatherNumericParameters.getTemperature());
			displayAtmosphericPressureText(weatherNumericParameters
					.getPressure());
			displayHumidity(weatherNumericParameters.getHumidity());
		}

		private void displayTemperatureText(Temperature temperature) {
			String temperatureInfo = MiscMethods.formatDoubleValue(temperature
					.getMain())
					+ res.getString(R.string.weather_info_degree_celcius);
			temperatureTextView.setText(temperatureInfo);

			String temperatureRangeInfo = res
					.getString(R.string.weather_info_temperature_range)
					+ SEPARATOR
					+ getTemperatureRangeText(temperature)
					+ res.getString(R.string.weather_info_degree_celcius);
			temperatureRangeTextView.setText(temperatureRangeInfo);
		}

		private String getTemperatureRangeText(Temperature temperature) {
			return MiscMethods.formatDoubleValue(temperature.getMinimum())
					+ DASH
					+ MiscMethods.formatDoubleValue(temperature.getMaximum());
		}

		private void displayAtmosphericPressureText(int pressure) {
			String pressureInfo = res
					.getString(R.string.weather_info_atmospheric_pressure)
					+ SEPARATOR + pressure;
			pressureTextView.setText(pressureInfo);
		}

		private void displayHumidity(int humidity) {
			String humidityInfo = res.getString(R.string.weather_info_humidity)
					+ SEPARATOR + humidity + PERCENT_SIGN;
			humidityTextView.setText(humidityInfo);
		}

	}

}
