/**
 * Author: Sam Gavis-Hughson
 * Date: 5-6-2014
 * 
 * PushData.java
 * A utility class that includes static functions to upload
 * data to SimpleDB.
 */

package com.samgavis.usagetracker;

import java.util.List;

import android.content.Context;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public class PushData {
	
	protected static boolean pushData(AmazonSimpleDBClient client, String domainPrefix, Context context) {
		return (pushCalls(client, domainPrefix, context)
				&& pushMobileData(client, domainPrefix, context)
				&& pushWifiData(client, domainPrefix, context));
	}
	
	/**
	 * Upload calls in SQLite table.
	 * @param client AmazonSimpleDBClient of DB to which to upload.
	 * @return True if successful, false if unsuccessful.
	 */
	private static boolean pushCalls(AmazonSimpleDBClient client, String domainPrefix, Context context) {
		CallListSQL mCallListSQL = new CallListSQL(context);
		CallListSDB mCallListSDB = new CallListSDB(client, domainPrefix);
		
		List<Call> calls = mCallListSQL.getCalls();
		for (Call c : calls) {
			if (!mCallListSDB.addCall(c)) {
				return false;
			}
			mCallListSQL.deleteCall(c.getTimestamp());
		}
		return true;
	}
	
	/**
	 * Upload mobile data in SQLite table.
	 * @param client AmazonSimpleDBClient of DB to which to upload.
	 * @return True if successful, false if unsuccessful.
	 */
	private static boolean pushMobileData(AmazonSimpleDBClient client, String domainPrefix, Context context) {
		MobileDataListSQL mDataListSQL = new MobileDataListSQL(context);
		MobileDataListSDB mDataListSDB = new MobileDataListSDB(client, domainPrefix);
		
		List<Data> data = mDataListSQL.getData();
		for (Data d : data) {
			if (!mDataListSDB.addData(d)) {
				return false;
			}
			mDataListSQL.deleteData(d.getTimestamp());
		}
		return true;
	}
	
	/**
	 * Upload wifi data in SQLite table.
	 * @param client AmazonSimpleDBClient of DB to which to upload.
	 * @return True if successful, false if unsuccessful.
	 */
	private static boolean pushWifiData(AmazonSimpleDBClient client, String domainPrefix, Context context) {
		WifiDataListSQL mDataListSQL = new WifiDataListSQL(context);
		WifiDataListSDB mDataListSDB = new WifiDataListSDB(client, domainPrefix);
		
		List<Data> data = mDataListSQL.getData();
		for (Data d : data) {
			if (!mDataListSDB.addData(d)) {
				return false;
			}
			mDataListSQL.deleteData(d.getTimestamp());
		}
		return true;
	}
}
