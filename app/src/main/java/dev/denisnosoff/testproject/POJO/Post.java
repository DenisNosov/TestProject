package dev.denisnosoff.testproject.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {
	@SerializedName("model")
	@Expose
	private String model;
	@SerializedName("pk")
	@Expose
	private Integer pk;
	@SerializedName("fields")
	@Expose
	private Fields fields;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}
}
