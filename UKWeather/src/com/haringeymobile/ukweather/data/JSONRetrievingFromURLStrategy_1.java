package com.haringeymobile.ukweather.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class JSONRetrievingFromURLStrategy_1 implements
		JSONRetrievingFromURLStrategy {

	public String retrieveJSONString(URL url) {
		HttpURLConnection connection = getConnection(url);
		InputStream inputStream = getInputStream(connection);
		if (inputStream == null) {
			return null;
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		StringBuilder stringBuilder = readData(bufferedReader);
		closeInputStream(inputStream);
		connection.disconnect();
		return stringBuilder.toString();
	}

	private HttpURLConnection getConnection(URL url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			dealWithException(e);
		}
		try {
			connection.setRequestMethod(GET);
		} catch (ProtocolException e) {
			dealWithException(e);
		}
		connection.setDoInput(true);
		// See http://developer.android.com/reference/java/net/URLConnection.html#setConnectTimeout(int)
		connection.setConnectTimeout(TIMEOUT);
		connection.setReadTimeout(TIMEOUT);
		try {
			connection.connect();
		} catch (IOException e) {
			dealWithException(e);
		}
		return connection;
	}

	private void dealWithException(IOException e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

	private InputStream getInputStream(HttpURLConnection connection) {
		InputStream inputStream = null;
		try {
			inputStream = connection.getInputStream();
		} catch (IOException e) {
			dealWithException(e);
		}
		return inputStream;
	}

	private StringBuilder buildString(BufferedReader bufferedReader) {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
		} catch (IOException e) {
			dealWithException(e);
		}
		return stringBuilder;
	}

	private StringBuilder readData(BufferedReader bufferedReader) {
		StringBuilder stringBuilder = null;
		stringBuilder = buildString(bufferedReader);
		try {
			bufferedReader.close();
		} catch (IOException e) {
			dealWithException(e);
		}
		return stringBuilder;
	}

	private void closeInputStream(InputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			dealWithException(e);
		}
	}

}
