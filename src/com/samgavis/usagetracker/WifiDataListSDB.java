/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * WifiDataListSDB.java
 * Subclass of ListSDB that implements functionality for storing wifi data.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class WifiDataListSDB extends ListSDB<Data> {

	/**
	 * Constructor.
	 * @param client AmazonSimpleDBClient to access SimpleDB.
	 * @param domainPrefix Domain prefix for user.
	 */
	protected WifiDataListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		super(client, domainPrefix);
	}
	
	/**
	 * Create wifi data domain.
	 */
	protected void createDomain() {
		super.createDomain(WIFI_DOMAIN_NAME);
	}

	/**
	 * Add data to mobile data domain.
	 * @param data Data to add.
	 * @return True if successful. False implies that AmazonSimpleDBClient must
	 * be updated.
	 */
	protected boolean addData(Data data) {
		return super.addItem(data, WIFI_DOMAIN_NAME);
	}
	
	/**
	 * Get PutAttributesRequest for data.
	 * @param data Data for which to get PutAttributesRequest.
	 * @return PutAttributesRequest for data.
	 */
	@Override
	protected PutAttributesRequest getPutAttributesRequest(Data data) {
		return ListSDBUtils.generateDataPutAttributesRequest(data, WIFI_DOMAIN_NAME);
	}
	
	/**
	 * Get all data from SimpleDB
	 * @return List of data. If null, then AmazonSimpleDBClient should be updated.
	 */
	public List<Data> getData() {
		return super.getItems(WIFI_DOMAIN_NAME);
	}
	
	/**
	 * Get data between timestamps (inclusive).
	 * @param earliest Timestamp of the earliest time in range. Null will return list with
	 * no lower bound on Timestamp.
	 * @param latest Timestamp of latest time in range. Null will return list with no upper
	 * bound on Timestamp.
	 * @param domainName Domain to query.
	 * @return List of data in range in database ordered from oldest to newest. Null if
	 * error reading from AmazonSimpleDBClient.
	 * @see ListSDB
	 */
	public List<Data> getData(Timestamp earliest, Timestamp latest) {
		return super.getItems(earliest, latest, WIFI_DOMAIN_NAME);
	}
	
	/**
	 * Get most recent data.
	 * @return Most recent data. Null if error reading from AmazonSimpleDBClient.
	 */
	public Data getMostRecentData() {
		return super.getMostRecentItem(WIFI_DOMAIN_NAME);
	}
	
	/**
	 * Get oldest data.
	 * @return Oldest data. Null if error reading from AmazonSimpleDBClient.
	 */
	public Data getOldestData() {
		return super.getOldestItem(WIFI_DOMAIN_NAME);
	}

	/**
	 * Convert SelectResult to a list of data.
	 * @param result Result from SelectRequest.
	 * @return List of data from SelectResult.
	 */
	@Override
	protected List<Data> convertResultToItemList(SelectResult result) {
		return ListSDBUtils.convertResultToDataList(result) ;
	}
}
