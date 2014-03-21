package com.haringeymobile.ukweather.utils;

import java.text.DecimalFormat;

import android.util.Log;

public class MiscMethods {

	private static final boolean LOGS_ON = false;
	private static final String LOGS = "Logs";

	public static void log(String s) {
		if (LOGS_ON)
			Log.d(LOGS, s);
	}

	public static String formatDoubleValue(double d) {
		DecimalFormat df = new DecimalFormat("##.#");
		return df.format(d);
	}

}
