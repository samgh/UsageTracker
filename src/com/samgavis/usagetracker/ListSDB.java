package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.NoSuchDomainException;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public abstract class ListSDB<Item> {

	protected static final String CALL_DOMAIN_NAME_SUFFIX = "_call";
	protected static final String MOBILE_DOMAIN_NAME_SUFFIX = "_mobile";
	protected static final String WIFI_DOMAIN_NAME_SUFFIX = "_wifi";
	
	// General
	protected static final String TIMESTAMP_ATTRIBUTE = "timestamp";
	protected static final String LAT_ATTRIBUTE = "lat";
	protected static final String LONG_ATTRIBUTE = "long";
	
	// Data
	protected static final String TIMEFRAME_ATTRIBUTE = "timeframe";
	protected static final String UP_DATA_ATTRIBUTE = "updata";
	protected static final String DOWN_DATA_ATTRIBUTE = "downdata";
	
	// Calls
	protected static final String PHONE_NUMBER_ATTRIBUTE = "number";
	protected static final String TYPE_ATTRIBUTE = "type";
	protected static final String DURATION_ATTRIBUTE = "duration";
	
	protected AmazonSimpleDBClient mSDBClient;
	
	protected String CALL_DOMAIN_NAME;
	protected String MOBILE_DOMAIN_NAME;
	protected String WIFI_DOMAIN_NAME;
	
	protected ListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		mSDBClient = client;
		
		CALL_DOMAIN_NAME = domainPrefix + CALL_DOMAIN_NAME_SUFFIX;
		MOBILE_DOMAIN_NAME = domainPrefix + MOBILE_DOMAIN_NAME_SUFFIX;
		WIFI_DOMAIN_NAME = domainPrefix + WIFI_DOMAIN_NAME_SUFFIX;
	}
	
	protected void createDomain(String domainName) {
		CreateDomainRequest cdr = new CreateDomainRequest(domainName);
		mSDBClient.createDomain(cdr);
	}
	
	protected boolean addItem(Item item, String domainName) {
		PutAttributesRequest par = getPutAttributesRequest(item);
		return addItem(par);
	}
	
	private boolean addItem(PutAttributesRequest par) {
		try {
			return new AddItemAsync().execute(par).get();
		} catch (Exception e) {
			return false;
		}
	}
	
	protected abstract PutAttributesRequest getPutAttributesRequest(Item item);
	
	private class AddItemAsync extends AsyncTask<PutAttributesRequest, Void, Boolean> {
		@Override
		protected Boolean doInBackground(PutAttributesRequest... pars) {
			try {
				mSDBClient.putAttributes(pars[0]);
			} catch (NoSuchDomainException e) {
				createDomain(pars[0].getDomainName());
				return addItem(pars[0]);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}
	
	protected List<Item> getItems(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE)
			.withConsistentRead(true);
		return getItems(selectRequest);
	}
	
	protected List<Item> getItems(Timestamp earliest, Timestamp latest, String domainName) {
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
	
	protected Item getMostRecentItem(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE + " desc limit 1")
			.withConsistentRead(true);
		List<Item> items = getItems(selectRequest);
		if (items != null) return items.get(0);
		return null;
	}
	
	protected Item getOldestItem(String domainName) {
		SelectRequest selectRequest = new SelectRequest("select * from `" + domainName + 
				"` where " + TIMESTAMP_ATTRIBUTE + " > '' order by " + TIMESTAMP_ATTRIBUTE + " limit 1")
			.withConsistentRead(true);
		List<Item> items = getItems(selectRequest);
		if (items != null) return items.get(0);
		return null;
	}
	
	private List<Item> getItems(SelectRequest selectRequest) {
		try {
			return new GetItemsAsync().execute(selectRequest).get();
		} catch (Exception e) {
			return null;
		}
	}
	
	private class GetItemsAsync extends AsyncTask<SelectRequest, Void, List<Item>> {
		@Override
		protected List<Item> doInBackground(SelectRequest... selectRequests) {
			String nextToken = null;
			List<Item> items = Collections.emptyList();
			
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
	
	protected abstract List<Item> convertResultToItemList(SelectResult result);

}
