/**
 * Author: Sam Gavis-Hughson
 * Date: 5-1-2014
 * 
 * ListSQLUtils.java
 * This class contains a handful of utility functions that are
 * used by ListSQL subclasses.
 */

package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.amazonaws.services.simpledb.util.SimpleDBUtils;

public class ListSDBUtils {
	
	/**
	 * Generate PutAttributesRequest for Call object.
	 * @param call Call for which to generate PutAttributesRequest.
	 * @param domainName Domain to which to add Call.
	 * @return PutAttributesRequest for call.
	 */
	protected static PutAttributesRequest generateCallPutAttributesRequest(Call call, String domainName) {
		ReplaceableAttribute timestampAttribute = new ReplaceableAttribute(ListSDB.TIMESTAMP_ATTRIBUTE, padTimestamp(call.getTimestamp()), Boolean.TRUE );
		ReplaceableAttribute phoneNumberAttribute = new ReplaceableAttribute(ListSDB.PHONE_NUMBER_ATTRIBUTE, call.getPhoneNumber(), Boolean.TRUE );
		ReplaceableAttribute typeAttribute = new ReplaceableAttribute(ListSDB.TYPE_ATTRIBUTE, String.valueOf(call.getType()), Boolean.TRUE );
		ReplaceableAttribute durationAttribute = new ReplaceableAttribute(ListSDB.DURATION_ATTRIBUTE, padDuration(call.getDuration()), Boolean.TRUE);
		ReplaceableAttribute latAttribute = new ReplaceableAttribute(ListSDB.LAT_ATTRIBUTE, padLatitude(call.getLatitude()), Boolean.TRUE );
		ReplaceableAttribute longAttribute = new ReplaceableAttribute(ListSDB.LONG_ATTRIBUTE, padLongitude(call.getLongitude()), Boolean.TRUE );
		
		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(5);
		attrs.add(timestampAttribute);
		attrs.add(phoneNumberAttribute);
		attrs.add(typeAttribute);
		attrs.add(durationAttribute);
		attrs.add(latAttribute);
		attrs.add(longAttribute);
		
		return new PutAttributesRequest(domainName, padTimestamp(call.getTimestamp()), attrs);	
	}
	/**
	 * Generate PutAttributesRequest for Data object.
	 * @param data Data for which to generate PutAttributesRequest.
	 * @param domainName Domain to which to add Data.
	 * @return PutAttributesRequest for data.
	 */
	protected static PutAttributesRequest generateDataPutAttributesRequest(Data data, String domainName) {
		ReplaceableAttribute timestampAttribute = new ReplaceableAttribute(ListSDB.TIMESTAMP_ATTRIBUTE, padTimestamp(data.getTimestamp()), Boolean.TRUE );
		ReplaceableAttribute timeframeAttribute = new ReplaceableAttribute(ListSDB.TIMEFRAME_ATTRIBUTE, padTimeframe(data.getTimeframe()), Boolean.TRUE );
		ReplaceableAttribute upDataAttribute = new ReplaceableAttribute(ListSDB.UP_DATA_ATTRIBUTE, padUpData(data.getUpData()), Boolean.TRUE );
		ReplaceableAttribute downDataAttribute = new ReplaceableAttribute(ListSDB.DOWN_DATA_ATTRIBUTE, padDownData(data.getDownData()), Boolean.TRUE );
		ReplaceableAttribute latAttribute = new ReplaceableAttribute(ListSDB.LAT_ATTRIBUTE, padLatitude(data.getLatitude()), Boolean.TRUE );
		ReplaceableAttribute longAttribute = new ReplaceableAttribute(ListSDB.LONG_ATTRIBUTE, padLongitude(data.getLongitude()), Boolean.TRUE );
		
		List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(6);
		attrs.add(timestampAttribute);
		attrs.add(timeframeAttribute);
		attrs.add(upDataAttribute);
		attrs.add(downDataAttribute);
		attrs.add(latAttribute);
		attrs.add(longAttribute);
		
		return new PutAttributesRequest(domainName, padTimestamp(data.getTimestamp()), attrs);
	}
		
	/**
	 * Convert selectResult to list of calls.
	 * @param selectResult SelectResult to convert to list.
	 * @return List of calls from selectResult.
	 */
	protected static List<Call> convertResultToCallList(SelectResult selectResult) {
		List<Item> items = selectResult.getItems();
		List<Call> calls = new ArrayList<Call>(items.size());
		for (Item item : items) {
			calls.add(convertItemToCall(item));
		}
		return calls;
	}
	
	/**
	 * Convert selectResutl to list of data.
	 * @param selectResult SelectResult to convert to list.
	 * @return List of data from selectResult.
	 */
	protected static List<Data> convertResultToDataList(SelectResult selectResult) {
		List<Item> items = selectResult.getItems();
		List<Data> data = new ArrayList<Data>(items.size());
		for (Item item : items) {
			data.add(convertItemToData(item));
		}
		return data;
	}
	
