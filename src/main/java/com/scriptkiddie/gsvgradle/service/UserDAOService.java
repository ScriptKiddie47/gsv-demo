package com.scriptkiddie.gsvgradle.service;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scriptkiddie.gsvgradle.util.GsvConstants;

@Repository
@Transactional
public class UserDAOService {

	private static final Logger log = LoggerFactory.getLogger(UserDAOService.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void createTable(JSONObject jsonObject, String file) {
		Query q = entityManager.createNativeQuery(buildTableCreationQueryStream(jsonObject,file));
		q.executeUpdate();
	}

	public void insert(JSONObject jsonObject) {
		Query q = entityManager.createNativeQuery(buildInsertQueryStream(jsonObject));
		q.executeUpdate();
	}

	/**
	 * Will let the Front End take care of the SQL Query
	 * 
	 * @param paraSet
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getData(String paraSet) {
		log.info("Table Queried with Request: " + paraSet);
		Query q = entityManager.createNativeQuery("SELECT * FROM " + paraSet + " LIMIT 100");

		// TODO : Using Jackson and Java 8 streams:
		// query.getResultList().stream().map(objectMapper::convertValue).collect(Collectors.toList());
		// of What? Need to explore

		List<Object[]> list = q.getResultList();
		JSONArray jArray = new JSONArray();
		JSONObject tableData = fetchTableNameFromJsonArray(paraSet);
		list.forEach(t -> JSONObjectTransformer(t, jArray, tableData));
		return jArray;
	}

	public JSONArray getTableMetaData() {
		return GsvConstants.tableMetaData;
	}

	private void JSONObjectTransformer(Object[] t, JSONArray jArray, JSONObject tableData) {
		JSONObject jsonObject = new JSONObject();
		int counter = 0;
		for (Object v : t) {
			jsonObject.put(tableData.getString(Integer.toString(++counter)), v);
		}
		jArray.put(jsonObject);
	}

	// private String buildTableCreationQuery(JSONObject jsonObject) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("CREATE TABLE Office_Supply_TBL ( ");
	// jsonObject.keySet().forEach((e) -> sb.append(jsonObject.get(e) + "
	// VARCHAR(50),"));
	// sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
	// sb.append(");");
	// return sb.toString().toUpperCase();
	// }

	private String buildTableCreationQueryStream(JSONObject jsonObject, String file) {
		String tableName = GsvConstants.TABLENAME;
		final StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + tableName + " ( ");
		jsonObject.keySet().stream().forEach(e -> sb.append(jsonObject.get(e) + " VARCHAR(50),"));
		sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
		sb.append(");");
		log.info("TABLE CREATION QUERY : " + sb);
		return sb.toString().toUpperCase();
	}

	// private String buildInsertQuery(JSONObject jsonObject) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("INSERT INTO Office_Supply_TBL VALUES (");
	// jsonObject.keySet().forEach((e) -> sb.append("'" + jsonObject.get(e) +
	// "',"));
	// sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
	// sb.append(");");
	// return sb.toString().toUpperCase();
	// }

	private String buildInsertQueryStream(JSONObject jsonObject) {
	
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + GsvConstants.TABLENAME + " VALUES (");
		jsonObject.keySet().stream().forEach(e -> sb.append("'" + jsonObject.get(e) + "',"));
		sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
		sb.append(");");
		
		return sb.toString().toUpperCase();
	}

	private String fetchTableName(String paraSet) {
		String[] words = paraSet.split(" ");
		for (String s : words) {
			if (s.contains("_TBL")) {
				return s;
			}
		}
		return null;
	}

	private JSONObject fetchTableNameFromJsonArray(String paraSet) {
		String tableName = fetchTableName(paraSet);
		for (int i = 0; i < GsvConstants.tableMetaData.length(); i++) {
			JSONObject jsonObject = GsvConstants.tableMetaData.getJSONObject(i);
			if (Objects.nonNull(jsonObject) && jsonObject.get("0").toString().equalsIgnoreCase(tableName)) {
				return jsonObject;
			}
		}
		return null;
	}
}