package com.scriptkiddie.gsvgradle.controller;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scriptkiddie.gsvgradle.model.GenericData;
import com.scriptkiddie.gsvgradle.service.ProcessGenericDataImpl;
import com.scriptkiddie.gsvgradle.service.ProcessSpreadsheets;
import com.scriptkiddie.gsvgradle.service.UserDAOService;

@CrossOrigin
@RestController
public class GsvController {

	@Autowired
	private ProcessGenericDataImpl genericDataImpl;
	@Autowired
	private ProcessSpreadsheets spreadsheets;
	@Autowired
	private UserDAOService daoService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/showMeData/{q}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMESomeData(@PathVariable(required = false) String q ) {
		JSONArray jsonArray = daoService.getData(q);
		if (Objects.nonNull(jsonArray)) return ResponseEntity.ok(jsonArray.toString());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		// TODO : Improve Code : https://www.baeldung.com/spring-request-param
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/items")
	public List<GenericData> getData() {
		return genericDataImpl.getData();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/items")
	public ResponseEntity<Void> setData(@RequestBody GenericData data) {
		if (genericDataImpl.addDataToList(data) == 0)
			return ResponseEntity.ok().build();
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@RequestMapping(method = RequestMethod.PATCH, path = "/items/{id}")
	public ResponseEntity<Void> updateData(@PathVariable int id) {
		if (genericDataImpl.updateData(id) == 0)
			return ResponseEntity.ok().build();
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/items/{id}")
	public ResponseEntity<Void> deleteData(@PathVariable int id) {
		if (genericDataImpl.handleDeleteById(id) == 0)
			return ResponseEntity.ok().build();
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	}

//	@RequestMapping(method = RequestMethod.GET, path = "/printSheets", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getSpreads() {
//		JSONObject jsonObject = spreadsheets.getSpreadsheetData();
//		if (Objects.nonNull(jsonObject)) return ResponseEntity.ok(jsonObject.toString());
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/getTablesMetaData",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> allTableData(@PathVariable(required = false) String q ) {
		JSONObject jsonObject = daoService.getTableMetaData();
		if (Objects.nonNull(jsonObject)) return ResponseEntity.ok(jsonObject.toString());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}