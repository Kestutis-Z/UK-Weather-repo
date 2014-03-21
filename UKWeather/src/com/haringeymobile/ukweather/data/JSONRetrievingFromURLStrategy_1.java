package com.haringeymobile.ukweather.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONRetrievingFromURLStrategy_1 implements
		JSONRetrievingFromURLStrategy {

	private static final int TIMEOUT = 4500;
	private static final String GET = "GET";

	public String retrieveJSONString(URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(GET);
			connection.setRequestProperty("Content-length", "0");
			connection.setUseCaches(false);
			connection.setAllowUserInteraction(false);
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			connection.connect();
			int status = connection.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}

		} catch (MalformedURLException ex) {
			// TODO
		} catch (IOException ex) {
		}
		return null;
	}

}
