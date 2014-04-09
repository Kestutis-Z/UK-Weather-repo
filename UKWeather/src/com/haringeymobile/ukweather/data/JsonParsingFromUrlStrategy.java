package com.haringeymobile.ukweather.data;

import java.net.URL;

public interface JsonParsingFromUrlStrategy {

	public static final int HTTP_STATUS_CODE_OK = 200;
	public static final int TIMEOUT = 4500;
	public static final String GET = "GET";

	public abstract String parseJsonString(URL url);

}
