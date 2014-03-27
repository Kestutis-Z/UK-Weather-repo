package com.haringeymobile.ukweather.datastorage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SQLOperation {

	/**
	 * Time period for which the weather data is considered "fresh". By default
	 * it is 10 minutes
	 */
	private static final int WEATHER_DATA_STORAGE_TIME = 10;
	private Context context;

	public SQLOperation(Context context) {
		this.context = context;
	}

	public void insertNewCity(int cityId, String cityName, long date,
			String currentWeather, String weatherForecast) {
		ContentValues newValues = new ContentValues();
		newValues.put(CityTable.COLUMN_CITY_ID, cityId);
		newValues.put(CityTable.COLUMN_NAME, cityName);
		newValues.put(CityTable.COLUMN_LAST_QUERY_DATE, date);
		newValues.put(CityTable.COLUMN_CACHED_JSON_CURRENT, currentWeather);
		newValues.put(CityTable.COLUMN_CACHED_JSON_FORECAST, weatherForecast);
		context.getContentResolver().insert(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS, newValues);
	}

	/**
	 * Updates city record if it already exists in the database, otherwise
	 * inserts new record.
	 * 
	 * @param cityId
	 *            Open Weather Map city ID
	 * @param cityName
	 *            Open Weather Map city name
	 * @param date
	 *            Time when the record inserted or last updated
	 * @param currentWeather
	 *            Json string for the current city weather
	 * @param weatherForecast
	 *            Json string for the weather forecast
	 */
	public void insertOrUpdateCity(int cityId, String cityName, long date,
			String currentWeather, String weatherForecast) {
		Cursor cursor = getCursorWithCityId(cityId);
		if (cursor == null) {
			return;
		}
		boolean cityIdExists = cursor.moveToFirst();
		if (cityIdExists) {
			Uri rowUri = getRowUri(cursor);
			ContentValues newValues = getContentValuesWithCurrentDateAndWeather(currentWeather);
			context.getContentResolver().update(rowUri, newValues, null, null);
			cursor.close();
		} else {
			insertNewCity(cityId, cityName, date, currentWeather,
					weatherForecast);
		}
	}

	private Cursor getCursorWithCityId(int cityId) {
		if (context == null) {
			return null;
		}
		Cursor cursor = context.getContentResolver().query(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS,
				new String[] { CityTable._ID, CityTable.COLUMN_CITY_ID, },
				CityTable.COLUMN_CITY_ID + "=?",
				new String[] { Integer.toString(cityId) }, null);
		return cursor;
	}

	private Uri getRowUri(Cursor cursor) {
		int columnIndex = cursor.getColumnIndexOrThrow(CityTable._ID);
		long rowId = cursor.getLong(columnIndex);
		Uri userRowUri = ContentUris.withAppendedId(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS, rowId);
		return userRowUri;
	}

	private ContentValues getContentValuesWithCurrentDateAndWeather(
			String jsonString) {
		ContentValues newValues = new ContentValues();
		newValues.put(CityTable.COLUMN_LAST_QUERY_DATE,
				System.currentTimeMillis());
		newValues.put(CityTable.COLUMN_CACHED_JSON_CURRENT, jsonString);
		return newValues;
	}

	public void updateCurrentWeather(int cityId, String jsonString) {
		Cursor cursor = getCursorWithCurrentWeather(cityId);
		if (cursor == null) {
			return;
		}
		if (!cursor.moveToFirst()) {
			cursor.close();
			return;
		}

		Uri rowUri = getRowUri(cursor);
		ContentValues newValues = getContentValuesWithCurrentDateAndWeather(jsonString);
		context.getContentResolver().update(rowUri, newValues, null, null);
		cursor.close();
	}

	private Cursor getCursorWithCurrentWeather(int cityId) {
		if (context == null) {
			return null;
		}
		Cursor cursor = context.getContentResolver().query(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS,
				new String[] { CityTable._ID, CityTable.COLUMN_LAST_QUERY_DATE,
						CityTable.COLUMN_CACHED_JSON_CURRENT },
				CityTable.COLUMN_CITY_ID + "=?",
				new String[] { Integer.toString(cityId) }, null);
		return cursor;
	}

	public String getJSONStringForCurrentWeather(int cityId) {
		Cursor cursor = getCursorWithCurrentWeather(cityId);
		if (cursor == null) {
			return null;
		}
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		String currentWeatherJson = getJsonStringForCurrentWeather(cursor);
		cursor.close();
		return currentWeatherJson;
	}

	private String getJsonStringForCurrentWeather(Cursor cursor) {
		String currentWeatherJson = null;
		if (!recordNeedsToBeUpdated(cursor)) {
			int columnIndexForCurrentWeather = cursor
					.getColumnIndexOrThrow(CityTable.COLUMN_CACHED_JSON_CURRENT);
			currentWeatherJson = cursor.getString(columnIndexForCurrentWeather);
		}
		return currentWeatherJson;
	}

	private boolean recordNeedsToBeUpdated(Cursor cursor) {
		int columnIndexForDate = cursor
				.getColumnIndexOrThrow(CityTable.COLUMN_LAST_QUERY_DATE);
		long lastUpdateTime = cursor.getLong(columnIndexForDate);
		if (lastUpdateTime == CityTable.CITY_NEVER_QUERIED) {
			return true;
		} else {
			long currentTime = System.currentTimeMillis();
			return currentTime - lastUpdateTime > getWeatherDataStorageLimit();
		}
	}

	private long getWeatherDataStorageLimit() {
		// TODO Let user choose the limit
		return WEATHER_DATA_STORAGE_TIME * 60 * 1000;
	}

}
