package com.haringeymobile.ukweather;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haringeymobile.ukweather.data.CityUK;

public class CityListFragment extends ListFragment {

	public interface OnCitySelectedListener {

		public void onCitySelected(CityUK city);

	}

	private static final String SELECTED_LIST_POSITION = "position";
	private static final int POSITION_NOT_YET_SELECTED = -1;

	private Activity parentActivity;
	private OnCitySelectedListener listener;
	private CityNameArrayAdapter arrayAdapter;
	private List<CityUK> ukCities;
	private int lastSelectedPosition = POSITION_NOT_YET_SELECTED;
	private boolean isDualPane;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
		try {
			listener = (OnCitySelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCitySelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ukCities = getUKCities();
	}

	private List<CityUK> getUKCities() {
		List<CityUK> cities = new ArrayList<>();
		cities.addAll(EnumSet.allOf(CityUK.class));
		return cities;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_city_list, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (arrayAdapter == null) {
			arrayAdapter = new CityNameArrayAdapter(parentActivity,
					R.layout.row_city_list, ukCities);
		}
		setListAdapter(arrayAdapter);

		if (savedInstanceState != null) {
			lastSelectedPosition = savedInstanceState
					.getInt(SELECTED_LIST_POSITION);
		}
		isDualPane = (FrameLayout) parentActivity
				.findViewById(R.id.weather_info_container) != null;
		if (isDualPane && lastSelectedPosition != POSITION_NOT_YET_SELECTED) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			displayWeatherInformation(lastSelectedPosition);
		}
	}

	public void displayWeatherInformation(int position) {
		lastSelectedPosition = position;
		if (isDualPane) {
			getListView().setItemChecked(position, true);
		}
		listener.onCitySelected((CityUK) getListView().getItemAtPosition(
				position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SELECTED_LIST_POSITION, lastSelectedPosition);
	}

	@Override
	public void onDestroyView() {
		setListAdapter(null);
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
		listener = null;
	}

	@Override
	public void onListItemClick(ListView listView, View v, int position, long id) {
		super.onListItemClick(listView, v, position, id);
		displayWeatherInformation(position);
		// TODO v.setBackgroundResource(R.drawable.background_highlighted_view);
	}

	private static class WeatherInformationViewHolder {
		TextView cityNameTextView;
	}

	private class CityNameArrayAdapter extends ArrayAdapter<CityUK> {

		private Activity context;
		private int layoutResourceId;
		private final List<CityUK> cities;

		private CityNameArrayAdapter(Activity activity, int layoutResourceId,
				List<CityUK> cities) {
			super(activity, layoutResourceId, cities);
			this.context = activity;
			this.layoutResourceId = layoutResourceId;
			this.cities = cities;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WeatherInformationViewHolder holder;
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(layoutResourceId, parent, false);
				holder = new WeatherInformationViewHolder();
				holder.cityNameTextView = (TextView) rowView
						.findViewById(R.id.city_name_in_list_row_text_view);
				rowView.setTag(holder);
			}
			holder = (WeatherInformationViewHolder) rowView.getTag();

			CityUK city = cities.get(position);
			String cityName = context.getResources().getString(
					city.getDisplayNameStringResource());
			holder.cityNameTextView.setText(cityName);

			if (position % 2 == 1) {
				rowView.setBackgroundResource(R.drawable.background_alternate_odd);
			} else {
				rowView.setBackgroundResource(R.drawable.background_alternate_even);
			}

			return rowView;
		}

	}

}
