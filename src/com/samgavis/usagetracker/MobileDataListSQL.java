/**
 * Author: Sam Gavis-Hughson
 * Date: 4-10-2014
 * 
 * MobileDataListSQL.java
 * Subclass of ListSQL that implements methods for storing
 * Data objects in the Mobile Data table.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MobileDataListSQL extends ListSQL<Data> {
	
	private static final String TAG = "MobileDataListSQL";

	/**
	 * Public constructor.
	 * @param context Current context.
	 */
	public MobileDataListSQL(Context context) {
		super(context);
	}
	
	/**
	 * Adds data object to mobile data table.
	 * @param data Data to add to table.
	 */
	protected void addData(Data data) {
		super.addItem(data, MOBILE_TABLE_NAME);
	}

	/**
	 * Generates a ConentValues object using data to be 
	 * inserted into database.
	 */
	@Override
	protected ContentValues generateValues(Data data) {
		return ListSQLUtils.generateDataContentValues(data);
	}
	
	/**
	 * Get a list of all of the data from the database.
	 * @return List of data. Ordered oldest Data first.
	 */
	protected List<Data> getData() {
		return super.getItems(MOBILE_TABLE_NAME);
	}

	/**
	 * Converts cursor into Data object.
	 */
	@Override
	protected Data convertCursorToItem(Cursor cursor) {
		return ListSQLUtils.convertCursorToData(cursor);
	}

	/**
	 * Get number of data objects in table.
	 * @return Number of data objects in table.
	 */
	protected int getCount() {
		return super.getCount(MOBILE_TABLE_NAME);
	}
	
	/**
	 * Delete data with given timestamp.
	 * @param timestamp Timestamp of the data to delete from table.
	 */
	protected void deleteData(Timestamp timestamp) {
		super.deleteItem(timestamp, MOBILE_TABLE_NAME);
	}
	
	/**
	 * Delete all data from table.
	 */
	protected void deleteAll() {
		super.deleteAll(MOBILE_TABLE_NAME);
	}
	
}
