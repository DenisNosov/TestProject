package dev.denisnosoff.testproject.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

	@SerializedName("owner")
	@Expose
	private Integer owner;
	@SerializedName("text")
	@Expose
	private String text;
	@SerializedName("attachment")
	@Expose
	private String attachment;
	@SerializedName("timestamp")
	@Expose
	private String timestamp;

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
