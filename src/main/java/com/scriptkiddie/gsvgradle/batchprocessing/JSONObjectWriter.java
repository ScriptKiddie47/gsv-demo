package com.scriptkiddie.gsvgradle.batchprocessing;

import java.util.List;

import org.json.JSONObject;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.scriptkiddie.gsvgradle.service.UserDAOService;

public class JSONObjectWriter implements ItemWriter<JSONObject> {

	@Autowired
	UserDAOService daoService;
	
	@Override
	public void write(List<? extends JSONObject> items) throws Exception {
		for (JSONObject jsonObject : items) {
			daoService.insert(jsonObject);
		}
	}
}
