package com.haringeymobile.ukweather.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.haringeymobile.ukweather.WeatherInfoType;

public class SqlOperation {

	/**
	 * Time period for which the weather data is considered "fresh". By default
	 * it is 10 minutes
	 */
	private static final int WEATHER_DATA_STORAGE_TIME = 10;

	private Context context;
	private String columnNameForJsonString;
	private String columnNameForLastQueryTime;

	public SqlOperation(Context context, WeatherInfoType weatherInfoType) {
		this.context = context;
		switch (weatherInfoType) {
		case CURRENT_WEATHER:
			columnNameForJsonString = CityTable.COLUMN_CACHED_JSON_CURRENT;
			columnNameForLastQueryTime = CityTable.COLUMN_LAST_QUERY_TIME_FOR_CURRENT_WEATHER;
			break;
		case DAILY_WEATHER_FORECAST:
			columnNameForJsonString = CityTable.COLUMN_CACHED_JSON_DAILY_FORECAST;
			columnNameForLastQueryTime = CityTable.COLUMN_LAST_QUERY_TIME_FOR_DAILY_WEATHER_FORECAST;
			break;
		case THREE_HOURLY_WEATHER_FORECAST:
			columnNameForJsonString = CityTable.COLUMN_CACHED_JSON_THREE_HOURLY_FORECAST;
			columnNameForLastQueryTime = CityTable.COLUMN_LAST_QUERY_TIME_FOR_THREE_HOURLY_WEATHER_FORECAST;
			break;
		default:
			throw new IllegalArgumentException("Unsupported weatherInfoType: "
					+ weatherInfoType);
		}
	}

	/**
	 * Updates city record if it already exists in the database, otherwise
	 * inserts new record.
	 * 
	 * @param cityId
	 *            Open Weather Map city ID
	 * @param cityName
	 *            Open Weather Map city name
	 * @param currentWeather
	 *            Json string for the current city weather
	 */
	public void updateOrInsertCityWithCurrentWeather(int cityId,
			String cityName, String currentWeather) {
		Cursor cursor = getCursorWithCityId(cityId);
		if (cursor == null) {
			return;
		}
		boolean cityIdExists = cursor.moveToFirst();
		if (cityIdExists) {
			Uri rowUri = getRowUri(cursor);
			ContentValues newValues = createContentValuesWithDateAndWeatherJsonString(currentWeather);
			context.getContentResolver().update(rowUri, newValues, null, null);
			cursor.close();
		} else {
			insertNewCityWithCurrentWeather(cityId, cityName, currentWeather);
		}
	}

	public void insertNewCityWithCurrentWeather(int cityId, String cityName,
			String currentWeather) {
		ContentValues newValues = new ContentValues();
		newValues.put(CityTable.COLUMN_CITY_ID, cityId);
		newValues.put(CityTable.COLUMN_NAME, cityName);
		long currentTime = System.currentTimeMillis();
		newValues.put(CityTable.COLUMN_LAST_QUERY_TIME_FOR_CURRENT_WEATHER,
				currentTime);
		newValues.put(CityTable.COLUMN_LAST_OVERALL_QUERY_TIME, currentTime);
		newValues.put(CityTable.COLUMN_CACHED_JSON_CURRENT, currentWeather);
		newValues.put(
				CityTable.COLUMN_LAST_QUERY_TIME_FOR_DAILY_WEATHER_FORECAST,
				CityTable.CITY_NEVER_QUERIED);
		newValues.putNull(CityTable.COLUMN_CACHED_JSON_DAILY_FORECAST);
		newValues
				.put(CityTable.COLUMN_LAST_QUERY_TIME_FOR_THREE_HOURLY_WEATHER_FORECAST,
						CityTable.CITY_NEVER_QUERIED);
		newValues.putNull(CityTable.COLUMN_CACHED_JSON_THREE_HOURLY_FORECAST);
		context.getContentResolver().insert(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS, newValues);
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

	public void updateWeatherInfo(int cityId, String jsonString) {
		Cursor cursor = getCursorWithWeatherInfo(cityId);
		if (cursor == null) {
			return;
		}
		if (!cursor.moveToFirst()) {
			cursor.close();
			return;
		}
		Uri rowUri = getRowUri(cursor);
		ContentValues newValues = createContentValuesWithDateAndWeatherJsonString(jsonString);
		context.getContentResolver().update(rowUri, newValues, null, null);
		cursor.close();
	}

	private Cursor getCursorWithWeatherInfo(int cityId) {
		if (context == null) {
			return null;
		}
		Cursor cursor = context.getContentResolver().query(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS,
				new String[] { CityTable._ID, columnNameForLastQueryTime,
						columnNameForJsonString,
						CityTable.COLUMN_LAST_OVERALL_QUERY_TIME },
				CityTable.COLUMN_CITY_ID + "=?",
				new String[] { Integer.toString(cityId) }, null);
		return cursor;
	}

	private ContentValues createContentValuesWithDateAndWeatherJsonString(
			String jsonString) {
		ContentValues newValues = new ContentValues();
		long currentTime = System.currentTimeMillis();
		newValues.put(columnNameForLastQueryTime, currentTime);
		newValues.put(CityTable.COLUMN_LAST_OVERALL_QUERY_TIME, currentTime);
		newValues.put(columnNameForJsonString, jsonString);
		return newValues;
	}

	public String getJsonStringForWeatherInfo(int cityId) {
		Cursor cursor = getCursorWithWeatherInfo(cityId);
		if (cursor == null) {
			return null;
		}
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		String weatherInfoJson = getJsonStringForWeatherInfo(cursor);
		cursor.close();
		return weatherInfoJson;
	}

	private String getJsonStringForWeatherInfo(Cursor cursor) {
		String weatherInfoJsonString = null;
		if (!recordNeedsToBeUpdatedForWeatherInfo(cursor)) {
			int columnIndexForWeatherInfo = cursor
					.getColumnIndexOrThrow(columnNameForJsonString);
			weatherInfoJsonString = cursor.getString(columnIndexForWeatherInfo);
		}
		return weatherInfoJsonString;
	}

	private boolean recordNeedsToBeUpdatedForWeatherInfo(Cursor cursor) {
		int columnIndexForDate = cursor
				.getColumnIndexOrThrow(columnNameForLastQueryTime);
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

	public void deleteCity(int cityId) {
		context.getContentResolver().delete(
				WeatherContentProvider.CONTENT_URI_CITY_RECORDS,
				CityTable.COLUMN_CITY_ID + "=?",
				new String[] { Integer.toString(cityId) });
	}

}
