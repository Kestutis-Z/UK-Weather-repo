package com.haringeymobile.ukweather.data;

import java.net.URL;

public interface JSONRetrievingFromURLStrategy {

	public static final int TIMEOUT = 4500;
	public static final String GET = "GET";

	public abstract String retrieveJSONString(URL url);

}
