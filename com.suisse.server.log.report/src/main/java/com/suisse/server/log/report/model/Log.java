package com.suisse.server.log.report.model;

public class Log {
	private String id;
	private String state;
	private long timestamp;
	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the state
	 */
	public final String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public final void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
