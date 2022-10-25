package com.scriptkiddie.gsvgradle.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public final class GsvConstants {	
	//This List will be reused for each table data Set.
	public static List<JSONObject> tableData = new ArrayList<>();

	//This List will contain the metaData of Each table
	public static JSONArray tableMetaData = new JSONArray();

	// CSV File Paths & Skip Lines
	public static List<List<String>> records = new ArrayList<>();

	// File List from each CSV file Paths
	public static List<String> filePaths = new ArrayList<>();

	// Rows to Skip
	public static int RowsToSkip = 0;

	// Table name
	public static String TABLENAME = null;
}
