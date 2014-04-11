package com.haringeymobile.ukweather;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haringeymobile.ukweather.database.CityTable;

public class CityUtilitiesCursorAdapter extends BaseCityCursorAdapter {

	private static class CityRowUtilitiesViewHolder {

		TextView cityNameTextView;
		ImageButton buttonRename;
		ImageButton buttonDelete;

	}

	CityUtilitiesCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, OnClickListener onClickListener) {
		super(context, layout, c, from, to, flags, onClickListener);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View rowView = ((LayoutInflater) context
				.getSystemService("layout_inflater")).inflate(
				R.layout.row_city_list_with_utils_buttons, parent, false);

		CityRowUtilitiesViewHolder holder = new CityRowUtilitiesViewHolder();
		holder.cityNameTextView = (TextView) rowView
				.findViewById(R.id.city_name_in_list_row_text_view);
		holder.buttonRename = (ImageButton) rowView
				.findViewById(R.id.city_rename_button);
		holder.buttonDelete = (ImageButton) rowView
				.findViewById(R.id.city_delete_button);
		holder.buttonRename.setOnClickListener(onClickListener);
		holder.buttonDelete.setOnClickListener(onClickListener);
		rowView.setTag(holder);

		return rowView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CityRowUtilitiesViewHolder holder = (CityRowUtilitiesViewHolder) view
				.getTag();
		int nameColumsIndex = cursor
				.getColumnIndexOrThrow(CityTable.COLUMN_NAME);
		holder.cityNameTextView.setText(cursor.getString(nameColumsIndex));
	}

}
