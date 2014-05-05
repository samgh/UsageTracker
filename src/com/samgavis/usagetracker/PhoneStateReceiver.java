/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * PhoneStateReciever.java
 * A broadcast receiver that receives phone state and starts
 * CallCacheService when the phone becomes idle.
 */

package com.samgavis.usagetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneStateReceiver extends BroadcastReceiver {

	private static final String TAG = "PhoneStateReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String state = intent.getStringExtra("state");	
		if (state.equals("IDLE")) {
			Intent i = new Intent(context, CallCacheService.class);
			context.startService(i);
		}
	}
}
