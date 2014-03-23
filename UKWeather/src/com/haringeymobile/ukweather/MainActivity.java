package com.haringeymobile.ukweather;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.haringeymobile.ukweather.data.CityUK;
import com.haringeymobile.ukweather.utils.MiscMethods;

public class MainActivity extends ActionBarActivity implements
		CityListFragment.OnCitySelectedListener {

	static final String CITY = "city";
	static final String CITY_LIST = "city list";
	private static final String LIST_FRAGMENT_TAG = "list fragment";
	private static final String WEATHER_INFO_FRAGMENT_TAG = "weather fragment";

	private boolean isDualPane;
	private SearchView searchView;

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
		if (isDualPane) {
			WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) fragmentManager
					.findFragmentByTag(WEATHER_INFO_FRAGMENT_TAG);
			weatherInfoFragment = WeatherInfoFragment.newInstance(null);
			fragmentTransaction.replace(R.id.weather_info_container,
					weatherInfoFragment, WEATHER_INFO_FRAGMENT_TAG);
		}
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem searchItem = menu.findItem(R.id.mi_search);
	    searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		
		// Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	   // SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
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
	public void onCitySelected(CityUK city) {
		MiscMethods.log("In onCitySelected" + " city");
		if (isDualPane) {
			updateWeatherInformation(city);
		} else {
			Intent intent = new Intent(this, WeatherInfoActivity.class);
			intent.putExtra(CITY, (Parcelable) city);
			startActivity(intent);
		}
	}

	public void updateWeatherInformation(CityUK city) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		WeatherInfoFragment weatherInfoFragment = (WeatherInfoFragment) fragmentManager
				.findFragmentByTag(WEATHER_INFO_FRAGMENT_TAG);
		if (weatherInfoFragment == null || !weatherInfoFragment.isInLayout()) {
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.weather_info_container,
					WeatherInfoFragment.newInstance(city),
					WEATHER_INFO_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.weather_info_container);
		frameLayout.setVisibility(View.VISIBLE);
	}

}
