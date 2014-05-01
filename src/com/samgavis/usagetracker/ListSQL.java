/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * ListSQL.java
 * Subclass of SQLiteOpenHelper that creates all of the local
 * SQL database tables. This also contains generic logic for 
 * adding and removing items from the database.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class ListSQL<Item> extends SQLiteOpenHelper {
	
	/**
	 * Database-specific info
	 */
	protected static final int SQL_DB_VERSION = 1;
	protected static final String SQL_DB_NAME = "rightcallLocal";
	
	protected static final String MOBILE_TABLE_NAME = "mobile_data";
	protected static final String WIFI_TABLE_NAME = "wifi_data";
	protected static final String CALL_TABLE_NAME = "call_data";
	
	/**
	 * Keys that are used by all subclasses. All of these
	 * keys must be included in this class to properly instantiate
	 * databases.
	 */
	protected static final String TIMESTAMP_KEY = "timestamp";
	protected static final String ID = "id";
	protected static final String LAT_KEY = "lat";
	protected static final String LONG_KEY = "long";
	
	/**
	 * Data-specific keys.
	 */
	protected static final String UP_DATA_KEY = "updata";
	protected static final String DOWN_DATA_KEY = "downdata";
	
	/**
	 * Call-specific keys.
	 */
	protected static final String PHONE_NUMBER_KEY = "number";
	protected static final String TYPE_KEY = "type";
	protected static final String DURATION_KEY = "duration";

	/**
	 * Public constructor.
	 * @param context Current context.
	 */
	public ListSQL(Context context) {
		super(context, SQL_DB_NAME, null, SQL_DB_VERSION);
	}

	/**
	 * Overrides SQLiteOpenHelper's onCreate method. Creates all local
	 * databases if necessary.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MOBILE_DATA_TABLE = "CREATE TABLE " + MOBILE_TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TIMESTAMP_KEY + " TEXT,"
                + UP_DATA_KEY + " TEXT," + DOWN_DATA_KEY + " TEXT," 
                + LAT_KEY + " TEXT," + LONG_KEY + " TEXT" + ")";
		String CREATE_WIFI_DATA_TABLE = "CREATE TABLE " + WIFI_TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TIMESTAMP_KEY + " TEXT,"
                + UP_DATA_KEY + " TEXT," + DOWN_DATA_KEY + " TEXT," 
                + LAT_KEY + " TEXT," + LONG_KEY + " TEXT" + ")";
		String CREATE_CALL_DATA_TABLE = "CREATE TABLE " + CALL_TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TIMESTAMP_KEY + " TEXT," 
				+ PHONE_NUMBER_KEY + " TEXT," + TYPE_KEY + " TEXT," 
                + DURATION_KEY + " TEXT," + LAT_KEY + " TEXT," 
                + LONG_KEY + " TEXT" + ")";
        db.execSQL(CREATE_MOBILE_DATA_TABLE);
        db.execSQL(CREATE_WIFI_DATA_TABLE);
        db.execSQL(CREATE_CALL_DATA_TABLE);
		
	}

	/**
	 * Create new tables on Upgrade.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       db.execSQL("DROP TABLE IF EXISTS " + MOBILE_TABLE_NAME);
	       db.execSQL("DROP TABLE IF EXISTS " + WIFI_TABLE_NAME);
	       db.execSQL("DROP TABLE IF EXISTS " + CALL_TABLE_NAME);
	       onCreate(db);
	}
	
	/**
	 * Add an item to the local database.
	 * @param item Item to be added to the database.
	 * @param tableName Name of the table to add the Item to.
	 */
	protected void addItem(Item item, String tableName) {
		ContentValues values = generateValues(item);
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(tableName, null, values);
	    db.close();
	}
	
	/**
	 * Helper method converts item into ContentValues object that
	 * can be added to the SQLite database.
	 * @param item Item to be converted into ContentValues object.
	 * @return ContentValues object containing the values from item.
	 */
	protected abstract ContentValues generateValues(Item item);
	
	/**
	 * Get a list of all of the items in table.
	 * @param tableName Name of the table to query.
	 * @return List of items in table with oldest items first.
	 */
	protected List<Item> getItems(String tableName) {
		List<Item> itemList = new ArrayList<Item>();
		String query = "SELECT * FROM " + tableName + " ORDER BY " + TIMESTAMP_KEY;
		
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
	    
	    if (cursor.moveToFirst()) {
	    	do {
	    		itemList.add(convertCursorToItem(cursor));
	    	} while (cursor.moveToNext());
	    }
	    
	    return itemList;
	}
	
	/**
	 * Helper method converts cursor into Item.
	 * @param cursor Cursor to convert.
	 * @return Item pointed to by cursor.
	 */
	protected abstract Item convertCursorToItem(Cursor cursor);
	
	/**
	 * Get the number of items in table.
	 * @param tableName Name of the table to query.
	 * @return Number of items in table.
	 */
	protected int getCount(String tableName) {
		String query = "SELECT  * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
 
        return cursor.getCount();
	}
	
	/**
	 * Delete item with given timestamp from table.
	 * @param timestamp Timestamp of item to delete from table.
	 * @param tableName Name of the table to query.
	 */
	protected void deleteItem(Timestamp timestamp, String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName, TIMESTAMP_KEY + "=" + timestamp.getTime(), null);
		db.close();
	}
	
	/**
	 * Delete all items from table.
	 * @param tableName Name of the table to query.
	 */
	protected void deleteAll(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(tableName, null, null);
	    db.close();
	}
}

