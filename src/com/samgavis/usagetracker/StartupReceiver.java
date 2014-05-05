/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * StartupReceiver.java
 * A broadcast receiver that receives startup broadcast and 
 * starts intent services.
 */

package com.samgavis.usagetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver {
	private static final String TAG = "StartupReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		DataCacheService.setServiceAlarm(context, true);
	}
}
