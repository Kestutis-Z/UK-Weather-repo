package com.haringeymobile.ukweather;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.haringeymobile.ukweather.data.JSONRetriever;
import com.haringeymobile.ukweather.data.JSONRetrievingFromURLStrategy_1;
import com.haringeymobile.ukweather.data.OpenWeatherMapURLBuilder;
import com.haringeymobile.ukweather.data.json.CityCurrentWeather;
import com.haringeymobile.ukweather.data.json.SearchResponseForFindQuery;

public class MainActivity extends ActionBarActivity implements
		CityListFragment.OnCitySelectedListener,
		CitySearchResultsDialog.OnCityNamesListItemClickedListener {

	static final String CITY_ID = "city";
	static final String CITY_LIST = "city list";
	private static final String LIST_FRAGMENT_TAG = "list fragment";
	private static final String WEATHER_INFO_FRAGMENT_TAG = "weather fragment";
	private static final String CITY_SEARCH_RESULTS_FRAGMENT_TAG = "search results";
	private static final String CITY_NAME_LIST = "city names";

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
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.mi_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				new GetAvailableCitiesTask()
						.execute(new OpenWeatherMapURLBuilder()
								.getAvailableCitiesListURL(query));
				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mi_search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCitySelected(int cityId) {
		if (isDualPane) {
			updateWeatherInformation(cityId);
		} else {
			Intent intent = new Intent(this, WeatherInfoActivity.class);
			intent.putExtra(CITY_ID, cityId);
			startActivity(intent);
		}
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
	public void onItemClicked(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "POSITION: " + position,
				Toast.LENGTH_SHORT).show();
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
			JSONRetriever jsonRetriever = new JSONRetriever();
			jsonRetriever
					.setHttpCallsHandlingStrategy(new JSONRetrievingFromURLStrategy_1());
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
					|| result.getCode() != SearchResponseForFindQuery.HTTP_STATUS_CODE_OK) {
				if (MainActivity.this != null) {
					Toast.makeText(MainActivity.this, R.string.error_message,
							Toast.LENGTH_SHORT).show();
				}
				return;
			} else if (result.getCount() < 1) {
				new NoCitiesFoundAlertDialog().show(
						getSupportFragmentManager(), null);
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

}
