/**
 * Author: Sam Gavis-Hughson
 * Date: 4-28-2014
 * 
 * Call.java
 * An object to represent phone calls sent and received.
 */

package com.samgavis.usagetracker;

import java.io.Serializable;
import java.sql.Timestamp;

import android.content.Context;

public class Call implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Timestamp mTimestamp;
	private String mPhoneNumber;
	private int mType;
	private long mDuration;
	private double mLatitude;
	private double mLongitude;
	
	/**
	 * Public constructor.
	 * 
	 * @param timestamp Time when call was made/received.
	 * @param phoneNumber Phone number that was called.
	 * @param type Type of call: Incoming, Outgoing, Missed. See {@link http://developer.android.com/reference/android/provider/CallLog.Calls.html}.
	 * @param duration Duration of call in seconds.
	 * @param latitude Latitude in degrees.
	 * @param longitude Longitude in degrees.
	 */
	public Call(Timestamp timestamp, String phoneNumber, int type, long duration, double latitude, double longitude) {
		mTimestamp = timestamp;
		mPhoneNumber = phoneNumber;
		mType = type;
		mDuration = duration;
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	/**
	 * Public constructor that uses current location. If location is unavailable, Lattitude and Longitude
	 * are both set to 0.0.
	 * 
	 * @param timestamp Time when call was made/received.
	 * @param phoneNumber Phone number that was called.
	 * @param type Type of call: Incoming, Outgoing, Missed. See {@link http://developer.android.com/reference/android/provider/CallLog.Calls.html}.
	 * @param duration Duration of call in seconds.
	 * @param context Context of current activity.
	 */
	public Call(Timestamp timestamp, String phoneNumber, int type, long duration, Context context) {	
		GPSTracker gps = new GPSTracker(context);

		mTimestamp = timestamp;
		mPhoneNumber = phoneNumber;
		mType = type;
		mDuration = duration;
		
		if(gps.canGetLocation()){
			mLatitude = gps.getLatitude();
			mLongitude = gps.getLongitude();
		} else {
			mLatitude = 0.0;
			mLongitude = 0.0;
		}
	}

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return mTimestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		mTimestamp = timestamp;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return mType;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		mType = type;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return mDuration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		mDuration = duration;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}
}
