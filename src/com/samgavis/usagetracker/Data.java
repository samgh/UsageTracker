/**
 * Author: Sam Gavis-Hughson
 * Date: 4-28-2014
 * 
 * Data.java
 * An object to represent mobile or wifi data transferred.
 */

package com.samgavis.usagetracker;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import android.content.Context;

public class Data implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Timestamp mTimestamp;
	private Timestamp mTimeframe;
	private long mUpData;
	private long mDownData;
	private double mLatitude;
	private double mLongitude;
	
	/**
	 * Public constructor.
	 * 
	 * @param timestamp Time when data usage was recorded.
	 * @param timeframe Previous frame recorded. This is used when uploading to SDB to compress data.
	 * @param upData Amount of data uploaded. This is cumulative (wrapping whenever device restarts)
	 * unless timeframe is set, in which case it represents the data uploaded within the timeframe.
	 * @param downData Amount of data downloaded. This is cumulative (wrapping whenever device restarts)
	 * unless timeframe is set, in which case it represents the data uploaded within the timeframe.
	 * @param latitude Latitude in degrees when data usage was recorded.
	 * @param longitude Longitude in degrees when data usage was recorded.
	 */
	public Data(Timestamp timestamp, Timestamp timeframe, long upData, long downData, double latitude, double longitude) {
		mTimestamp = timestamp;
		mTimeframe = timeframe;
		mUpData = upData;
		mDownData = downData;
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	/**
	 * Public constructor that uses current time. Timeframe is set to null.
	 * 
	 * @param upData Amount of data uploaded. This is cumulative.
	 * @param downData Amount of data downloaded. This is cumulative.
	 * @param latitude Latitude when data usage was recorded.
	 * @param longitude Longitude when data usage was recorded.
	 */
	public Data(long upData, long downData, double latitude, double longitude) {
		this(new Timestamp(new Date().getTime()), null, upData, downData, latitude, longitude);
	}
	
	/**
	 * Public constructor that uses current location. If location is unavailable, Latitude and Longitude
	 * are both set to 0.0. Timeframe is set to null.
	 * 
	 * @param timestamp Time when data usage was recorded.
	 * @param upData Amount of data uploaded. This is cumulative.
	 * @param downData Amount of data downloaded. This is cumulative.
	 * @param context Context of current activity.
	 */
	public Data(Timestamp timestamp, long upData, long downData, Context context) {
		GPSTracker gps = new GPSTracker(context);

		mTimestamp = timestamp;
		mTimeframe = null;
		mUpData = upData;
		mDownData = downData;
		
		if(gps.canGetLocation()){
			mLatitude = gps.getLatitude();
			mLongitude = gps.getLongitude();
		} else {
			mLatitude = 0.0;
			mLongitude = 0.0;
		}
	}
	
	/**
	 * Public constructor that uses current time and location. If location is unavailable, Lattitude
	 * and Longitude are both set to 0.0. Timeframe is set to null.
	 * 
	 * @param upData Amount of data uploaded. This is cumulative.
	 * @param downData Amount of data downloaded. This is cumulative.
	 * @param context Context of current activity.
	 */
	public Data(long upData, long downData, Context context) {
		GPSTracker gps = new GPSTracker(context);

		mTimestamp = new Timestamp(new Date().getTime());
		mTimeframe = null;
		mUpData = upData;
		mDownData = downData;
		
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
	 * @return the timeframe
	 */
	public Timestamp getTimeframe() {
		return mTimeframe;
	}

	/**
	 * @param timeframe the timeframe to set
	 */
	public void setTimeframe(Timestamp timeframe) {
		mTimeframe = timeframe;
	}

	/**
	 * @return the upData
	 */
	public long getUpData() {
		return mUpData;
	}

	/**
	 * @param upData the upData to set
	 */
	public void setUpData(long upData) {
		mUpData = upData;
	}

	/**
	 * @return the downData
	 */
	public long getDownData() {
		return mDownData;
	}

	/**
	 * @param downData the downData to set
	 */
	public void setDownData(long downData) {
		mDownData = downData;
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
