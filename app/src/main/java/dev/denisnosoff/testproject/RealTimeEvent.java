package dev.denisnosoff.testproject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class RealTimeEvent {

	@SerializedName("command")
	private String event;

	public RealTimeEvent(String event, String lastTime) {
		this.event = event;
		this.lastTime = lastTime;
	}

	@SerializedName("last_timestamp")
	private String lastTime;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
