package com.haringeymobile.ukweather.utils;

import android.app.Activity;
import android.content.Context;

public class SharedPrefsHelper {

	public static final String SHARED_PREFS_KEY = "com.haringeymobile.ukweather.PREFERENCE_FILE_KEY";

	public static int getIntFromSharedPrefs(Context context, String key,
			int defValue) {
		return context.getSharedPreferences(SHARED_PREFS_KEY,
				Activity.MODE_PRIVATE).getInt(key, defValue);
	}

	public static void putIntIntoSharedPrefs(Context context, String key,
			int value) {
		context.getSharedPreferences(SHARED_PREFS_KEY, Activity.MODE_PRIVATE)
				.edit().putInt(key, value).apply();
	}

}
