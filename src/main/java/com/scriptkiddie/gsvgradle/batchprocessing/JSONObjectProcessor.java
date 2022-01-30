package com.scriptkiddie.gsvgradle.batchprocessing;

import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;

/**
 * A common paradigm in batch processing is to ingest data, transform it, and
 * then pipe it out somewhere else. We will try to skip/jump this later.
 * @author ritam
 */
public class JSONObjectProcessor implements ItemProcessor<JSONObject, JSONObject> {

	@Override
	public JSONObject process(JSONObject item) throws Exception {
		return item;
	}
}
