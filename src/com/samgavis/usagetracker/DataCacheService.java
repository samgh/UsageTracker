/**
 * Author: Sam Gavis-Hughson
 * Date: 4-10-2014
 * 
 * DataCacheService.java
 * An IntentService that executes every 15 seconds and adds a new Data
 * object to the local SQLite database.
 */

package com.samgavis.usagetracker;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

public class DataCacheService extends IntentService {

	private static final String TAG = "DataCacheService";
	private static final int POLL_INTERVAL = 1000 * 15;
	private static final int REQUEST_CODE = 0;
	
	public DataCacheService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		long rxBytes = TrafficStats.getTotalRxBytes();
        long txBytes = TrafficStats.getTotalTxBytes();

        long mobUp = TrafficStats.getMobileTxBytes();
        long mobDown = TrafficStats.getMobileRxBytes();

        long wifiUp = txBytes-(mobUp);
        long wifiDown = rxBytes-(mobDown);
		
		Data mMobileData = new Data(mobUp, mobDown, this);
		Data mWifiData = new Data(wifiUp, wifiDown, this);

		MobileDataListSQL mMobileList = new MobileDataListSQL(this);
		WifiDataListSQL mWifiList = new WifiDataListSQL(this);
		
		mMobileList.addData(mMobileData);
		mWifiList.addData(mWifiData);
	}
	
	/**
	 * Set PendingIntent alarm for DataCacheService that runs every 15 seconds.
	 * @param context Current context.
	 * @param isOn Value to set for whether alarm is on.
	 */
	protected static void setServiceAlarm(Context context, boolean isOn) {
		Intent i = new Intent(context, DataCacheService.class);
		PendingIntent pi = PendingIntent.getService(context,  REQUEST_CODE,  i, 0);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		if (isOn) {
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pi);
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
	protected static boolean isServiceAlarmOn(Context context) {
		Intent i = new Intent(context, DataCacheService.class);
		PendingIntent pi = PendingIntent.getService(context, REQUEST_CODE, i, PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}
}
