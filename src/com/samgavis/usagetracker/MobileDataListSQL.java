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

	@Override
	protected ContentValues generateValues(Data data) {
		String timestamp = String.valueOf(data.getTimestamp().getTime());
		String upData = String.valueOf(data.getUpData());
		String downData = String.valueOf(data.getDownData());
		String lattitude = String.valueOf((float)data.getLatitude());
		String longitude = String.valueOf((float)data.getLongitude());
		
		ContentValues values = new ContentValues();
		
		values.put(TIMESTAMP_ATTRIBUTE, timestamp);
		values.put(UP_DATA_ATTRIBUTE, upData);
		values.put(DOWN_DATA_ATTRIBUTE, downData);
		values.put(LAT_ATTRIBUTE, lattitude);
		values.put(LONG_ATTRIBUTE, longitude);
		
		return values;
	}
	
	protected List<Data> getData() {
		return super.getItems(MOBILE_TABLE_NAME);
	}

	@Override
	protected Data convertCursorToItem(Cursor cursor) {
		return new Data(
				ListSQLUtils.getTimestampForCursor(cursor),
				null,
				ListSQLUtils.getUpDataForCursor(cursor), 
				ListSQLUtils.getDownDataForCursor(cursor), 
				ListSQLUtils.getLatForDataCursor(cursor), 
				ListSQLUtils.getLongForDataCursor(cursor));
	}

	protected int getCount() {
		return super.getCount(MOBILE_TABLE_NAME);
	}
	
	protected void deleteCall(Timestamp timestamp) {
		super.deleteItem(timestamp, MOBILE_TABLE_NAME);
	}
	
	protected void deleteAll() {
		super.deleteAll(CALL_TABLE_NAME);
	}
	
}
