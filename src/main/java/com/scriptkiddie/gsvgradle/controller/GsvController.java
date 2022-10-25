package com.scriptkiddie.gsvgradle.controller;

import java.util.Objects;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scriptkiddie.gsvgradle.service.UserDAOService;

@CrossOrigin
@RestController
public class GsvController {

	@Autowired
	private UserDAOService daoService;

	@RequestMapping(method = RequestMethod.GET, path = "/showMeData/{q}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMESomeData(@PathVariable(required = false) String q) {
		JSONArray jsonArray = daoService.getData(q);
		if (Objects.nonNull(jsonArray))
			return ResponseEntity.ok(jsonArray.toString());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		// TODO : Improve Code : https://www.baeldung.com/spring-request-param
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getTablesMetaData", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> allTableData(@PathVariable(required = false) String q) {
		JSONArray jsonArray = daoService.getTableMetaData();
		if (Objects.nonNull(jsonArray))
			return ResponseEntity.ok(jsonArray.toString());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}