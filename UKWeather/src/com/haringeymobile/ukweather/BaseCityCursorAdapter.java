package com.haringeymobile.ukweather;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haringeymobile.ukweather.database.CityTable;

public abstract class BaseCityCursorAdapter extends SimpleCursorAdapter {

	protected OnClickListener onClickListener;

	BaseCityCursorAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags, OnClickListener onClickListener) {
		super(context, layout, c, from, to, flags);
		this.onClickListener = onClickListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (position % 2 == 1) {
			view.setBackgroundResource(CityListFragmentWithWeatherButtons.BACKGROUND_RESOURCE_ODD);
		} else {
			view.setBackgroundResource(CityListFragmentWithWeatherButtons.BACKGROUND_RESOURCE_EVEN);
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

	String getCityName(int position) {
		Cursor cursor = getCursor();
		if (cursor.moveToPosition(position)) {
			return cursor.getString(cursor
					.getColumnIndex(CityTable.COLUMN_NAME));
		}
		return null;
	}

}
