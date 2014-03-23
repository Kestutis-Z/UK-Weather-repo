package com.haringeymobile.ukweather.datastorage;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CityTable implements BaseColumns {

	public static final String TABLE_CITIES = "Cities";
	public static final String COLUMN_CITY_ID = "Id";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_LAST_QUERY_DATE = "Date";	
	public static final String COLUMN_CACHED_JSON_CURRENT = "Current";
	public static final String COLUMN_CACHED_JSON_FORECAST = "Forecast";	
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CITIES
			+ "(" 
			+ _ID + " integer primary key autoincrement, " 
			+ COLUMN_CITY_ID + " integer, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_LAST_QUERY_DATE + " integer, "
			+ COLUMN_CACHED_JSON_CURRENT + " text, "
			+ COLUMN_CACHED_JSON_FORECAST + " text"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
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
