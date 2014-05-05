/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * CallListSDB.java
 * Subclass of ListSDB that implements functionality for storing Calls.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class CallListSDB extends ListSDB<Call> {

	/**
	 * Constructor.
	 * @param client AmazonSimpleDBClient to access SimpleDB.
	 * @param domainPrefix Domain prefix for user.
	 */
	protected CallListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		super(client, domainPrefix);
	}
	
	/**
	 * Create call domain.
	 */
	protected void createDomain() {
		super.createDomain(CALL_DOMAIN_NAME);
	}

	/**
	 * Add call to call domain.
	 * @param call Call to add.
	 * @return True if successful. False implies that AmazonSimpleDBClient must
	 * be updated.
	 */
	protected boolean addCall(Call call) {
		return super.addItem(call, CALL_DOMAIN_NAME);
	}
	
	/**
	 * Get PutAttributesRequest for call.
	 * @param call Call for which to get PutAttributesRequest.
	 * @return PutAttributesRequest for call.
	 */
	@Override
	protected PutAttributesRequest getPutAttributesRequest(Call call) {
		return ListSDBUtils.generateCallPutAttributesRequest(call, CALL_DOMAIN_NAME);
	}
	
	/**
	 * Get all calls from SimpleDB
	 * @return List of calls. If null, then AmazonSimpleDBClient should be updated.
	 */
	public List<Call> getCalls() {
		return super.getItems(CALL_DOMAIN_NAME);
	}
	
	/**
	 * Get calls between timestamps (inclusive).
	 * @param earliest Timestamp of the earliest time in range. Null will return list with
	 * no lower bound on Timestamp.
	 * @param latest Timestamp of latest time in range. Null will return list with no upper
	 * bound on Timestamp.
	 * @param domainName Domain to query.
	 * @return List of calls in range in database ordered from oldest to newest. Null if
	 * error reading from AmazonSimpleDBClient.
	 * @see ListSDB
	 */
	public List<Call> getCalls(Timestamp earliest, Timestamp latest) {
		return super.getItems(earliest, latest, CALL_DOMAIN_NAME);
	}
	
	/**
	 * Get most recent call.
	 * @return Most recent call. Null if error reading from AmazonSimpleDBClient.
	 */
	public Call getMostRecentCall() {
		return super.getMostRecentItem(CALL_DOMAIN_NAME);
	}
	
	/**
	 * Get oldest call.
	 * @return Oldest call. Null if error reading from AmazonSimpleDBClient.
	 */
	public Call getOldestCall() {
		return super.getOldestItem(CALL_DOMAIN_NAME);
	}

	/**
	 * Convert SelectResult to a list of calls.
	 * @param result Result from SelectRequest.
	 * @return List of calls from SelectResult.
	 */
	@Override
	protected List<Call> convertResultToItemList(SelectResult result) {
		return ListSDBUtils.convertResultToCallList(result) ;
	}
}
