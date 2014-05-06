/** 
 * Author: Sam Gavis-Hughson
 * Date: 5-6-2014
 * 
 * UsageTracker.java
 * The main UsageTracker object. This provides a public interface to developers.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import android.content.Context;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public class UsageTracker {

	private static final String TAG = "UsageTracker";
	
	private Context mContext;
	private AmazonSimpleDBClient mClient;
	private String mDomainPrefix;
	
	/**
	 * Public constructor.
	 * @param context Current context.
	 * @param client AmazonSimpleDBClient to which to upload.
	 * @param domainPrefix Domain prefix for user.
	 * @param pushData Should the cached data be pushed to the cloud when
	 * UsageTracker is instantiated?
	 */
	public UsageTracker(Context context, AmazonSimpleDBClient client, String domainPrefix, boolean pushData) {
		mContext = context;
		mClient = client;
		mDomainPrefix = domainPrefix;
		
		if (pushData) PushData.pushData(mClient, mDomainPrefix, mContext);
	}
	
	/**
	 * Update AmazonSimpleDBClient if the authorization has expired.
	 * @param client Client to update to.
	 */
	public void updateClient(AmazonSimpleDBClient client) {
		mClient = client;
	}
	
	/**
	 * Get all calls from DB.
	 * @return List of calls. Null means client must be updated.
	 */
	public List<Call> getCalls() {
		return (new CallListSDB(mClient, mDomainPrefix)).getCalls();
	}
	
	/**
	 * Return all calls within range of Timestamps (inclusive).
	 * @param earliest Earliest time to include. If null then there is no lower bound.
	 * @param latest Latest time to include. If null then there is no upper bound.
	 * @return List of Calls in range. Null means client must be updated.
	 */
	public List<Call> getCalls(Timestamp earliest, Timestamp latest) {
		return (new CallListSDB(mClient, mDomainPrefix)).getCalls(earliest, latest);
	}
	
	/**
	 * Get most recent call.
	 * @return Most recent call. Null means client must be updated.
	 */
	public Call getMostRecentCall() {
		return (new CallListSDB(mClient, mDomainPrefix)).getMostRecentCall();
	}
	
	/**
	 * Get oldest call.
	 * @return Oldest call. Null means client must be updated.
	 */
	public Call getOldestCall() {
		return (new CallListSDB(mClient, mDomainPrefix)).getOldestCall();
	}
	
	/**
	 * Get all mobile data from DB.
	 * @return List of all mobile data. Null means client must be updated.
	 */
	public List<Data> getMobileData() {
		return (new MobileDataListSDB(mClient, mDomainPrefix)).getData();
	}
	
	/**
	 * Return all mobile data within range of Timestamps (inclusive).
	 * @param earliest Earliest time to include. If null then there is no lower bound.
	 * @param latest Latest time to include. If null then there is no upper bound.
	 * @return List of mobile data in range. Null means client must be updated.
	 */
	public List<Data> getMobileData(Timestamp earliest, Timestamp latest) {
		return (new MobileDataListSDB(mClient, mDomainPrefix)).getData(earliest, latest);
	}
	
	/**
	 * Get most recent mobile data.
	 * @return Most recent mobile data. Null means client must be updated.
	 */
	public Data getMostRecentMobileData() {
		return (new MobileDataListSDB(mClient, mDomainPrefix)).getMostRecentData();
	}
	
	/**
	 * Get oldest mobile data.
	 * @return Oldest mobile data. Null means client must be updated.
	 */
	public Data getOldestMobileData() {
		return (new MobileDataListSDB(mClient, mDomainPrefix)).getOldestData();
	}
	
	/**
	 * Get all wifi data from DB.
	 * @return List of all wifi data. Null means client must be updated.
	 */
	public List<Data> getWifiData() {
		return (new WifiDataListSDB(mClient, mDomainPrefix)).getData();
	}
	
	/**
	 * Return all wifi data within range of Timestamps (inclusive).
	 * @param earliest Earliest time to include. If null then there is no lower bound.
	 * @param latest Latest time to include. If null then there is no upper bound.
	 * @return List of wifi data in range. Null means client must be updated.
	 */
	public List<Data> getWifiData(Timestamp earliest, Timestamp latest) {
		return (new WifiDataListSDB(mClient, mDomainPrefix)).getData(earliest, latest);
	}
	
	/**
	 * Get most recent wifi data.
	 * @return Most recent wifi data. Null means client must be updated.
	 */
	public Data getMostRecentWifiData() {
		return (new WifiDataListSDB(mClient, mDomainPrefix)).getMostRecentData();
	}
	
	/**
	 * Get oldest wifi data.
	 * @return Oldest wifi data. Null means client must be updated.
	 */
	public Data getOldestData() {
		return (new WifiDataListSDB(mClient, mDomainPrefix)).getOldestData();
	}
}
