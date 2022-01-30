package com.scriptkiddie.gsvgradle.batchprocessing;

import java.util.Objects;

import org.json.JSONObject;
import org.springframework.batch.item.ItemReader;

import com.scriptkiddie.gsvgradle.util.GsvConstants;

public class JSONObjectReader implements ItemReader<JSONObject> {

	private int entryIndex = 1;

	@Override
	public JSONObject read() throws Exception {
		if (entryIndex < GsvConstants.tableData.size()) {
			JSONObject jsonObject = GsvConstants.tableData.get(entryIndex++);
			if (Objects.nonNull(jsonObject))
				return jsonObject;
		}
		return null;
	}
}
