package com.haringeymobile.ukweather.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WeatherContentProvider extends ContentProvider {

	private DatabaseHelper databaseHelper;

	// Used for the UriMacher
	private static final int CITIES_ALL_ROWS = 1;
	private static final int CITIES_SINGLE_ROW = 2;

	private static final String AUTHORITY = "com.haringeymobile.ukweather.provider";

	private static final String PATH_CITY_RECORDS = CityTable.TABLE_CITIES;

	public static final Uri CONTENT_URI_CITY_RECORDS = Uri.parse("content://"
			+ AUTHORITY + "/" + PATH_CITY_RECORDS);

	private static final String PROVIDER_SPECIFIC_SUBTYPE_FOR_CITY_RECORDS = "/vnd."
			+ AUTHORITY + "." + CityTable.TABLE_CITIES;

	public static final String CONTENT_TYPE_CITY_RECORDS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ PROVIDER_SPECIFIC_SUBTYPE_FOR_CITY_RECORDS;
	public static final String CONTENT_ITEM_TYPE_CITY_RECORDS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ PROVIDER_SPECIFIC_SUBTYPE_FOR_CITY_RECORDS;

	private static final UriMatcher myURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		myURIMatcher.addURI(AUTHORITY, PATH_CITY_RECORDS, CITIES_ALL_ROWS);
		myURIMatcher.addURI(AUTHORITY, PATH_CITY_RECORDS + "/#",
				CITIES_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db;
		try {
			db = databaseHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = databaseHelper.getReadableDatabase();
		}

		String groupBy = null;
		String having = null;
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (myURIMatcher.match(uri)) {
		case CITIES_SINGLE_ROW:
			queryBuilder.appendWhere(CityTable._ID + "="
					+ uri.getLastPathSegment());
			// fall through
		case CITIES_ALL_ROWS:
			queryBuilder.setTables(CityTable.TABLE_CITIES);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (myURIMatcher.match(uri)) {
		case CITIES_SINGLE_ROW:
			return CONTENT_ITEM_TYPE_CITY_RECORDS;
		case CITIES_ALL_ROWS:
			return CONTENT_TYPE_CITY_RECORDS;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		String nullColumnHack = null;
		long id;
		switch (myURIMatcher.match(uri)) {
		case CITIES_ALL_ROWS:
			id = db.insert(CityTable.TABLE_CITIES, nullColumnHack, values);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		if (id > -1) {
			Uri insertedIdUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(insertedIdUri, null);
			return insertedIdUri;
		}

		throw new SQLException("Could not insert into table for uri: " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (myURIMatcher.match(uri)) {
		case CITIES_SINGLE_ROW:
			String id_rec = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(CityTable.TABLE_CITIES, CityTable._ID
						+ "=" + id_rec, null);
			} else {
				rowsDeleted = db.delete(CityTable.TABLE_CITIES, CityTable._ID
						+ "=" + id_rec + " and " + selection, selectionArgs);
			}
			break;
		case CITIES_ALL_ROWS:
			rowsDeleted = db.delete(CityTable.TABLE_CITIES, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (myURIMatcher.match(uri)) {
		case CITIES_SINGLE_ROW:
			String id_rec = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(CityTable.TABLE_CITIES, values,
						CityTable._ID + "=" + id_rec, null);
			} else {
				rowsUpdated = db.update(CityTable.TABLE_CITIES, values,
						CityTable._ID + "=" + id_rec + " and " + selection,
						selectionArgs);
			}
			break;
		case CITIES_ALL_ROWS:
			rowsUpdated = db.update(CityTable.TABLE_CITIES, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
