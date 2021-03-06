/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * MobileDataListSDB.java
 * Subclass of ListSDB that implements functionality for storing mobile data.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class MobileDataListSDB extends ListSDB<Data> {

	/**
	 * Constructor.
	 * @param client AmazonSimpleDBClient to access SimpleDB.
	 * @param domainPrefix Domain prefix for user.
	 */
	protected MobileDataListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		super(client, domainPrefix);
	}
	
	/**
	 * Create mobile data domain.
	 */
	protected void createDomain() {
		super.createDomain(MOBILE_DOMAIN_NAME);
	}

	/**
	 * Add data to mobile data domain.
	 * @param data Data to add.
	 * @return True if successful. False implies that AmazonSimpleDBClient must
	 * be updated.
	 */
	protected boolean addData(Data data) {
		return super.addItem(data, MOBILE_DOMAIN_NAME);
	}
	
	/**
	 * Get PutAttributesRequest for data.
	 * @param data Data for which to get PutAttributesRequest.
	 * @return PutAttributesRequest for data.
	 */
	@Override
	protected PutAttributesRequest getPutAttributesRequest(Data data) {
		return ListSDBUtils.generateDataPutAttributesRequest(data, MOBILE_DOMAIN_NAME);
	}
	
	/**
	 * Get all data from SimpleDB
	 * @return List of data. If null, then AmazonSimpleDBClient should be updated.
	 */
	protected List<Data> getData() {
		return super.getItems(MOBILE_DOMAIN_NAME);
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
	protected List<Data> getData(Timestamp earliest, Timestamp latest) {
		return super.getItems(earliest, latest, MOBILE_DOMAIN_NAME);
	}
	
	/**
	 * Get most recent data.
	 * @return Most recent data. Null if error reading from AmazonSimpleDBClient.
	 */
	protected Data getMostRecentData() {
		return super.getMostRecentItem(MOBILE_DOMAIN_NAME);
	}
	
	/**
	 * Get oldest data.
	 * @return Oldest data. Null if error reading from AmazonSimpleDBClient.
	 */
	protected Data getOldestData() {
		return super.getOldestItem(MOBILE_DOMAIN_NAME);
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
