package com.scriptkiddie.gsvgradle.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserDAOService {

	@PersistenceContext
	private EntityManager entityManager;

	public void createTable(JSONObject jsonObject) {
		Query q = entityManager.createNativeQuery(buildTableCreationQuery(jsonObject));
		q.executeUpdate();
	}

	public void insert(JSONObject jsonObject) {
		Query q = entityManager.createNativeQuery(buildInsertQuery(jsonObject));
		q.executeUpdate();
	}

	private String buildTableCreationQuery(JSONObject jsonObject) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE Office_Supply_TBL ( ");
		jsonObject.keySet().forEach((e) -> sb.append(jsonObject.get(e) + " VARCHAR(50),"));
		sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
		sb.append(");");
		return sb.toString().toUpperCase();
	}

	private String buildInsertQuery(JSONObject jsonObject) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO Office_Supply_TBL VALUES (");
		jsonObject.keySet().forEach((e) -> sb.append("'" + jsonObject.get(e) + "',"));
		sb.setLength(sb.length() - 1); // Remove last ',' ( Comma )
		sb.append(");");
		return sb.toString().toUpperCase();
	}
}