package com.scriptkiddie.gsvgradle.model;


public class GenericData {
	
	private int id;
	private boolean checked;
	private String item;

	public GenericData() {
	}

	public GenericData(int id, boolean checked, String item) {
		super();
		this.id = id;
		this.checked = checked;
		this.item = item;
	}
	
	public int getId() {
		return id;
	}
	public boolean isChecked() {
		return checked;
	}
	public String getItem() {
		return item;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "GenericData [id=" + id + ", checked=" + checked + ", item=" + item + "]";
	}
	
}
