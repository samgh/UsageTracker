package com.samgavis.usagetracker;

import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.util.SimpleDBUtils;

public class ListSDBUtils {
	protected static String padTimestamp(Timestamp timestamp) {
		return SimpleDBUtils.encodeZeroPadding(timestamp.getTime(), 20);
	}
	
	protected static String padUpData(long upData) {
		return SimpleDBUtils.encodeZeroPadding(upData, 20);
	}
	
	protected static String padDownData(long downData) {
		return SimpleDBUtils.encodeZeroPadding(downData, 20);
	}
	
	protected static String padLatitude(double latitude) {
		return SimpleDBUtils.encodeZeroPadding((float)latitude, 10);
	}
	
	protected static String padLongitude(double longitude) {
		return SimpleDBUtils.encodeZeroPadding((float)longitude, 20);
	}
	
	
	protected static String getStringValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(attributeName)) {
				return attribute.getValue();
			}
		}
		return "";		
	}
	
	protected static int getIntValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Integer.parseInt(attribute.getValue());
			}
		}	
		return 0;		
	}	
	
	protected static long getLongValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Long.parseLong(attribute.getValue());
			}
		}	
		return 0;		
	}	
	
	protected static double getDoubleValueForAttributeFromList(String attributeName, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals( attributeName)) {
				return Double.parseDouble(attribute.getValue());
			}
		}	
		return 0;		
	}
}