	/**
	 * Convert item to Call.
	 * @param item Item to convert.
	 * @return Call converted from item.
	 */
	private static Call convertItemToCall(Item item) {
		return new Call(getTimestampFromItem(item), getPhoneNumberFromItem(item), getTypeFromItem(item), 
				getDurationFromItem(item), getLatFromItem(item), getLongFromItem(item));
	}
	
	/**
	 * Convert item to Data.
	 * @param item Item to convert.
	 * @return Data converted from item.
	 */
	private static Data convertItemToData(Item item) {
		return new Data(getTimestampFromItem(item), getTimeframeFromItem(item), getUpDataFromItem(item), 
				getDownDataFromItem(item), getLatFromItem(item), getLongFromItem(item));
	}
	
	/**
	 * Pad timestamp.
	 */
	protected static String padTimestamp(Timestamp timestamp) {
		return SimpleDBUtils.encodeZeroPadding(timestamp.getTime(), 20);
	}
	
	/**
	 * Pad latitude.
	 */	
	protected static String padLatitude(double latitude) {
		return SimpleDBUtils.encodeZeroPadding((float)latitude, 10);
	}
	
	/**
	 * Pad longitude.
	 */
	protected static String padLongitude(double longitude) {
		return SimpleDBUtils.encodeZeroPadding((float)longitude, 20);
	}
	
	/**
	 * Pad duration.
	 */
	protected static String padDuration(long duration) {
		return SimpleDBUtils.encodeZeroPadding(duration, 20);
	}

	/**
	 * Pad timeframe.
	 */
	protected static String padTimeframe(Timestamp timeframe) {
		return SimpleDBUtils.encodeZeroPadding(timeframe.getTime(), 20);
	}
	
	/**
	 * Pad upData.
	 */
	protected static String padUpData(long upData) {
		return SimpleDBUtils.encodeZeroPadding(upData, 20);
	}
	
	/**
	 * Pad downData.
	 */
	protected static String padDownData(long downData) {
		return SimpleDBUtils.encodeZeroPadding(downData, 20);
	}
	
	/**
	 * Get timestamp from item.
	 */
	private static Timestamp getTimestampFromItem(Item item) {
		return new Timestamp(getLongValueForAttributeFromList(ListSDB.TIMESTAMP_ATTRIBUTE, item.getAttributes()));
	}
	
	/**
	 * Get latitude from item.
	 */
	private static double getLatFromItem(Item item) {
		return getDoubleValueForAttributeFromList(ListSDB.LAT_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get longitude from item.
	 */
	private static double getLongFromItem(Item item) {
		return getDoubleValueForAttributeFromList(ListSDB.LONG_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get phone number from item.
	 */
	private static String getPhoneNumberFromItem(Item item) {
		return getStringValueForAttributeFromList(ListSDB.PHONE_NUMBER_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get type from item.
	 */
	private static int getTypeFromItem(Item item) {
		return getIntValueForAttributeFromList(ListSDB.TYPE_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get duration from item.
	 */
	private static long getDurationFromItem(Item item) {
		return getLongValueForAttributeFromList(ListSDB.DURATION_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get timeframe from item.
	 */
	private static Timestamp getTimeframeFromItem(Item item) {
		return new Timestamp(getLongValueForAttributeFromList(ListSDB.TIMEFRAME_ATTRIBUTE, item.getAttributes()));
	}
	
	/**
	 * Get up data from item.
	 */
	private static long getUpDataFromItem(Item item) {
		return getLongValueForAttributeFromList(ListSDB.UP_DATA_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get down data from item.
	 */
	private static long getDownDataFromItem(Item item) {
		return getLongValueForAttributeFromList(ListSDB.DOWN_DATA_ATTRIBUTE, item.getAttributes());
	}
	
	/**
	 * Get string value for attribute from attribute list.
	 * @param attributeName Name of attribute to find.
	 * @param attributes List of attributes.
	 * @return String value for attribute
	 */
	private static String getStringValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(attributeName)) {
				return attribute.getValue();
			}
		}
		return "";		
	}
	
	/**
	 * Get string value for attribute from attribute list.
	 * @param attributeName Name of attribute to find.
	 * @param attributes List of attributes.
	 * @return String value for attribute
	 */
	private static int getIntValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Integer.parseInt(attribute.getValue());
			}
		}	
		return 0;		
	}	
	
	/**
	 * Get string value for attribute from attribute list.
	 * @param attributeName Name of attribute to find.
	 * @param attributes List of attributes.
	 * @return String value for attribute
	 */
	private static long getLongValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Long.parseLong(attribute.getValue());
			}
		}	
		return 0;		
	}	
	
	/**
	 * Get string value for attribute from attribute list.
	 * @param attributeName Name of attribute to find.
	 * @param attributes List of attributes.
	 * @return String value for attribute
	 */
	private static double getDoubleValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Double.parseDouble(attribute.getValue());
			}
		}	
		return 0;		
	}
}
