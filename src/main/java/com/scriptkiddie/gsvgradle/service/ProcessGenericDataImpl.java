package com.scriptkiddie.gsvgradle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptkiddie.gsvgradle.model.GenericData;

@Service
public class ProcessGenericDataImpl {

	private static Logger log = LoggerFactory.getLogger(ProcessGenericDataImpl.class);

	private static List<GenericData> dataList = new ArrayList<>();

	static {
		dataList.add(new GenericData(1, false, "Almonds, Unsalted, in the blue bag"));
		dataList.add(new GenericData(2, false, "Pizza"));
		dataList.add(new GenericData(3, false, "Burgir"));
	}

	/**
	 * Return dataList
	 * 
	 * @return dataList
	 */
	public List<GenericData> getData() {
		objectsToJsonString("Json Response", dataList);
		return dataList;
	}

	/**
	 * Add GenericData to the dataList.
	 * 
	 * @param data
	 * @return 0 or -1
	 */
	public int addDataToList(GenericData data) {
		objectsToJsonString("Json Request", data);
		long total = dataList.stream().filter(t -> t.getItem().contentEquals(data.getItem())).count();
		log.info("Total :" + total);
		if (total != 0) return -1;
		dataList.add(data);
		return 0;
	}

	/**
	 * Update Checked Value based on ID
	 * @param id
	 * @return 0 or -1
	 */
	public int updateData(int id) {
		objectsToJsonString("Update Request for ID :", id);
		long value = dataList
				.stream()
				.filter(t -> t.getId() == id)
				.peek(t -> t.setChecked(modifyChecked(t.isChecked())))
				.count();
		if (value != 0) return 0;
		return 1;
	}
	
	/**
	 * Delete data based on ID
	 * @param id
	 * @return 0 or -1
	 */
	public int handleDeleteById(int id) {
		objectsToJsonString("Delete Request for ID :", id);
		if (Objects.nonNull(dataList.remove(id - 1))) return 0;
		return 1;
	}


	private boolean modifyChecked(boolean checked) {
		return checked == true ? false : true;
	}

	private void objectsToJsonString(String message, Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info("" + message + " : " + mapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
