package com.haringeymobile.ukweather.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class WeatherConditions {

	private static final String ICON_URL_PREFIX = "http://openweathermap.org/img/w/";

	private String conditionName;
	private Drawable icon;

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(String iconString, Resources res) {
		String iconUrl = ICON_URL_PREFIX + iconString;
		this.icon = getDrawableFromURL(iconUrl, res);
	}

	public Drawable getDrawableFromURL(String url1, Resources res) {
		try {
			URL url = new URL(url1);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Drawable d = new BitmapDrawable(res, myBitmap);
			return d;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
