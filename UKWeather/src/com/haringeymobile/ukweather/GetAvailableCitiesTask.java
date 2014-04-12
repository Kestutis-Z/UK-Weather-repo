package com.haringeymobile.ukweather;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.JsonParser;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlStrategy;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlUsingHttpConnection;
import com.haringeymobile.ukweather.data.objects.CityCurrentWeather;
import com.haringeymobile.ukweather.data.objects.Coordinates;
import com.haringeymobile.ukweather.data.objects.SearchResponseForFindQuery;
import com.haringeymobile.ukweather.utils.AsyncTaskWithProgressBar;
import com.haringeymobile.ukweather.utils.MiscMethods;

class GetAvailableCitiesTask extends
		AsyncTaskWithProgressBar<URL, Void, SearchResponseForFindQuery> {

	public interface Listener {

		public void onSearchResponseForFindQueryRetrieved(
				SearchResponseForFindQuery searchResponseForFindQuery);

	}

	static final String CITY_SEARCH_RESULTS_FRAGMENT_TAG = "search results";
	static final String CITY_DELETE_DIALOG_FRAGMENT_TAG = "delete city dialog";

	private final FragmentActivity activity;

	GetAvailableCitiesTask(FragmentActivity activity) {
		this.activity = activity;
	}

	@Override
	protected SearchResponseForFindQuery doInBackground(URL... params) {
		JsonParser jsonParser = new JsonParser();
		jsonParser
				.setJsonParsingStrategy(new JsonParsingFromUrlUsingHttpConnection());
		String jsonString = jsonParser.getJsonString(params[0]);
		if (jsonString == null) {
			return null;
		} else {
			Gson gson = new Gson();
			SearchResponseForFindQuery searchResponseForFindQuery = gson
					.fromJson(jsonString, SearchResponseForFindQuery.class);
			return searchResponseForFindQuery;
		}
	}

	@Override
	protected void onPostExecute(SearchResponseForFindQuery result) {
		super.onPostExecute(result);
		if (result == null
				|| result.getCode() != JsonParsingFromUrlStrategy.HTTP_STATUS_CODE_OK) {
			if (activity != null) {
				Toast.makeText(activity, R.string.error_message,
						Toast.LENGTH_SHORT).show();
			}
			return;
		} else if (result.getCount() < 1) {
			showNoCitiesFoundAlertDialog();
		} else {
			try {
				Listener listener = (Listener) activity;
				listener.onSearchResponseForFindQueryRetrieved(result);
			} catch (ClassCastException e) {
				throw new ClassCastException(activity.toString()
						+ " must implement Listener");
			}

			ArrayList<String> foundCityNames = getFoundCityNames(result);
			FragmentManager fragmentManager = activity
					.getSupportFragmentManager();
			CitySearchResultsDialog citySearchResultsDialog = CitySearchResultsDialog
					.newInstance(foundCityNames);
			citySearchResultsDialog.show(fragmentManager,
					CITY_SEARCH_RESULTS_FRAGMENT_TAG);
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(CitySearchResultsDialog.CITY_NAME_LIST,
					foundCityNames);
		}
	}

	private ArrayList<String> getFoundCityNames(
			SearchResponseForFindQuery result) {
		ArrayList<String> foundCityNames = new ArrayList<>();
		List<CityCurrentWeather> cities = result.getCities();
		for (CityCurrentWeather city : cities) {
			Coordinates cityCoordinates = city.getCoordinates();
			foundCityNames.add(city.getCityName()
					+ ", "
					+ city.getSystemParameters().getCountry()
					+ "\n("
					+ MiscMethods.formatDoubleValue(cityCoordinates
							.getLatitude())
					+ ", "
					+ MiscMethods.formatDoubleValue(cityCoordinates
							.getLongitude()) + ")");
		}
		return foundCityNames;
	}

	void showNoCitiesFoundAlertDialog() {
		new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.dialog_title_no_cities_found)
						.setMessage(R.string.message_no_cities_found)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dismiss();
									}
								});
				return builder.create();
			}

		}.show(activity.getSupportFragmentManager(),
				CITY_DELETE_DIALOG_FRAGMENT_TAG);
	}

}