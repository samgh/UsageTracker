package com.samgavis.usagetracker;

import java.sql.Timestamp;

import android.database.Cursor;

public class ListSQLUtils {
	protected static Timestamp getTimestampForCursor(Cursor c) {
		return new Timestamp(Long.parseLong(c.getString(1)));
	}
	
	protected static String getPhoneNumberForCursor(Cursor c) {
		return c.getString(2);
	}
	
	protected static int getTypeForCursor(Cursor c) {
		return Integer.parseInt(c.getString(3));
	}
	
	protected static long getDurationForCursor(Cursor c) {
		return Long.parseLong(c.getString(4));
	}
	
	protected static double getLatForCallCursor(Cursor c) {
		return Double.parseDouble(c.getString(5));
	}
	
	protected static double getLongForCallCursor(Cursor c) {
		return Double.parseDouble(c.getString(6));
	}
	
	protected static long getUpDataForCursor(Cursor c) {
		return Long.parseLong(c.getString(2));
	}
	
	protected static long getDownDataForCursor(Cursor c) {
		return Long.parseLong(c.getString(3));
	}
	
	protected static double getLatForDataCursor(Cursor c) {
		return Double.parseDouble(c.getString(4));
	}
	
	protected static double getLongForDataCursor(Cursor c) {
		return Double.parseDouble(c.getString(5));
	}
}
