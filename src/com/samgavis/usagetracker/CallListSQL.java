package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CallListSQL extends ListSQL<Call> {
	
	private static final String TAG = "CallListSQL";
	
	public CallListSQL(Context context) {
		super(context);
	}

	protected void addCall(Call call) {
		super.addItem(call, CALL_TABLE_NAME);
	}
	
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
	
	protected List<Call> getCalls() {
		return super.getItems(CALL_TABLE_NAME);
	}

	@Override
	protected Call convertCursorToItem(Cursor cursor) {
		return new Call(
				getTimestampForCursor(cursor), 
				getPhoneNumberForCursor(cursor), 
				getTypeForCursor(cursor), 
				getDurationForCursor(cursor), 
				getLatForCursor(cursor), 
				getLongForCursor(cursor));
	}
	
	protected int getCount() {
		return super.getCount(CALL_TABLE_NAME);
	}
	
	protected void deleteCall(Timestamp timestamp) {
		super.deleteItem(timestamp, CALL_TABLE_NAME);
	}
	
	protected void deleteAll() {
		super.deleteAll(CALL_TABLE_NAME);
	}

	private Timestamp getTimestampForCursor(Cursor c) {
		return new Timestamp(Long.parseLong(c.getString(1)));
	}
	
	private String getPhoneNumberForCursor(Cursor c) {
		return c.getString(2);
	}
	
	private int getTypeForCursor(Cursor c) {
		return Integer.parseInt(c.getString(3));
	}
	
	private long getDurationForCursor(Cursor c) {
		return Long.parseLong(c.getString(4));
	}
	
	private double getLatForCursor(Cursor c) {
		return Double.parseDouble(c.getString(5));
	}
	
	private double getLongForCursor(Cursor c) {
		return Double.parseDouble(c.getString(6));
	}
}