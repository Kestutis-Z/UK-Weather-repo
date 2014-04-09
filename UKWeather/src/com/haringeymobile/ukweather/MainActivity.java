package com.haringeymobile.ukweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.OpenWeatherMapUrl;
import com.haringeymobile.ukweather.data.objects.CityCurrentWeather;
import com.haringeymobile.ukweather.data.objects.SearchResponseForFindQuery;
import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.GeneralDatabaseService;
import com.haringeymobile.ukweather.database.SqlOperation;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class MainActivity extends ActionBarActivity implements
		CityListFragment.OnCitySelectedListener,
		GetAvailableCitiesTask.Listener,
		CitySearchResultsDialog.OnCityNamesListItemClickedListener,
		DeleteCityDialog.Listener, WorkerFragmentToRetrieveJsonString.Listener {

	public static final String CITY_ID = "city id";
	public static final String WEATHER_INFORMATION_TYPE = "weather info type";
	public static final String WEATHER_INFO_JSON_STRING = "json string";

	private static final String LIST_FRAGMENT_TAG = "list fragment";
	private static final String WORKER_FRAGMENT_TAG = "worker fragment";
	private static final int MINIMUM_SEARCH_QUERY_STRING_LENGTH = 3;

	private boolean isDualPane;
	private SearchResponseForFindQuery searchResponseForFindQuery;
	private WorkerFragmentToRetrieveJsonString workerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isDualPane = (FrameLayout) findViewById(R.id.weather_info_container) != null;
		if (savedInstanceState != null) {
			String jsonString = savedInstanceState
					.getString(WEATHER_INFO_JSON_STRING);
			if (jsonString != null) {
				searchResponseForFindQuery = new Gson().fromJson(jsonString,
						SearchResponseForFindQuery.class);
			}
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		workerFragment = (WorkerFragmentToRetrieveJsonString) fragmentManager
				.findFragmentByTag(WORKER_FRAGMENT_TAG);
		if (workerFragment == null) {
			workerFragment = new WorkerFragmentToRetrieveJsonString();
			fragmentTransaction.add(workerFragment, WORKER_FRAGMENT_TAG);
		}
		Fragment cityListFragment = fragmentManager
				.findFragmentByTag(LIST_FRAGMENT_TAG);
		if (cityListFragment == null) {
			cityListFragment = new CityListFragment();
			fragmentTransaction.add(R.id.city_list_container, cityListFragment,
					LIST_FRAGMENT_TAG);
		}
		fragmentTransaction.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (searchResponseForFindQuery != null) {
			outState.putString(WEATHER_INFO_JSON_STRING,
					new Gson().toJson(searchResponseForFindQuery));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.mi_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);

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
					new GetAvailableCitiesTask(MainActivity.this).setContext(
							MainActivity.this).execute(
							new OpenWeatherMapUrl()
									.getAvailableCitiesListUrl(query));
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

		}.show(getSupportFragmentManager(),
				GetAvailableCitiesTask.CITY_DELETE_DIALOG_FRAGMENT_TAG);
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
	public void onSearchResponseForFindQueryRetrieved(
			SearchResponseForFindQuery searchResponseForFindQuery) {
		this.searchResponseForFindQuery = searchResponseForFindQuery;
	}

	@Override
	public void onCityRecordDeletionRequested(int cityId, String cityName) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		DeleteCityDialog dialogFragment = (DeleteCityDialog) fragmentManager
				.findFragmentByTag(GetAvailableCitiesTask.CITY_DELETE_DIALOG_FRAGMENT_TAG);
		if (dialogFragment == null) {
			dialogFragment = DeleteCityDialog.newInstance(cityId, cityName);
			dialogFragment.show(fragmentManager,
					GetAvailableCitiesTask.CITY_DELETE_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	public void onCityRecordDeletionConfirmed(int cityId) {
		int lastCityId = SharedPrefsHelper.getCityIdFromSharedPrefs(this);
		if (cityId == lastCityId) {
			SharedPrefsHelper.putCityIdIntoSharedPrefs(this,
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
	public void onFoundCityNamesItemClicked(final int position) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (searchResponseForFindQuery == null) {
					return;
				}
				CityCurrentWeather selectedCityWeather = searchResponseForFindQuery
						.getCities().get(position);
				Gson gson = new Gson();
				String currentWeather = gson.toJson(selectedCityWeather);
				new SqlOperation(MainActivity.this,
						WeatherInfoType.CURRENT_WEATHER)
						.updateOrInsertCityWithCurrentWeather(
								selectedCityWeather.getCityId(),
								selectedCityWeather.getCityName(),
								currentWeather);
			}
		}).start();

	}

	@Override
	public void onCityWeatherInfoRequested(int cityId,
			WeatherInfoType weatherInfoType) {
		workerFragment.retrieveWeatherInfoJsonString(cityId, weatherInfoType);
	}

	@Override
	public void onJsonStringRetrieved(String jsonString,
			WeatherInfoType weatherInfoType) {
		saveRetrievedData(jsonString, weatherInfoType);
		if (isDualPane) {
			displayRetrievedDataInThisActivity(jsonString, weatherInfoType);
		} else {
			displayRetrievedDataInNewActivity(jsonString, weatherInfoType);
		}
	}

	private void saveRetrievedData(String jsonString,
			WeatherInfoType weatherInfoType) {
		Intent intent = new Intent(this, GeneralDatabaseService.class);
		intent.setAction(GeneralDatabaseService.ACTION_UPDATE_WEATHER_INFO);
		intent.putExtra(WEATHER_INFO_JSON_STRING, jsonString);
		intent.putExtra(WEATHER_INFORMATION_TYPE, (Parcelable) weatherInfoType);
		startService(intent);
	}

	private void displayRetrievedDataInThisActivity(String jsonString,
			WeatherInfoType weatherInfoType) {
		Fragment fragment;
		if (weatherInfoType == WeatherInfoType.CURRENT_WEATHER) {
			fragment = WeatherInfoFragment.newInstance(weatherInfoType, null,
					jsonString);
		} else {
			fragment = WeatherForecastParentFragment.newInstance(
					weatherInfoType, jsonString);
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.weather_info_container, fragment).commit();
	}

	private void displayRetrievedDataInNewActivity(String jsonString,
			WeatherInfoType weatherInfoType) {
		Intent intent = new Intent(this, WeatherInfoActivity.class);
		intent.putExtra(WEATHER_INFORMATION_TYPE, (Parcelable) weatherInfoType);
		intent.putExtra(WEATHER_INFO_JSON_STRING, jsonString);
		startActivity(intent);
	}

}
