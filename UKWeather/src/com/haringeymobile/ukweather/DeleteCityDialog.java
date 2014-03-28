package com.haringeymobile.ukweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteCityDialog extends DialogFragment {

	public interface Listener {

		void onCityRecordDeletionConfirmed(int cityId);

	}

	private static final String CITY_NAME = "city name";

	private Activity parentActivity;
	private Listener callbackToParentActivity;

	public static DeleteCityDialog newInstance(int cityId, String cityName) {
		DeleteCityDialog dialogFragment = new DeleteCityDialog();
		Bundle b = new Bundle();
		b.putInt(MainActivity.CITY_ID, cityId);
		b.putString(CITY_NAME, cityName);
		dialogFragment.setArguments(b);
		return dialogFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = getActivity();
		try {
			callbackToParentActivity = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		parentActivity = null;
		callbackToParentActivity = null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Resources res = parentActivity.getResources();
		final String cityName = getArguments().getString(CITY_NAME);
		String title = res.getString(R.string.dialog_title_delete_city_part_1)
				+ " " + cityName + " "
				+ res.getString(R.string.dialog_title_delete_city_part_2);
		return new AlertDialog.Builder(parentActivity)
				.setIcon(R.drawable.abc_ic_go)
				.setTitle(title)
				.setPositiveButton(res.getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callbackToParentActivity
										.onCityRecordDeletionConfirmed(getArguments()
												.getInt(MainActivity.CITY_ID));
							}
						}).create();
	}

}
