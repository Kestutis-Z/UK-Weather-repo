package com.haringeymobile.ukweather.datastorage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.haringeymobile.ukweather.data.CityUK;

public class CityTable implements BaseColumns {

	public static final int CITY_NEVER_QUERIED = -1;
	public static final int CITY_ID_DOES_NOT_EXIST = -1;

	public static final String TABLE_CITIES = "Cities";
	public static final String COLUMN_CITY_ID = "Id";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_LAST_QUERY_DATE = "Date";
	public static final String COLUMN_CACHED_JSON_CURRENT = "Current";
	public static final String COLUMN_CACHED_JSON_FORECAST = "Forecast";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CITIES + "(" + _ID + " integer primary key autoincrement, "
			+ COLUMN_CITY_ID + " integer, " + COLUMN_NAME + " text not null, "
			+ COLUMN_LAST_QUERY_DATE + " integer, "
			+ COLUMN_CACHED_JSON_CURRENT + " text, "
			+ COLUMN_CACHED_JSON_FORECAST + " text" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		insertInitialData(database);
	}

	private static void insertInitialData(SQLiteDatabase database) {
		for (CityUK city : CityUK.values()) {
			ContentValues newValues = new ContentValues();
			newValues.put(CityTable.COLUMN_CITY_ID, city.getOpenWeatherMapId());
			newValues.put(CityTable.COLUMN_NAME, city.getDisplayName());
			newValues.put(CityTable.COLUMN_LAST_QUERY_DATE, CITY_NEVER_QUERIED);
			database.insert(TABLE_CITIES, null, newValues);
		}

	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(CityTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
		onCreate(database);
	}

}
