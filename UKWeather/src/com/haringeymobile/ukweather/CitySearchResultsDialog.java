package com.haringeymobile.ukweather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CitySearchResultsDialog extends DialogFragment {

	public interface OnCityNamesListItemClickedListener {

		void onItemClicked(int position);

	}

	private OnCityNamesListItemClickedListener listener;
	private ListView listView;
	private CityNameArrayAdapter arrayAdapter;

	static CitySearchResultsDialog newInstance(ArrayList<String> cityNames) {
		CitySearchResultsDialog dialog = new CitySearchResultsDialog();
		Bundle args = new Bundle();
		args.putStringArrayList(MainActivity.CITY_LIST, cityNames);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnCityNamesListItemClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCityNamesListItemClickedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_results,
				container);

		listView = (ListView) view.findViewById(android.R.id.list);
		listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.onItemClicked(position);
				CitySearchResultsDialog.this.dismiss();
			}
			
		});

		getDialog().setTitle(R.string.search_results_dialog_title);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (arrayAdapter == null) {
			Bundle args = getArguments();
			ArrayList<String> cityNames = args
					.getStringArrayList(MainActivity.CITY_LIST);
			arrayAdapter = new CityNameArrayAdapter(getActivity(),
					R.layout.row_city_list, cityNames);
		}
		listView.setAdapter(arrayAdapter);
	}

	@Override
	public void onDestroyView() {
		listView.setAdapter(null);
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	private static class CityNameViewHolder {
		TextView cityNameTextView;
	}

	private class CityNameArrayAdapter extends ArrayAdapter<String> {

		private Activity context;
		private int layoutResourceId;
		private final List<String> cities;

		private CityNameArrayAdapter(Activity activity, int layoutResourceId,
				List<String> cities) {
			super(activity, layoutResourceId, cities);
			this.context = activity;
			this.layoutResourceId = layoutResourceId;
			this.cities = cities;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CityNameViewHolder holder;
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(layoutResourceId, parent, false);
				holder = new CityNameViewHolder();
				holder.cityNameTextView = (TextView) rowView
						.findViewById(R.id.city_name_in_list_row_text_view);
				rowView.setTag(holder);
			}
			holder = (CityNameViewHolder) rowView.getTag();

			String cityName = cities.get(position);
			holder.cityNameTextView.setText(cityName);

			if (position % 2 == 1) {
				rowView.setBackgroundResource(CityListFragment.BACKGROUND_RESOURCE_ODD);
			} else {
				rowView.setBackgroundResource(CityListFragment.BACKGROUND_RESOURCE_EVEN);
			}

			return rowView;
		}

	}

}
