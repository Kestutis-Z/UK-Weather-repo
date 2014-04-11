package com.haringeymobile.ukweather;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.WeatherContentProvider;

public abstract class BaseCityListFragmentWithButtons extends ListFragment
		implements LoaderCallbacks<Cursor>, OnClickListener {

	protected int[] TO = new int[] { R.id.city_name_in_list_row_text_view };
	protected String[] COLUMNS_TO_DISPLAY = new String[] { CityTable.COLUMN_NAME };

	static final int BACKGROUND_RESOURCE_EVEN = R.drawable.clickable_blue;
	static final int BACKGROUND_RESOURCE_ODD = R.drawable.clickable_green;
	private static final int LOADER_ALL_CITY_RECORDS = 0;

	protected Activity parentActivity;
	protected BaseCityCursorAdapter cursorAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
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
	}

	private void displayCityList() {
		cursorAdapter = getCityCursorAdapter();

		setListAdapter(cursorAdapter);
		ListView listView = getListView();
		listView.setItemsCanFocus(true);
		listView.setFocusable(false);
		listView.setFocusableInTouchMode(false);
		listView.setClickable(false);

		getLoaderManager().initLoader(LOADER_ALL_CITY_RECORDS, null, this);
	}

	protected abstract int getRowLayoutId();

	protected abstract BaseCityCursorAdapter getCityCursorAdapter();

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
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = CityTable.COLUMN_LAST_OVERALL_QUERY_TIME + " DESC";

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

}
