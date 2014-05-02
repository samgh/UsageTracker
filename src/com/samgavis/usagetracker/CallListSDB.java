package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class CallListSDB extends ListSDB<Call> {

	protected CallListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		super(client, domainPrefix);
		// TODO Auto-generated constructor stub
	}
	
	protected void createDomain() {
		super.createDomain(CALL_DOMAIN_NAME);
	}

	protected boolean addCall(Call call) {
		return super.addItem(call, CALL_DOMAIN_NAME);
	}
	
	@Override
	protected PutAttributesRequest getPutAttributesRequest(Call call) {
		return ListSDBUtils.generateCallPutAttributesRequest(call, CALL_DOMAIN_NAME);
	}
	
	protected List<Call> getCalls() {
		return super.getItems(CALL_DOMAIN_NAME);
	}
	
	protected List<Call> getCalls(Timestamp earliest, Timestamp latest) {
		return super.getItems(earliest, latest, CALL_DOMAIN_NAME);
	}
	
	protected Call getMostRecentCall() {
		return super.getMostRecentItem(CALL_DOMAIN_NAME);
	}
	
	protected Call getOldestCall() {
		return super.getOldestItem(CALL_DOMAIN_NAME);
	}

	@Override
	protected List<Call> convertResultToItemList(SelectResult result) {
		return ListSDBUtils.convertResultToCallList(result) ;
	}
}
