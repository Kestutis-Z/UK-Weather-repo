package com.haringeymobile.ukweather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.InitialCity;
import com.haringeymobile.ukweather.data.JsonParser;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlUsingHttpConnection;
import com.haringeymobile.ukweather.data.objects.CityCurrentWeather;
import com.haringeymobile.ukweather.data.objects.NumericParameters;
import com.haringeymobile.ukweather.data.objects.Weather;
import com.haringeymobile.ukweather.data.objects.Wind;
import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.SQLOperation;
import com.haringeymobile.ukweather.utils.MiscMethods;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class WeatherInfoFragment extends Fragment {

	private static final String SEPARATOR = ": ";
	private static final String PERCENT_SIGN = "%";
	private static final String HECTOPASCAL = "hPa";

	private Activity parentActivity;
	private TextView nameTextView;
	private TextView conditionsTextView;
	private ImageView conditionsImageView;
	private TextView temperatureTextView;
	private TextView pressureTextView;
	private TextView humidityTextView;
	private TextView windTextView;

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
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int cityId = SharedPrefsHelper.getIntFromSharedPrefs(parentActivity,
				CityListFragment.LAST_SELECTED_CITY_ID,
				InitialCity.LONDON.getOpenWeatherMapId());
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
			AsyncTask<Integer, Void, CityCurrentWeather> {

		ProgressDialog progressDialog;
		Resources res = parentActivity.getResources();
		Drawable iconDrawable;

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
		protected CityCurrentWeather doInBackground(Integer... params) {
			int cityId = params[0];
			SQLOperation sqlOperation = new SQLOperation(parentActivity);
			String jsonString = sqlOperation
					.getJSONStringForCurrentWeather(cityId);
			if (jsonString == null) {
				jsonString = getJSONStringFromWebService(cityId);
			}
			if (jsonString == null) {
				return null;
			} else {
				Gson gson = new Gson();
				CityCurrentWeather cityCurrentWeather = gson.fromJson(
						jsonString, CityCurrentWeather.class);
				setWeatherIconDrawable(cityCurrentWeather);
				sqlOperation.updateCurrentWeather(cityId, jsonString);
				return cityCurrentWeather;
			}
		}

		private String getJSONStringFromWebService(int cityId) {
			String jsonString;
			JsonParser jsonParser = new JsonParser();
			jsonParser
					.setJsonParsingStrategy(new JsonParsingFromUrlUsingHttpConnection());
			jsonString = jsonParser.getJsonString(cityId);
			return jsonString;
		}

		private void setWeatherIconDrawable(
				CityCurrentWeather cityCurrentWeather) {
			String iconName = cityCurrentWeather.getWeather().get(0).getIcon();
			iconDrawable = getIconDrawable(iconName);
		}

		private Drawable getIconDrawable(String icon) {
			String iconUrl = Weather.ICON_URL_PREFIX + icon;
			try {
				URL url = new URL(iconUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				Drawable d = new BitmapDrawable(res, myBitmap);
				return d;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(CityCurrentWeather cityWeather) {
			super.onPostExecute(cityWeather);
			progressDialog.dismiss();
			if (cityWeather == null) {
				if (parentActivity != null) {
					Toast.makeText(parentActivity, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
				return;
			}
			nameTextView.setText(cityWeather.getCityName());
			displayConditions(cityWeather.getWeather());
			displayWeatherNumericParametersText(cityWeather
					.getNumericParameters());
			displayWindInfo(cityWeather.getWind());
			if (parentActivity != null) {
				ScrollView weatherInfoScrollView = ((ScrollView) parentActivity
						.findViewById(R.id.weather_info_scroll_view));
				if (weatherInfoScrollView != null) {
					weatherInfoScrollView.setVisibility(View.VISIBLE);
				}
			}
		}

		private void displayConditions(List<Weather> weatherList) {
			Weather weather = weatherList.get(0);
			String weatherDescription = weather.getType() + " ("
					+ weather.getDescription() + ")";
			conditionsTextView.setText(weatherDescription);
			conditionsImageView.setImageDrawable(iconDrawable);
		}

		private void displayWeatherNumericParametersText(
				NumericParameters numericParameters) {
			displayTemperatureText(numericParameters);
			displayAtmosphericPressureText(numericParameters);
			displayHumidity(numericParameters);
		}

		private void displayTemperatureText(NumericParameters numericParameters) {
			String temperatureInfo = MiscMethods
					.formatDoubleValue(numericParameters.getTemperature())
					+ res.getString(R.string.weather_info_degree_celcius);
			temperatureTextView.setText(temperatureInfo);
		}

		private void displayAtmosphericPressureText(
				NumericParameters numericParameters) {
			String pressureInfo = res
					.getString(R.string.weather_info_atmospheric_pressure)
					+ SEPARATOR
					+ numericParameters.getPressure()
					+ " "
					+ HECTOPASCAL;
			pressureTextView.setText(pressureInfo);
		}

		private void displayHumidity(NumericParameters numericParameters) {
			String humidityInfo = res.getString(R.string.weather_info_humidity)
					+ SEPARATOR + numericParameters.getHumidity()
					+ PERCENT_SIGN;
			humidityTextView.setText(humidityInfo);
		}

		private void displayWindInfo(Wind wind) {
			String windInfo = res.getString(R.string.weather_info_wind_speed)
					+ SEPARATOR + wind.getSpeedInMilesPerSecond() + " "
					+ res.getString(R.string.miles_per_second);
			windInfo += "\n"
					+ res.getString(R.string.weather_info_wind_direction)
					+ SEPARATOR + wind.getDirectionInDegrees()
					+ res.getString(R.string.weather_info_degree);
			windInfo += " ("
					+ res.getString(wind.getCardinalDirectionStringResource())
					+ ")";
			windTextView.setText(windInfo);
		}

	}

}
