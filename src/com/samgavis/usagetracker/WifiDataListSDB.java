package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class WifiDataListSDB extends ListSDB<Data> {

	protected WifiDataListSDB(AmazonSimpleDBClient client, String domainPrefix) {
		super(client, domainPrefix);
	}
	
	protected void createDomain() {
		super.createDomain(WIFI_DOMAIN_NAME);
	}

	protected boolean addData(Data data) {
		return super.addItem(data, WIFI_DOMAIN_NAME);
	}
	
	@Override
	protected PutAttributesRequest getPutAttributesRequest(Data data) {
		return ListSDBUtils.generateDataPutAttributesRequest(data, WIFI_DOMAIN_NAME);
	}
	
	protected List<Data> getData() {
		return super.getItems(WIFI_DOMAIN_NAME);
	}
	
	protected List<Data> getData(Timestamp earliest, Timestamp latest) {
		return super.getItems(earliest, latest, WIFI_DOMAIN_NAME);
	}
	
	protected Data getMostRecentData() {
		return super.getMostRecentItem(WIFI_DOMAIN_NAME);
	}
	
	protected Data getOldestData() {
		return super.getOldestItem(WIFI_DOMAIN_NAME);
	}

	@Override
	protected List<Data> convertResultToItemList(SelectResult result) {
		return ListSDBUtils.convertResultToDataList(result) ;
	}
}
