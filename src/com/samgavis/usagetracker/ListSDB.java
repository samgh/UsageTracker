/**
 * Author: Sam Gavis-Hughson
 * Date: 4-14-2014
 * 
 * ListSDB.java
 * SDB controller that implements all general functionality
 * for SDB.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.NoSuchDomainException;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public abstract class ListSDB<Item> {

	/**
	 * Used to generate unique domain names.
	 */
	protected static final String CALL_DOMAIN_NAME_SUFFIX = "_call";
	protected static final String MOBILE_DOMAIN_NAME_SUFFIX = "_mobile";
	protected static final String WIFI_DOMAIN_NAME_SUFFIX = "_wifi";
	
	/**
	 * Attributes used by all SDB domains
	 */
	protected static final String TIMESTAMP_ATTRIBUTE = "timestamp";
	protected static final String LAT_ATTRIBUTE = "lat";
	protected static final String LONG_ATTRIBUTE = "long";
	
	/**
	 * Call-specific attributes.
	 */
	protected static final String PHONE_NUMBER_ATTRIBUTE = "number";
	protected static final String TYPE_ATTRIBUTE = "type";
	protected static final String DURATION_ATTRIBUTE = "duration";
	
	/**
	 * Data-specific attributes.
	 */
	protected static final String TIMEFRAME_ATTRIBUTE = "timeframe";
	protected static final String UP_DATA_ATTRIBUTE = "updata";
	protected static final String DOWN_DATA_ATTRIBUTE = "downdata";
	
	protected AmazonSimpleDBClient mSDBClient;
	
	protected String CALL_DOMAIN_NAME;
	protected String MOBILE_DOMAIN_NAME;
	protected String WIFI_DOMAIN_NAME;
	
	/**
	 * Constructor.
	 * @param client AmazonSimpleDBClient with appropriate permissions.
	 * @param domainPrefix Prefix to apply to all domains.
	 */
	protected ListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		mSDBClient = client;
		
		CALL_DOMAIN_NAME = domainPrefix + CALL_DOMAIN_NAME_SUFFIX;
		MOBILE_DOMAIN_NAME = domainPrefix + MOBILE_DOMAIN_NAME_SUFFIX;
		WIFI_DOMAIN_NAME = domainPrefix + WIFI_DOMAIN_NAME_SUFFIX;
	}
	
	/**
	 * Updates SimpleDBClient. This can be used if authorization expires.
	 * @param client New client.
	 */
	protected void updateSDBClient(AmazonSimpleDBClient client) {
		mSDBClient = client;
	}
	
	/**
	 * Creates a domain with the given name.
	 * @param domainName Name of the domain to create.
	 */
	protected void createDomain(String domainName) {
		CreateDomainRequest cdr = new CreateDomainRequest(domainName);
		mSDBClient.createDomain(cdr);
	}
	
	/**
	 * Add an item to the domain specified by domainName.
	 * @param item Item to add to domain.
	 * @param domainName Domain name to add item to.
	 * @return True if successful, false if unsuccessful. False should be interpreted
	 * to mean that AmazonSimpleDBClient must be updated.
	 */
	protected boolean addItem(Item item, String domainName) {
		PutAttributesRequest par = getPutAttributesRequest(item);
		return addItem(par);
	}
	
	/**
	 * Calls asynchronous class to add item.
	 * @param par PutAttributesRequest to be added to domain.
	 * @return True if successful, false if unsuccessful. False should be interpreted
	 * to mean that AmazonSimpleDBClient must be updated.
	 */
	private boolean addItem(PutAttributesRequest par) {
		try {
			return new AddItemAsync().execute(par).get();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Helper method to generate PutAttributesRequest
	 * @param item Item for which to generate PutAttributesRequest
	 * @return PutAttributesRequest that was created
	 */
	protected abstract PutAttributesRequest getPutAttributesRequest(Item item);
	
	/**
	 * AsyncTask to upload item to domain.
	 * @author SamGavisHughson
	 * @return True if successful, false if unsuccessful. Reason for failure is
	 * likely an issue with AmazonSimpleDBClient and so it should be updated.
	 */
	private class AddItemAsync extends AsyncTask<PutAttributesRequest, Void, Boolean> {
		@Override
		protected Boolean doInBackground(PutAttributesRequest... pars) {
			try {
				mSDBClient.putAttributes(pars[0]);
			} catch (NoSuchDomainException e) {
				// Lazily instantiate domain name
				createDomain(pars[0].getDomainName());
				return addItem(pars[0]);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * Get all items from domain.
	 * @param domainName Domain from which to get items.
	 * @return List of items in database ordered from oldest to newest. Null if
	 * error reading from AmazonSimpleDBClient.
	 */
	public List<Item> getItems(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE)
			.withConsistentRead(true);
		return getItems(selectRequest);
	}
	
	/**
	 * Get domains within range of timestamps (inclusive).<br />
	 * {@code getItems(timestamp, null, null, domainName)} -> Return all items in table.<br />
	 * {@code getItems(timestamp1, null, timestamp2, domainName)} -> Return all items <= {@code timestamp2}<br />
	 * {@code getItems(timestamp1, timestamp2, null, domainName)} -> Return all items >= {@code timestamp2}<br />
	 * {@code getItems(timestamp1, timestamp2, timestamp3, domainName)} -> Return all items >= {@code timestamp2} 
	 * and <= {@code timestamp3}<br />
	 * @param earliest Timestamp of the earliest time in range. Null will return list with
	 * no lower bound on Timestamp.
	 * @param latest Timestamp of latest time in range. Null will return list with no upper
	 * bound on Timestamp.
	 * @param domainName Domain to query.
	 * @return List of items in range in database ordered from oldest to newest. Null if
	 * error reading from AmazonSimpleDBClient.
	 */
	public List<Item> getItems(Timestamp earliest, Timestamp latest, String domainName) {
		if (earliest == null && latest == null) {
			return getItems(domainName);
		} else if (earliest == null) {
			SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
					"` where " + TIMESTAMP_ATTRIBUTE + " > '' and " + TIMESTAMP_ATTRIBUTE + " <= " + 
					ListSDBUtils.padTimestamp(latest) + " order by " + TIMESTAMP_ATTRIBUTE)
				.withConsistentRead(true);
			return getItems(selectRequest);
		} else if (latest == null) {
			SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
					"` where " + TIMESTAMP_ATTRIBUTE + " >= " + ListSDBUtils.padTimestamp(earliest) + 
					" order by " + TIMESTAMP_ATTRIBUTE)
				.withConsistentRead(true);
			return getItems(selectRequest);
		} else {
			SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
					"` where " + TIMESTAMP_ATTRIBUTE + " >= " + ListSDBUtils.padTimestamp(earliest) + 
					" and " + TIMESTAMP_ATTRIBUTE + " <= " + ListSDBUtils.padTimestamp(latest) + 
					" order by " + TIMESTAMP_ATTRIBUTE)
				.withConsistentRead(true);
			return getItems(selectRequest);	
		}
	}
	
	/**
	 * Get most recent item from domain.
	 * @param domainName Domain to query.
	 * @return Most recent item in domain. Null if error reading from AmazonSimpleDBClient.
	 */
	public Item getMostRecentItem(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE + " desc limit 1")
			.withConsistentRead(true);
		List<Item> items = getItems(selectRequest);
		if (items != null) return items.get(0);
		return null;
	}
	
	/**
	 * Get oldest item from domain.
	 * @param domainName Domain to query.
	 * @return Oldest item in domain. Null if error reading from AmazonSimpleDBClient.
	 */
	public Item getOldestItem(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE + " limit 1")
			.withConsistentRead(true);
		List<Item> items = getItems(selectRequest);
		if (items != null) return items.get(0);
		return null;
	}
	
	/**
	 * Execute SelectRequest asynchronously.
	 * @param selectRequest SelectRequest to execute.
	 * @return Return list of items on success, null on failure.
	 */
	private List<Item> getItems(SelectRequest selectRequest) {
		try {
			return new GetItemsAsync().execute(selectRequest).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Asynchronous class that executes SelectRequest. Returns null on failure.
	 * @author SamGavisHughson
	 */
	private class GetItemsAsync extends AsyncTask<SelectRequest, Void, List<Item>> {
		@Override
		protected List<Item> doInBackground(SelectRequest... selectRequests) {
			String nextToken = null;
			List<Item> items = Collections.emptyList();
			
			/**
			 * SimpleDB pages results using a NextToken. This logic collects all
			 * pages of data.
			 */
			do {
				selectRequests[0].setNextToken(nextToken);
				try {
					SelectResult result = mSDBClient.select(selectRequests[0]);
					nextToken = result.getNextToken();
					items.addAll(convertResultToItemList(result));
				} catch (Exception e) {
					return null;
				}
			} while (nextToken != null);		
			return items;
		}	
	}
	
	/**
	 * Convert SelectResult to a list of items.
	 * @param result Result from SelectRequest.
	 * @return List of items from SelectResult.
	 */
	protected abstract List<Item> convertResultToItemList(SelectResult result);

}
