package com.haringeymobile.ukweather.data;

import java.net.URL;

public interface JSONRetrievingFromURLStrategy {

	public abstract String retrieveJSONString(URL url);

}
