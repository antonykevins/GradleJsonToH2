package com.suisse.server.log.report.model;

public class ApplicationLog extends Log {
	private String type;
	private String host;
	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the host
	 */
	public final String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public final void setHost(String host) {
		this.host = host;
	}
}
