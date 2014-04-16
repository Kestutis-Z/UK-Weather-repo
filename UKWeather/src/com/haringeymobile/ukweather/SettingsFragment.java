package com.haringeymobile.ukweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * An fragment to provide settings for the app for Android versions starting
 * with Honeycomb (version 11).
 */
@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreferences);
	}

}
