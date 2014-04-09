package com.haringeymobile.ukweather.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.haringeymobile.ukweather.utils.MiscMethods;

public class JsonParsingFromUrlUsingHttpConnection implements
		JsonParsingFromUrlStrategy {

	public String parseJsonString(URL url) {
		StringBuilder stringBuilder = null;
		try {
			HttpURLConnection connection = getConnection(url);
			int responseCode = connection.getResponseCode();
			InputStream inputStream = null;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
			}
			if (inputStream == null) {
				return null;
			}
			stringBuilder = readData(inputStream);
			inputStream.close();
			connection.disconnect();
		} catch (MalformedURLException e) {
			MiscMethods.log("MalformedURLException");
			return null;
		} catch (IOException e) {
			MiscMethods.log("IOException");
			return null;
		}
		return stringBuilder.toString();
	}

	private HttpURLConnection getConnection(URL url) throws IOException {
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(GET);
		connection.setDoInput(true);
		connection.setConnectTimeout(TIMEOUT);
		connection.setReadTimeout(TIMEOUT);
		connection.connect();
		return connection;
	}

	private StringBuilder readData(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		StringBuilder stringBuilder = buildString(bufferedReader);
		inputStream.close();
		return stringBuilder;
	}

	private StringBuilder buildString(BufferedReader bufferedReader)
			throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}
		return stringBuilder;
	}

}
