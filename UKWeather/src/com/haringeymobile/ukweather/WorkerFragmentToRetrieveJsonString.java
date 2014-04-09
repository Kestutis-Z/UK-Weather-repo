package com.haringeymobile.ukweather;

import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.haringeymobile.ukweather.data.JsonParser;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlUsingHttpConnection;
import com.haringeymobile.ukweather.database.SqlOperation;
import com.haringeymobile.ukweather.utils.AsyncTaskWithProgressBar;

public class WorkerFragmentToRetrieveJsonString extends Fragment {

	public interface Listener {

		public abstract void onJsonStringRetrieved(String jsonString,
				WeatherInfoType weatherInfoType);

	}

	private Activity parentActivity;
	private Listener callbackToParentActivity;
	private int cityId;
	private WeatherInfoType weatherInfoType;
	private RetrieveWeatherInformationJsonStringTask retrieveWeatherInformationJsonStringTask;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
		try {
			callbackToParentActivity = (Listener) activity;
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

	public void retrieveWeatherInfoJsonString(int cityId,
			WeatherInfoType weatherInfoType) {
		this.cityId = cityId;
		this.weatherInfoType = weatherInfoType;
		URL openWeatherMapUrl = weatherInfoType.getOpenWeatherMapUrl(cityId);
		retrieveWeatherInformationJsonStringTask = new RetrieveWeatherInformationJsonStringTask();
		retrieveWeatherInformationJsonStringTask.setContext(parentActivity);
		retrieveWeatherInformationJsonStringTask.execute(openWeatherMapUrl);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
		callbackToParentActivity = null;
	}

	private class RetrieveWeatherInformationJsonStringTask extends
			AsyncTaskWithProgressBar<URL, Void, String> {

		@Override
		protected String doInBackground(URL... params) {
			SqlOperation sqlOperation = new SqlOperation(parentActivity,
					weatherInfoType);
			String jsonString = sqlOperation
					.getJsonStringForWeatherInfo(cityId);
			if (jsonString == null) {
				jsonString = getJSONStringFromWebService(params[0]);
			}
			return jsonString;
		}

		private String getJSONStringFromWebService(URL url) {
			String jsonString;
			JsonParser jsonParser = new JsonParser();
			jsonParser
					.setJsonParsingStrategy(new JsonParsingFromUrlUsingHttpConnection());
			jsonString = jsonParser.getJsonString(url);
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				if (parentActivity != null) {
					Toast.makeText(parentActivity, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
				return;
			}
			if (callbackToParentActivity != null) {
				callbackToParentActivity.onJsonStringRetrieved(result,
						weatherInfoType);
			}
		}

	}

}
