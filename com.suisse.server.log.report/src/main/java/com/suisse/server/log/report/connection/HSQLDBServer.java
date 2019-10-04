package com.suisse.server.log.report.connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hsqldb.persist.HsqlProperties;

import com.suisse.server.log.report.model.Log;

public class HSQLDBServer {

	private static HSQLDBServer sSoleInstance;
	Map<String, Log> logData = Collections.synchronizedMap(new HashMap<>());
	private boolean server_started;
	private org.hsqldb.Server server;

	// private constructor.
	private <T> HSQLDBServer() {
		// Prevent form the reflection api.
		if (sSoleInstance != null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
	}

	public static HSQLDBServer getInstance() {
		if (sSoleInstance == null) { // if there is no instance available... create new one
			sSoleInstance = new HSQLDBServer();
		}
		return sSoleInstance;
	}

	public void start() {
		if (server_started)
			return;

		HsqlProperties props = new HsqlProperties();
		props.setProperty("server.database.0", "file:C:\\Users\\KEVIN\\logdb");
		props.setProperty("server.dbname.0", "logdb");
		server = new org.hsqldb.Server();

		try {
			server.setProperties(props);
		} catch (Exception e) {
			return;
		}
		server_started = true;
		server.start();
	}

	public void shutdown() {
		if (server_started) {
			server.shutdown();
			server_started = false;
		}
	}

}
