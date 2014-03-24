package com.haringeymobile.ukweather.data.json;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class Weather {

	private static final String ICON_URL_PREFIX = "http://openweathermap.org/img/w/";

	private String description;

	private String icon;

	private int id;

	private Drawable iconDrawable;

	@SerializedName("main")
	private String type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Drawable getIconDrawable(Resources res) {
		String iconUrl = ICON_URL_PREFIX + icon;
		try {
			URL url = new URL(iconUrl);
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
