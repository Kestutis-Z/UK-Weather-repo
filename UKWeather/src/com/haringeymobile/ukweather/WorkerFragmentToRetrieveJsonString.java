package com.haringeymobile.ukweather;

import java.io.IOException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.haringeymobile.ukweather.data.JsonFetcher;
import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.SqlOperation;
import com.haringeymobile.ukweather.utils.AsyncTaskWithProgressBar;
import com.haringeymobile.ukweather.utils.MiscMethods;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

/**
 * A fragment to asynchronously obtain the specified JSON weather data. This
 * fragment has no UI, and simply acts as an executor of the
 * {@link RetrieveWeatherInformationJsonStringTask}.
 */
public class WorkerFragmentToRetrieveJsonString extends Fragment {

	/** A listener for the requested JSON weather data retrieval. */
	public interface OnJsonStringRetrievedListener {

		/**
		 * Reacts to the JSON weather information retrieval.
		 * 
		 * @param jsonString
		 *            JSON weather data
		 * @param weatherInfoType
		 *            a kind of weather information
		 * @param shouldSaveLocally
		 *            whether the retrieved data should be saved in the database
		 */
		public abstract void onJsonStringRetrieved(String jsonString,
				WeatherInfoType weatherInfoType, boolean shouldSaveLocally);

	}

	private Activity parentActivity;
	private OnJsonStringRetrievedListener callbackToParentActivity;
	/** An Open Weather Map city ID. */
	private int cityId;
	private WeatherInfoType weatherInfoType;
	private RetrieveWeatherInformationJsonStringTask retrieveWeatherInformationJsonStringTask;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
		try {
			callbackToParentActivity = (OnJsonStringRetrievedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Listener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Repeats the last weather data request, i.e. retrieves the weather
	 * information using parameters (city and information type) used for last
	 * attempt to obtain weather information.
	 */
	void retrieveLastRequestedWeatherInfo() {
		int lastCityId = SharedPrefsHelper
				.getCityIdFromSharedPrefs(parentActivity);
		if (lastCityId != CityTable.CITY_ID_DOES_NOT_EXIST) {
			WeatherInfoType lastWeatherInfoType = SharedPrefsHelper
					.getLastWeatherInfoTypeFromSharedPrefs(parentActivity);
			retrieveWeatherInfoJsonString(lastCityId, lastWeatherInfoType);
		}
	}

	/**
	 * Starts an {@link AsyncTask} to obtain the requested JSON weather data.
	 * 
	 * @param cityId
	 *            an Open Weather Map city ID
	 * @param weatherInfoType
	 *            a type of the requested weather data
	 */
	void retrieveWeatherInfoJsonString(int cityId,
			WeatherInfoType weatherInfoType) {
		if (MiscMethods.isUserOnline(parentActivity)) {
			this.cityId = cityId;
			this.weatherInfoType = weatherInfoType;

			URL openWeatherMapUrl = weatherInfoType
					.getOpenWeatherMapUrl(cityId);
			retrieveWeatherInformationJsonStringTask = new RetrieveWeatherInformationJsonStringTask();
			retrieveWeatherInformationJsonStringTask.setContext(parentActivity);
			retrieveWeatherInformationJsonStringTask.execute(openWeatherMapUrl);
		} else {
			Toast.makeText(parentActivity, R.string.error_message,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (retrieveWeatherInformationJsonStringTask != null) {
			retrieveWeatherInformationJsonStringTask.cancel(true);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
		callbackToParentActivity = null;
	}

	/**
	 * A task to obtain JSON weather data from the provided Open Weather Map
	 * URL.
	 * <p>
	 * <p>
	 * Since weather data doesn't change that often, retrieved JSON strings are
	 * saved locally, and reused for a short period of time (chosen by user). So
	 * the task first checks if there already exists a recently requested and
	 * saved data in the local SQLite Database, and connects to internet only if
	 * such data is too old or does not exist.
	 */
	private class RetrieveWeatherInformationJsonStringTask extends
			AsyncTaskWithProgressBar<URL, Void, String> {

		/**
		 * If the data is obtained from the web service, it should be saved and
		 * reused for a short period of time for efficiency reasons.
		 */
		private boolean saveDataLocally = false;

		@Override
		protected String doInBackground(URL... params) {
			SqlOperation sqlOperation = new SqlOperation(parentActivity,
					weatherInfoType);
			String jsonString = sqlOperation
					.getJsonStringForWeatherInfo(cityId);
			if (jsonString == null && !isCancelled()) {
				jsonString = getJSONStringFromWebService(params[0]);
				saveDataLocally = jsonString != null;
			}
			return jsonString;
		}

		/**
		 * Attempts to obtain JSON weather data from the provided Open Weather
		 * Map URL
		 * 
		 * @param url
		 *            Open Weather Map URL
		 * @return JSON data for the requested city and weather information
		 *         type, or {@code null} in case of network problems
		 */
		private String getJSONStringFromWebService(URL url) {
			try {
				return new JsonFetcher().getJsonString(url);
			} catch (IOException e) {
				MiscMethods.log("IOException in getJSONStringFromWebService()");
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				if (parentActivity != null) {
					Toast.makeText(parentActivity, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
			} else if (callbackToParentActivity != null) {
				callbackToParentActivity.onJsonStringRetrieved(result,
						weatherInfoType, saveDataLocally);
			}
		}

	}

}
