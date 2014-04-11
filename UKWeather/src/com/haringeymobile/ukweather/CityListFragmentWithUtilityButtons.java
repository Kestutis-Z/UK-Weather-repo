package com.haringeymobile.ukweather;

import android.app.Activity;
import android.view.View;

public class CityListFragmentWithUtilityButtons extends
		BaseCityListFragmentWithButtons {

	public interface Listener {

		public void onCityRecordDeletionRequested(int cityId, String cityName);

		public void onCityNameChangeRequested(int cityId,
				String cityOriginalName);

	}

	private Listener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Listener");
		}
	}

	protected int getRowLayoutId() {
		return R.layout.row_city_list_with_weather_buttons;
	}

	protected BaseCityCursorAdapter getCityCursorAdapter() {
		return new CityUtilitiesCursorAdapter(parentActivity, getRowLayoutId(),
				null, COLUMNS_TO_DISPLAY, TO, 0, this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onClick(View view) {
		int position = getListView().getPositionForView(view);
		int cityId = cursorAdapter.getCityId(position);
		int viewId = view.getId();
		switch (viewId) {
		case R.id.city_rename_button:
			listener.onCityNameChangeRequested(cityId,
					cursorAdapter.getCityName(position));
			break;
		case R.id.city_delete_button:
			listener.onCityRecordDeletionRequested(cityId,
					cursorAdapter.getCityName(position));
			break;

		default:
			throw new IllegalArgumentException("Not supported view ID: "
					+ viewId);
		}
	}

}
