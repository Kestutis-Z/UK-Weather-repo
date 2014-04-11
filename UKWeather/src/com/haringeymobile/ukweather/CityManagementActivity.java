package com.haringeymobile.ukweather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haringeymobile.ukweather.database.CityTable;
import com.haringeymobile.ukweather.database.GeneralDatabaseService;
import com.haringeymobile.ukweather.utils.SharedPrefsHelper;

public class CityManagementActivity extends ActionBarActivity implements
		CityListFragmentWithUtilityButtons.Listener, DeleteCityDialog.Listener {

	public static final String CITY_ID = "city id";
	public static final String CITY_NEW_NAME = "city new name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_management);
	}

	@Override
	public void onCityNameChangeRequested(final int cityId,
			final String originalName) {
		AlertDialog.Builder cityNameChangeDialog = new AlertDialog.Builder(this);
		Resources res = getResources();
		String title = res.getString(R.string.dialog_title_rename_city_part_1)
				+ originalName
				+ res.getString(R.string.dialog_title_rename_city_part_2);
		cityNameChangeDialog.setTitle(title);

		final EditText cityNameEditText = new EditText(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		cityNameEditText.setLayoutParams(lp);
		cityNameChangeDialog.setView(cityNameEditText);

		cityNameChangeDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						String newName = cityNameEditText.getText().toString();
						if (newName.length() == 0) {
							Toast.makeText(CityManagementActivity.this,
									R.string.message_enter_city_name,
									Toast.LENGTH_SHORT).show();
							return;
						}
						boolean userNameHasBeenChanged = !newName
								.equals(originalName);
						if (userNameHasBeenChanged) {
							renameCity(cityId, newName);
						}
					}

				});

		cityNameChangeDialog.show();
	}

	private void renameCity(int cityId, String newName) {
		Intent intent = new Intent(this, GeneralDatabaseService.class);
		intent.setAction(GeneralDatabaseService.ACTION_RENAME_CITY);
		intent.putExtra(CITY_ID, cityId);
		intent.putExtra(CITY_NEW_NAME, newName);
		startService(intent);
	}

	@Override
	public void onCityRecordDeletionRequested(int cityId, String cityName) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		DeleteCityDialog dialogFragment = (DeleteCityDialog) fragmentManager
				.findFragmentByTag(GetAvailableCitiesTask.CITY_DELETE_DIALOG_FRAGMENT_TAG);
		if (dialogFragment == null) {
			dialogFragment = DeleteCityDialog.newInstance(cityId, cityName);
			dialogFragment.show(fragmentManager,
					GetAvailableCitiesTask.CITY_DELETE_DIALOG_FRAGMENT_TAG);
		}
	}

	@Override
	public void onCityRecordDeletionConfirmed(int cityId) {
		int lastCityId = SharedPrefsHelper.getCityIdFromSharedPrefs(this);
		if (cityId == lastCityId) {
			SharedPrefsHelper.putCityIdIntoSharedPrefs(this,
					CityTable.CITY_ID_DOES_NOT_EXIST);
		}
		removeCity(cityId);
	}

	private void removeCity(int cityId) {
		Intent intent = new Intent(this, GeneralDatabaseService.class);
		intent.setAction(GeneralDatabaseService.ACTION_DELETE_CITY_RECORDS);
		intent.putExtra(CITY_ID, cityId);
		startService(intent);
	}

}
