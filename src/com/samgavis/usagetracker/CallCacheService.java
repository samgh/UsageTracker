/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * CallCacheService.java
 * An IntentService that caches Call objects to local SQLite
 * database.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;

public class CallCacheService extends IntentService {

	private static final String TAG = "CallCacheService";
	
	public CallCacheService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null, null);
		int iNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER ); 
		int iType = cursor.getColumnIndex(CallLog.Calls.TYPE );
		int iDate = cursor.getColumnIndex(CallLog.Calls.DATE);
		int iDuration = cursor.getColumnIndex(CallLog.Calls.DURATION);
		
		String number = cursor.getString(iNumber);
		int type = Integer.parseInt(cursor.getString(iType));
		Timestamp date = new Timestamp(Long.parseLong(cursor.getString(iDate)));
		long duration = Long.parseLong(cursor.getString(iDuration));
		
		if (cursor.moveToLast()) {
			Call call = new Call(date, number, type, duration, this);
			(new CallListSQL(this)).addCall(call);
		}
	}	
}
