package com.scriptkiddie.gsvgradle.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public final class GsvConstants {	
	//This List will be reused for each table data Set.
	public static List<JSONObject> tableData = new ArrayList<>();
	//This List will contain the metaData of Each table
	public static JSONObject tableMetaData = new JSONObject();
}
