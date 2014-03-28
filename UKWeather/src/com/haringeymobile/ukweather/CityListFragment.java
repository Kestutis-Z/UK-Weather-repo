package com.haringeymobile.ukweather;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.haringeymobile.ukweather.datastorage.CityTable;
import com.haringeymobile.ukweather.datastorage.WeatherContentProvider;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class CityListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, OnClickListener {

	public interface OnCitySelectedListener {

		public void onCityRecordDeletionRequested(int cityId, String cityName);

		public void onCityCurrentWeatherRequested(int cityId);

		public void onCityWeatherForecastRequested(int cityId);

	}

	public static final String LAST_SELECTED_CITY_ID = "city id";
	static final int BACKGROUND_RESOURCE_EVEN = R.drawable.clickable_blue;
	static final int BACKGROUND_RESOURCE_ODD = R.drawable.clickable_green;
	private static final int LOADER_ALL_CITY_RECORDS = 0;

	private Activity parentActivity;
	private OnCitySelectedListener listener;
	private CityCursorAdapter cursorAdapter;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_city_list, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		displayCityList();
		isDualPane = (FrameLayout) parentActivity
				.findViewById(R.id.weather_info_container) != null;
		if (isDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			int lastCityId = SharedPrefsHelper.getIntFromSharedPrefs(
					parentActivity, LAST_SELECTED_CITY_ID,
					CityTable.CITY_ID_DOES_NOT_EXIST);
			listener.onCityCurrentWeatherRequested(lastCityId);
		}
	}

	private void displayCityList() {
		int rowLayoutID = R.layout.row_city_list_with_buttons;
		int[] to = new int[] { R.id.city_name_in_list_row_text_view };
		String[] columnsToDisplay = new String[] { CityTable.COLUMN_NAME };

		cursorAdapter = new CityCursorAdapter(parentActivity, rowLayoutID,
				null, columnsToDisplay, to, 0);

		setListAdapter(cursorAdapter);
		ListView listView = getListView();
		listView.setItemsCanFocus(true);
		listView.setFocusable(false);
		listView.setFocusableInTouchMode(false);
		listView.setClickable(false);

		getLoaderManager().initLoader(LOADER_ALL_CITY_RECORDS, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Starts a new or restarts an existing Loader in this manager
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
		listener = null;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = CityTable.COLUMN_LAST_QUERY_DATE + " DESC";

		CursorLoader cursorLoader = new CursorLoader(parentActivity,
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS, projection,
				selection, selectionArgs, sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		cursorAdapter.swapCursor(data);
		// Jump to the top of the list
		ListView listView = getListView();
		if (listView != null) {
			listView.setSelection(0);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.swapCursor(null);
	}

	@Override
	public void onClick(View view) {
		int position = getListView().getPositionForView(view);
		int cityId = cursorAdapter.getCityId(position);
		switch (view.getId()) {
		case R.id.city_delete_button:
			listener.onCityRecordDeletionRequested(cityId,
					cursorAdapter.getCityName(position));
			break;
		case R.id.city_current_weather_button:
			SharedPrefsHelper.putIntIntoSharedPrefs(parentActivity,
					LAST_SELECTED_CITY_ID, cityId);
			listener.onCityCurrentWeatherRequested(cityId);
			break;
		case R.id.city_weather_forecast_button:
			listener.onCityWeatherForecastRequested(cityId);
			break;
		default:
			throw new IllegalArgumentException("Not supported view ID: "
					+ view.getId());
		}
	}

	private static class CityViewHolder {

		TextView cityNameTextView;
		ImageButton buttonDelete;
		ImageButton buttonCurrentWeather;
		ImageButton buttonWeatherForecast;

	}

	private class CityCursorAdapter extends SimpleCursorAdapter {

		private OnClickListener onClickListener = CityListFragment.this;

		private CityCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			if (position % 2 == 1) {
				view.setBackgroundResource(BACKGROUND_RESOURCE_ODD);
			} else {
				view.setBackgroundResource(BACKGROUND_RESOURCE_EVEN);
			}
			return view;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View rowView = ((LayoutInflater) context
					.getSystemService("layout_inflater")).inflate(
					R.layout.row_city_list_with_buttons, parent, false);

			CityViewHolder holder = new CityViewHolder();
			holder.cityNameTextView = (TextView) rowView
					.findViewById(R.id.city_name_in_list_row_text_view);
			holder.buttonDelete = (ImageButton) rowView
					.findViewById(R.id.city_delete_button);
			holder.buttonCurrentWeather = (ImageButton) rowView
					.findViewById(R.id.city_current_weather_button);
			holder.buttonWeatherForecast = (ImageButton) rowView
					.findViewById(R.id.city_weather_forecast_button);
			holder.buttonDelete.setOnClickListener(onClickListener);
			holder.buttonCurrentWeather.setOnClickListener(onClickListener);
			holder.buttonWeatherForecast.setOnClickListener(onClickListener);
			rowView.setTag(holder);

			return rowView;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			CityViewHolder holder = (CityViewHolder) view.getTag();
			int nameColumsIndex = cursor
					.getColumnIndexOrThrow(CityTable.COLUMN_NAME);
			holder.cityNameTextView.setText(cursor.getString(nameColumsIndex));
		}

		int getCityId(int position) {
			Cursor cursor = getCursor();
			if (cursor.moveToPosition(position)) {
				return cursor.getInt(cursor
						.getColumnIndex(CityTable.COLUMN_CITY_ID));
			}
			return CityTable.CITY_ID_DOES_NOT_EXIST;
		}

		String getCityName(int position) {
			Cursor cursor = getCursor();
			if (cursor.moveToPosition(position)) {
				return cursor.getString(cursor
						.getColumnIndex(CityTable.COLUMN_NAME));
			}
			return null;
		}

	}

}
