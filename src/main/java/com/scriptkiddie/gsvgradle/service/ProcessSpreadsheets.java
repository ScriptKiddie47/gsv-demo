package com.scriptkiddie.gsvgradle.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.scriptkiddie.gsvgradle.util.GsvConstants;

@Service
public class ProcessSpreadsheets {

	public void getSpreadsheetData() {
		JSONObject jsonObject = null;
		try (FileInputStream file = new FileInputStream(
				new File("C:\\Users\\ritam\\Desktop\\DataSets\\Office Supply.xlsx"));
				XSSFWorkbook workbook = new XSSFWorkbook(file);) {

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			processFirstRow(rowIterator.next()); // Skip First Row //Minor Risk of getting an empty sheet.
			
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
						jsonObject.put(Integer.toString(counter++),Double.toString(cell.getNumericCellValue()));
						break;
					case STRING:
						jsonObject.put(Integer.toString(counter++),cell.getStringCellValue());
						break;
					case _NONE:
						break;
					default:
						counter++;
						break;
					}
				}
				GsvConstants.tableData.add(jsonObject);
				System.out.println(jsonObject.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFirstRow(Row firstRow) {
		JSONObject jsonObject = new JSONObject();
		Iterator<Cell> cellIterator = firstRow.cellIterator();
		int counter = 0;
		while (cellIterator.hasNext()) jsonObject.put(Integer.toString(++counter), cellIterator.next().getStringCellValue());
		GsvConstants.tableData.add(jsonObject);
	}
}
