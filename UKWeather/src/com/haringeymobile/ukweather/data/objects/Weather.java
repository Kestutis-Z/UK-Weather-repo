package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;

public class Weather {

	public static final String ICON_URL_PREFIX = "http://openweathermap.org/img/w/";

	@SerializedName("description")
	private String description;

	@SerializedName("icon")
	private String icon;

	@SerializedName("id")
	private int id;

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

}
