package com.haringeymobile.ukweather;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.JsonParser;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlStrategy;
import com.haringeymobile.ukweather.data.JsonParsingFromUrlUsingHttpConnection;
import com.haringeymobile.ukweather.data.OpenWeatherMapUrlBuilder;
import com.haringeymobile.ukweather.data.objects.CityCurrentWeather;
import com.haringeymobile.ukweather.data.objects.SearchResponseForFindQuery;
import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.GeneralDatabaseService;
import com.haringeymobile.ukweather.database.SQLOperation;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class MainActivity extends ActionBarActivity implements
		CityListFragment.OnCitySelectedListener,
		CitySearchResultsDialog.OnCityNamesListItemClickedListener,
		DeleteCityDialog.Listener {

	public static final String CITY_ID = "city id";
	static final String CITY_LIST = "city list";
	private static final String LIST_FRAGMENT_TAG = "list fragment";
	private static final String WEATHER_INFO_FRAGMENT_TAG = "weather fragment";
	private static final String CITY_SEARCH_RESULTS_FRAGMENT_TAG = "search results";
	private static final String CITY_DELETE_DIALOG_FRAGMENT_TAG = "delete city dialog";
	private static final String CITY_NAME_LIST = "city names";

	private static final int MINIMUM_SEARCH_QUERY_STRING_LENGTH = 3;

	private boolean isDualPane;
	private SearchView searchView;
	private SearchResponseForFindQuery searchResponseForFindQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment cityListFragment = fragmentManager
				.findFragmentByTag(LIST_FRAGMENT_TAG);
		if (cityListFragment == null) {
			cityListFragment = new CityListFragment();
			fragmentTransaction.add(R.id.city_list_container, cityListFragment,
					LIST_FRAGMENT_TAG);
		}

		isDualPane = (FrameLayout) findViewById(R.id.weather_info_container) != null;
		if (isDualPane && savedInstanceState != null) {
			WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) fragmentManager
					.findFragmentByTag(WEATHER_INFO_FRAGMENT_TAG);
			if (weatherInfoFragment == null) {
				weatherInfoFragment = new WeatherInfoFragment();
				fragmentTransaction.replace(R.id.weather_info_container,
						weatherInfoFragment, WEATHER_INFO_FRAGMENT_TAG);
			}
		}
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.mi_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint(getResources().getString(
				R.string.city_search_hint));

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (query.length() < MINIMUM_SEARCH_QUERY_STRING_LENGTH) {
					showQueryStringTooShortAlertDialog();
				} else {
					new GetAvailableCitiesTask()
							.execute(new OpenWeatherMapUrlBuilder()
									.getAvailableCitiesListURL(query));
				}
				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		return true;
	}

	private void showQueryStringTooShortAlertDialog() {
		new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.dialog_title_query_too_short)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dismiss();
									}
								});
				return builder.create();
			}

		}.show(getSupportFragmentManager(), CITY_DELETE_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.mi_search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCityRecordDeletionRequested(int cityId, String cityName) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		DeleteCityDialog dialogFragment = (DeleteCityDialog) fragmentManager
				.findFragmentByTag(CITY_DELETE_DIALOG_FRAGMENT_TAG);
		if (dialogFragment == null) {
			dialogFragment = DeleteCityDialog.newInstance(cityId, cityName);
			dialogFragment.show(fragmentManager,
					CITY_DELETE_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	public void onCityRecordDeletionConfirmed(int cityId) {
		int lastCityId = SharedPrefsHelper.getIntFromSharedPrefs(this,
				CityListFragment.LAST_SELECTED_CITY_ID,
				CityTable.CITY_ID_DOES_NOT_EXIST);
		if (cityId == lastCityId) {
			SharedPrefsHelper.putIntIntoSharedPrefs(this,
					CityListFragment.LAST_SELECTED_CITY_ID,
					CityTable.CITY_ID_DOES_NOT_EXIST);
		}
		removeCity(cityId);

	}

	private void removeCity(int cityId) {
		Intent intent = new Intent(this, GeneralDatabaseService.class);
		intent.setAction(GeneralDatabaseService.ACTION_DELETE_CITY_RECORDS);
		intent.putExtra(CITY_ID, cityId);
		startService(intent);
	}

	@Override
	public void onCityCurrentWeatherRequested(int cityId) {
		if (isDualPane) {
			if (cityId != CityTable.CITY_ID_DOES_NOT_EXIST) {
				updateWeatherInformation(cityId);
			}
		} else {
			Intent intent = new Intent(this, WeatherInfoActivity.class);
			intent.putExtra(CITY_ID, cityId);
			startActivity(intent);
		}
	}

	@Override
	public void onCityWeatherForecastRequested(int cityId) {
		// TODO Auto-generated method stub
		new DialogFragment() {

			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(
						"Sorry, weather forecast is not yet implemented!")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dismiss();
									}
								});
				return builder.create();
			}

		}.show(getSupportFragmentManager(), CITY_DELETE_DIALOG_FRAGMENT_TAG);

	}

	public void updateWeatherInformation(int cityId) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) fragmentManager
				.findFragmentByTag(WEATHER_INFO_FRAGMENT_TAG);
		if (weatherInfoFragment == null || !weatherInfoFragment.isInLayout()) {
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.weather_info_container,
					new WeatherInfoFragment(), WEATHER_INFO_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.weather_info_container);
		frameLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClicked(final int position) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (searchResponseForFindQuery == null) {
					// TODO save it in onSaveInstanceState
					return;
				}
				CityCurrentWeather selectedCityWeather = searchResponseForFindQuery
						.getCities().get(position);
				Gson gson = new Gson();
				String currentWeather = gson.toJson(selectedCityWeather);
				new SQLOperation(MainActivity.this).insertOrUpdateCity(
						selectedCityWeather.getCityId(),
						selectedCityWeather.getCityName(),
						System.currentTimeMillis(), currentWeather, null);
			}
		}).start();

	}

	private class GetAvailableCitiesTask extends
			AsyncTask<URL, Void, SearchResponseForFindQuery> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage(getResources().getString(
					R.string.loading_message));
			progressDialog.setIndeterminate(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected SearchResponseForFindQuery doInBackground(URL... params) {
			JsonParser jsonRetriever = new JsonParser();
			jsonRetriever
					.setHttpCallsHandlingStrategy(new JsonParsingFromUrlUsingHttpConnection());
			String jsonString = jsonRetriever.getJSONString(params[0]);
			if (jsonString == null) {
				return null;
			} else {
				Gson gson = new Gson();
				searchResponseForFindQuery = gson.fromJson(jsonString,
						SearchResponseForFindQuery.class);
				return searchResponseForFindQuery;
			}
		}

		@Override
		protected void onPostExecute(SearchResponseForFindQuery result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result == null
					|| result.getCode() != JsonParsingFromUrlStrategy.HTTP_STATUS_CODE_OK) {
				if (MainActivity.this != null) {
					Toast.makeText(MainActivity.this, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
				return;
			} else if (result.getCount() < 1) {
				showNoCitiesFoundAlertDialog();
			} else {
				ArrayList<String> foundCityNames = getFoundCityNames(result);
				FragmentManager fragmentManager = getSupportFragmentManager();
				CitySearchResultsDialog citySearchResultsDialog = CitySearchResultsDialog
						.newInstance(foundCityNames);
				citySearchResultsDialog.show(fragmentManager,
						CITY_SEARCH_RESULTS_FRAGMENT_TAG);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(CITY_NAME_LIST, foundCityNames);
			}
		}

		private ArrayList<String> getFoundCityNames(
				SearchResponseForFindQuery result) {
			ArrayList<String> foundCityNames = new ArrayList<>();
			List<CityCurrentWeather> cities = result.getCities();
			for (CityCurrentWeather city : cities) {
				foundCityNames.add(city.getCityName() + ", "
						+ city.getSystemParameters().getCountry());
			}
			return foundCityNames;
		}

	}

	private void showNoCitiesFoundAlertDialog() {
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

		}.show(getSupportFragmentManager(), CITY_DELETE_DIALOG_FRAGMENT_TAG);
	}

}
