/**
 * Author: Sam Gavis-Hughson
 * Date: 4-10-2014
 * 
 * CallListSQL.java
 * Subclass of ListSQL that implements methods for storing
 * Call objects in the appropriate table.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CallListSQL extends ListSQL<Call> {
	
	private static final String TAG = "CallListSQL";
	
	/**
	 * Public constructor.
	 * @param context Current context.
	 */
	public CallListSQL(Context context) {
		super(context);
	}

	/**
	 * Adds call object to call table.
	 * @param call Call to add to table.
	 */
	protected void addCall(Call call) {
		super.addItem(call, CALL_TABLE_NAME);
	}
	
	/**
	 * Generates a ConentValues object using call to be 
	 * inserted into database.
	 */
	@Override
	protected ContentValues generateValues(Call call) {
		String timestamp = String.valueOf(call.getTimestamp().getTime());
		String phoneNumber = call.getPhoneNumber();
		String type = String.valueOf(call.getType());
		String duration = String.valueOf(call.getDuration());
		String lattitude = String.valueOf((float)call.getLatitude());
		String longitude = String.valueOf((float)call.getLongitude());
		
		ContentValues values = new ContentValues();
		
		values.put(TIMESTAMP_ATTRIBUTE, timestamp);
		values.put(PHONE_NUMBER_ATTRIBUTE, phoneNumber);
		values.put(TYPE_ATTRIBUTE, type);
		values.put(DURATION_ATTRIBUTE, duration);
		values.put(LAT_ATTRIBUTE, lattitude);
		values.put(LONG_ATTRIBUTE, longitude);
		
		return values;
	}
	
	/**
	 * Get a list of all of the calls from the database.
	 * @return List of calls. Ordered oldest Calls first.
	 */
	protected List<Call> getCalls() {
		return super.getItems(CALL_TABLE_NAME);
	}

	/**
	 * Converts cursor into Call object.
	 */
	@Override
	protected Call convertCursorToItem(Cursor cursor) {
		return new Call(
				ListSQLUtils.getTimestampForCursor(cursor), 
				ListSQLUtils.getPhoneNumberForCursor(cursor), 
				ListSQLUtils.getTypeForCursor(cursor), 
				ListSQLUtils.getDurationForCursor(cursor), 
				ListSQLUtils.getLatForCallCursor(cursor), 
				ListSQLUtils.getLongForCallCursor(cursor));
	}
	
	/**
	 * Get number of items in table.
	 * @return Number of items in table.
	 */
	protected int getCount() {
		return super.getCount(CALL_TABLE_NAME);
	}
	
	/**
	 * Delete call with given timestamp.
	 * @param timestamp Timestamp of the call to delete from table.
	 */
	protected void deleteCall(Timestamp timestamp) {
		super.deleteItem(timestamp, CALL_TABLE_NAME);
	}
	
	/**
	 * Delete all calls from table.
	 */
	protected void deleteAll() {
		super.deleteAll(CALL_TABLE_NAME);
	}
}