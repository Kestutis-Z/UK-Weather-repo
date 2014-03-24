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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.haringeymobile.ukweather.data.CityUK;
import com.haringeymobile.ukweather.datastorage.CityTable;
import com.haringeymobile.ukweather.datastorage.WeatherContentProvider;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class CityListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	public interface OnCitySelectedListener {

		public void onCitySelected(int cityId);

	}

	static final String LAST_SELECTED_CITY_ID = "city id";
	private static final int BACKGROUND_RESOURCE_EVEN = R.drawable.clickable_blue;
	private static final int BACKGROUND_RESOURCE_ODD = R.drawable.clickable_green;
	private static final int LOADER_ALL_CITY_RECORDS = 0;

	private Activity parentActivity;
	private OnCitySelectedListener listener;
	private CityCursorAdapter cursorAdapter;
	private int lastCityId;
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
		lastCityId = SharedPrefsHelper.getIntFromSharedPrefs(parentActivity,
				LAST_SELECTED_CITY_ID, CityUK.LONDON.getOpenWeatherMapId());
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
			listener.onCitySelected(lastCityId);
		}
	}

	private void displayCityList() {
		int rowLayoutID = R.layout.row_city_list;
		int[] to = new int[] { R.id.city_name_in_list_row_text_view };
		String[] columnsToDisplay = new String[] { CityTable.COLUMN_NAME };

		cursorAdapter = new CityCursorAdapter(parentActivity, rowLayoutID,
				null, columnsToDisplay, to, 0);

		setListAdapter(cursorAdapter);

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
	public void onListItemClick(ListView listView, View v, int position, long id) {
		super.onListItemClick(listView, v, position, id);
		lastCityId = cursorAdapter.getCityId(position);
		SharedPrefsHelper.putIntIntoSharedPrefs(parentActivity,
				LAST_SELECTED_CITY_ID, lastCityId);
		listener.onCitySelected(lastCityId);
		// TODO v.setBackgroundResource(R.drawable.background_highlighted_view);
	}

	int getLastCityId() {
		return lastCityId;
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
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		cursorAdapter.swapCursor(null);
	}

	private class CityCursorAdapter extends SimpleCursorAdapter {

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

		int getCityId(int position) {
			Cursor cursor = getCursor();
			if (cursor.moveToPosition(position)) {
				return cursor.getInt(cursor
						.getColumnIndex(CityTable.COLUMN_CITY_ID));
			}
			return CityTable.CITY_ID_DOES_NOT_EXIST;
		}

	}

}
