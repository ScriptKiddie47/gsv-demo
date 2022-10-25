package com.scriptkiddie.gsvgradle.service;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.scriptkiddie.gsvgradle.util.GsvConstants;

@Service
public class ProcessSpreadsheets {

	private static final Logger log = LoggerFactory.getLogger(ProcessSpreadsheets.class);

	public void getSpreadsheetData(String filePath, String rowsToSkip) {

		// [1] Clear table data list
		GsvConstants.tableData.clear();

		// [2] Set Rows to Skip
		GsvConstants.RowsToSkip = Integer.parseInt(rowsToSkip);

		// [3] Clear 

		try {
			processAllSheets(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		processFirstRow(filePath);
		log.info("Table Data:" + GsvConstants.tableData);
		log.info("Table Meta Data:" + GsvConstants.tableMetaData);
	}

	/**
	 * @param filePath
	 *                 Even though the method name is processAllSheets , we only
	 *                 process the 1st sheet of any given excel file.
	 *                 Techinique used : XSSF and SAX (Event API)
	 *                 https://poi.apache.org/components/spreadsheet/how-to.html
	 */
	private void processAllSheets(String filePath) throws Exception {
		log.info("Rows to Skip : " + GsvConstants.RowsToSkip);

		OPCPackage pkg = OPCPackage.open(filePath);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = (SharedStringsTable) r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			System.out.println("Sheet Processed");
			break; // Breaks after processing the 1st sheet.
		}
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
		XMLReader parser = XMLHelper.newXMLReader();
		ContentHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	private String getMeATableName(String f) {
		String[] words = f.split("\\\\");
		for (String s : words) {
			if (s.contains(".xlsx")) {
				return s.replace(".xlsx", "").replace("-", "_").replace(" ","") + "_TBL";
			}
		}
		return null;
	}

	private void processFirstRow(String filePath) {
		String tableName = getMeATableName(filePath);
		GsvConstants.TABLENAME = tableName;
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = GsvConstants.tableData.get(0);
		jsonObject.put("0", tableName);
		jsonObject2.keySet().forEach(keyStr -> {
			Object keyvalue = jsonObject2.get(keyStr);
			int n = Integer.parseInt(keyStr);
			jsonObject.put(Integer.toString(n+1), keyvalue);
		});
		GsvConstants.tableMetaData.put(jsonObject);
	}
}