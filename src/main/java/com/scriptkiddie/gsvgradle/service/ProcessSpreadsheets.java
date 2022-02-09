package com.scriptkiddie.gsvgradle.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.scriptkiddie.gsvgradle.util.GsvConstants;

@Service
public class ProcessSpreadsheets {

	
	private static final Logger log = LoggerFactory.getLogger(ProcessSpreadsheets.class);

	
	public void getSpreadsheetData() {
		JSONObject jsonObject = null;
		try (FileInputStream file = new FileInputStream(
				new File("/home/sbala/Documents/DataSets/Historicalinvesttemp.xlsx"));
				XSSFWorkbook workbook = new XSSFWorkbook(file);) {

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			String tableName = "Historicalinvesttemp"+"_TBL";
			processFirstRow(rowIterator.next(),tableName); // Skip First Row //Minor Risk of getting an empty sheet.
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				Iterator<Cell> cellIterator = row.cellIterator();
				
				int counter = 0;
				jsonObject = new JSONObject();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case BLANK:
						break;
					case BOOLEAN:
						break;
					case ERROR:
						break;
					case FORMULA:
						break;
					case NUMERIC:
						jsonObject.put(Integer.toString(++counter),Double.toString(cell.getNumericCellValue()));
						break;
					case STRING:
						jsonObject.put(Integer.toString(++counter),cell.getStringCellValue());
						break;
					case _NONE:
						break;
					default:
						counter++;
						break;
					}
				}
				if (!jsonObject.isEmpty()) {
					jsonObject.put("0",tableName);
					GsvConstants.tableData.add(jsonObject);
					//log.info(jsonObject.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFirstRow(Row firstRow, String tableName) {
		JSONObject jsonObject = new JSONObject();
		Iterator<Cell> cellIterator = firstRow.cellIterator();
		int counter = 0;
		jsonObject.put("0",tableName);
		while (cellIterator.hasNext()) jsonObject.put(Integer.toString(++counter), cellIterator.next().getStringCellValue().replace(".", "_").replace(" ","_"));
		GsvConstants.tableData.add(jsonObject);
		GsvConstants.tableMetaData.put(tableName, jsonObject);
		log.info("TABLE STRUCTURE : " + GsvConstants.tableMetaData.toString());
	}
}
