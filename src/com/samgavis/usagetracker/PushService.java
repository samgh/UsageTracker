/**
 * Author: Sam Gavis-Hughson
 * Date: 4-10-2014
 * 
 * PushService.java
 * An IntentService that executes pushes cached data to SDB instance
 */

package com.samgavis.usagetracker;

import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public abstract class PushService extends IntentService {
	private static final String TAG = "PushDataService";
	
	private static final int REQUEST_CODE = 1;
	
	protected String domainPrefix = "user";
	
	public PushService() {
		super(TAG);
	}
	
	/**
	 * Intent handler.
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onHandleIntent(Intent intent) {
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
		if (!isNetworkAvailable) {
			pushError();
			return;
		}
		
		AmazonSimpleDBClient client = getSimpleDBClient(domainPrefix);

		if (!(pushCalls(client) && pushMobileData(client) && pushWifiData(client))) {
			pushError();
		}
	}
	
	/**
	 * Upload calls in SQLite table.
	 * @param client AmazonSimpleDBClient of DB to which to upload.
	 * @return True if successful, false if unsuccessful.
	 */
	private boolean pushCalls(AmazonSimpleDBClient client) {
		CallListSQL mCallListSQL = new CallListSQL(this);
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
	private boolean pushMobileData(AmazonSimpleDBClient client) {
		MobileDataListSQL mDataListSQL = new MobileDataListSQL(this);
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
	private boolean pushWifiData(AmazonSimpleDBClient client) {
		WifiDataListSQL mDataListSQL = new WifiDataListSQL(this);
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
	
	/**
	 * Abstract class that gets an instance of AmazonSimpleDBClient. This
	 * is called every time an intent is received.
	 * @param prefix Domain prefix.
	 * @return AmazonSimpleDBClient generated.
	 */
	public abstract AmazonSimpleDBClient getSimpleDBClient(String prefix);
	
	/**
	 * This is the error handler. It calls the user-defined error handler
	 * and retries pushing data if return value is true.
	 */
	private void pushError() {
		if (handleError()) {
			AmazonSimpleDBClient client = getSimpleDBClient(domainPrefix);
			pushCalls(client);
			pushMobileData(client);
			pushWifiData(client);
		}
	}
	
	/**
	 * Handle error. This should generally involve ensuring that getSimpleDBClient()
	 * will return a valid client ie. updating session keys or other verification data.
	 * @return True means that push will be retried after calling handleError(). False
	 * means that it will not be retried.
	 */
	public abstract boolean handleError();
	
	/**
	 * Start/stop Alarm. It is highly recommended to call this from a Startup broadcast 
	 * receiver or else it may not be running. This may cause the local cache to fill up.
	 * @param context Current context.
	 * @param isOn Value to be set for alarm.
	 * @param pollInterval How often PendingIntents should be sent.
	 */
	public static void setServiceAlarm(Context context, boolean isOn, int pollInterval) {
		Log.d(TAG, "Setting SDB Alarm");
		Intent i = new Intent(context, PushService.class);
		PendingIntent pi = PendingIntent.getService(context,  REQUEST_CODE,  i, 0);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		if (isOn) {
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), pollInterval, pi);
		} else {
			alarmManager.cancel(pi);
			pi.cancel();
		}
	}
	
	/**
	 * Get whether or not alarm is on.
	 * @param context Current context.
	 * @return True if alarm is on, false otherwise.
	 */
	public static boolean isServiceAlarmOn(Context context) {
		Intent i = new Intent(context, PushService.class);
		PendingIntent pi = PendingIntent.getService(context, REQUEST_CODE, i, PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}
}