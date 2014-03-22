package com.haringeymobile.ukweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.haringeymobile.ukweather.data.CityUK;
import com.haringeymobile.ukweather.data.JSONConverter;
import com.haringeymobile.ukweather.data.JSONRetrievingFromURLStrategy_1;
import com.haringeymobile.ukweather.data.JSONRetriever;
import com.haringeymobile.ukweather.data.LocationCoordinates;
import com.haringeymobile.ukweather.data.Temperature;
import com.haringeymobile.ukweather.data.WeatherConditions;
import com.haringeymobile.ukweather.data.WeatherInformation;
import com.haringeymobile.ukweather.data.WeatherNumericParameters;
import com.haringeymobile.ukweather.utils.MiscMethods;

public class WeatherInfoFragment extends Fragment {

	private static final String SEPARATOR = ": ";
	private static final String DASH = " - ";
	private static final String PERCENT_SIGN = "%";
	private Activity parentActivity;
	private TextView nameTextView;
	private TextView locationTextView;
	private TextView conditionsTextView;
	private TextView temperatureTextView;
	private TextView pressureTextView;
	private TextView humidityTextView;

	static final WeatherInfoFragment newInstance(CityUK city) {
		WeatherInfoFragment solutionFragment = new WeatherInfoFragment();
		Bundle args = new Bundle();
		args.putParcelable(MainActivity.CITY, city);
		solutionFragment.setArguments(args);
		return solutionFragment;
	}

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
		temperatureTextView = (TextView) view
				.findViewById(R.id.temperature_text_view);
		pressureTextView = (TextView) view
				.findViewById(R.id.atmospheric_pressure_text_view);
		humidityTextView = (TextView) view
				.findViewById(R.id.humidity_text_view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			CityUK city = getArguments().getParcelable(MainActivity.CITY);
			if (city != null) {
				updateWeatherInfo(city);
			}
		}
	}

	public void updateWeatherInfo(CityUK city) {
		new WeatherInformationRetrieverTask().execute(city);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
	}

	private class WeatherInformationRetrieverTask extends
			AsyncTask<CityUK, Integer, WeatherInformation> {

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
		protected WeatherInformation doInBackground(CityUK... params) {
			CityUK city = params[0];
			JSONRetriever jsonRetriever = new JSONRetriever();
			jsonRetriever
					.setHttpCallsHandlingStrategy(new JSONRetrievingFromURLStrategy_1());
			String jsonString = jsonRetriever.getJSONString(city
					.getOpenWeatherMapSearchName());
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
		}

		private void displayLocationText(LocationCoordinates coordinates) {
			String locationInfo = res
					.getString(R.string.weather_info_longitude)
					+ SEPARATOR
					+ coordinates.getLongitude();
			locationInfo += "\n";
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
			conditionsTextView.setCompoundDrawablesWithIntrinsicBounds(
					weatherConditions.getIcon(), null, null, null);
		}

		private void displayWeatherNumericParametersText(
				WeatherNumericParameters weatherNumericParameters) {
			displayTemperatureText(weatherNumericParameters.getTemperature());
			displayAtmosphericPressureText(weatherNumericParameters
					.getPressure());
			displayHumidity(weatherNumericParameters.getHumidity());
		}

		private void displayTemperatureText(Temperature temperature) {
			String temperatureInfo = res
					.getString(R.string.weather_info_temperature)
					+ SEPARATOR
					+ MiscMethods.formatDoubleValue(temperature.getMain());
			temperatureInfo += "\n";
			temperatureInfo += res
					.getString(R.string.weather_info_temperature_range)
					+ SEPARATOR + getTemperatureRangeText(temperature);
			temperatureTextView.setText(temperatureInfo);

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
