/**
 * Author: Sam Gavis-Hughson
 * Date: 4-30-2014
 * 
 * ListSQLUtils.java
 * This class contains a handful of utility functions that are
 * used by ListSQL subclasses.
 */
package com.samgavis.usagetracker;

import java.sql.Timestamp;

import android.content.ContentValues;
import android.database.Cursor;

public class ListSQLUtils {
	
	/**
	 * Generate ConventValues object for Call object.
	 * @param call Call object to be converted.
	 * @return ContentValues object containing data from call.
	 */
	protected static ContentValues generateCallContentValues(Call call) {
		String timestamp = String.valueOf(call.getTimestamp().getTime());
		String phoneNumber = call.getPhoneNumber();
		String type = String.valueOf(call.getType());
		String duration = String.valueOf(call.getDuration());
		String latitude = String.valueOf((float)call.getLatitude());
		String longitude = String.valueOf((float)call.getLongitude());
		
		ContentValues values = new ContentValues();
		
		values.put(ListSQL.TIMESTAMP_KEY, timestamp);
		values.put(ListSQL.PHONE_NUMBER_KEY, phoneNumber);
		values.put(ListSQL.TYPE_KEY, type);
		values.put(ListSQL.DURATION_KEY, duration);
		values.put(ListSQL.LAT_KEY, latitude);
		values.put(ListSQL.LONG_KEY, longitude);
		
		return values;
	}
	
	/**
	 * Generate ContentValues object for Data object.
	 * @param data Data object to be converted.
	 * @return ContentValues object containing data from data.
	 */
	protected static ContentValues generateDataContentValues(Data data) {
		String timestamp = String.valueOf(data.getTimestamp().getTime());
		String upData = String.valueOf(data.getUpData());
		String downData = String.valueOf(data.getDownData());
		String latitude = String.valueOf((float)data.getLatitude());
		String longitude = String.valueOf((float)data.getLongitude());
		
		ContentValues values = new ContentValues();
		
		values.put(ListSQL.TIMESTAMP_KEY, timestamp);
		values.put(ListSQL.UP_DATA_KEY, upData);
		values.put(ListSQL.DOWN_DATA_KEY, downData);
		values.put(ListSQL.LAT_KEY, latitude);
		values.put(ListSQL.LONG_KEY, longitude);
		
		return values;
	}
	
	/**
	 * Generate Call object from cursor.
	 * @param cursor Cursor pointing to database entry.
	 * @return Call object referenced by cursor.
	 */
	protected static Call convertCursorToCall(Cursor cursor) {
		return new Call(
				ListSQLUtils.getTimestampForCursor(cursor), 
				ListSQLUtils.getPhoneNumberForCursor(cursor), 
				ListSQLUtils.getTypeForCursor(cursor), 
				ListSQLUtils.getDurationForCursor(cursor), 
				ListSQLUtils.getLatForCallCursor(cursor), 
				ListSQLUtils.getLongForCallCursor(cursor));
	}
	
	/**
	 * Generate Data object from cursor.
	 * @param cursor Cursor pointng to database entry.
	 * @return Data object referenced by cursor.
	 */
	protected static Data convertCursorToData(Cursor cursor) {
		return new Data(
				ListSQLUtils.getTimestampForCursor(cursor),
				null,
				ListSQLUtils.getUpDataForCursor(cursor), 
				ListSQLUtils.getDownDataForCursor(cursor), 
				ListSQLUtils.getLatForDataCursor(cursor), 
				ListSQLUtils.getLongForDataCursor(cursor));
	}
	
	/**
	 * Get timestamp field from cursor.
	 */
	protected static Timestamp getTimestampForCursor(Cursor cursor) {
		return new Timestamp(Long.parseLong(cursor.getString(1)));
	}
	
	/**
	 * The following methods are for cursors that refer to Call objects.
	 */
	
	/**
	 * Get phone number field from cursor.
	 */
	protected static String getPhoneNumberForCursor(Cursor cursor) {
		return cursor.getString(2);
	}
	
	/**
	 * Get call type field from cursor.
	 */
	protected static int getTypeForCursor(Cursor cursor) {
		return Integer.parseInt(cursor.getString(3));
	}
	
	/**
	 * Get call duration field from cursor.
	 */
	protected static long getDurationForCursor(Cursor cursor) {
		return Long.parseLong(cursor.getString(4));
	}
	
	/**
	 * Get latitude field from cursor.
	 */
	protected static double getLatForCallCursor(Cursor cursor) {
		return Double.parseDouble(cursor.getString(5));
	}
	
	/**
	 * Get longitude field from cursor.
	 */
	protected static double getLongForCallCursor(Cursor cursor) {
		return Double.parseDouble(cursor.getString(6));
	}
	
	/**
	 * The following methods are for cursors that refer to Data objects.
	 */
	
	/**
	 * Get up data field from cursor.
	 */
	protected static long getUpDataForCursor(Cursor cursor) {
		return Long.parseLong(cursor.getString(2));
	}
	
	/**
	 * Get down data field from cursor.
	 */
	protected static long getDownDataForCursor(Cursor cursor) {
		return Long.parseLong(cursor.getString(3));
	}
	
	/**
	 * Get latitude field from cursor.
	 */
	protected static double getLatForDataCursor(Cursor cursor) {
		return Double.parseDouble(cursor.getString(4));
	}
	
	/**
	 * Get longitude field from cursor.
	 */
	protected static double getLongForDataCursor(Cursor cursor) {
		return Double.parseDouble(cursor.getString(5));
	}
}
