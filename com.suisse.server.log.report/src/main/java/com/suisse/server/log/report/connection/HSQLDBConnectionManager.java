package com.suisse.server.log.report.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.suisse.server.log.report.LogReport;

public class HSQLDBConnectionManager {

	private static final Logger LOGGER = Logger.getLogger(LogReport.class);
	private static HSQLDBConnectionManager instance;

	private String db = "jdbc:hsqldb:hsql://localhost/logdb";
	private String user = "sa";
	private String password = "";

	private GenericObjectPool<PoolableConnection> connectionPool = null;
	private DataSource dataSource = null;

	// private constructor.
	private <T> HSQLDBConnectionManager() {

		// Prevent form the reflection api.
		if (instance != null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
	}

	public static synchronized HSQLDBConnectionManager getInstance() {
		if (instance == null) { // if there is no instance available... create new one
			instance = new HSQLDBConnectionManager();
		}
		return instance;
	}

	public void closeConnection() {
		try {
			// Close connection
			if (getConnection() != null)
				getConnection().close();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public Connection getConnection() throws SQLException {
		if (dataSource == null)
			openConnection();
		return dataSource.getConnection();
	}

	private DataSource setupConnectionPool() {
		// Creates a connection factory object which will be use by
		// the pool to create the connection object. We passes the
		// JDBC url info, username and password.
		ConnectionFactory cf = new DriverManagerConnectionFactory(db, user, password);

		// Creates a PoolableConnectionFactory that will wraps the
		// connection object created by the ConnectionFactory to add
		// object pooling functionality.
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, null);
		pcf.setValidationQuery("SELECT 1");

		// Creates an instance of GenericObjectPool that holds our
		// pool of connections object.
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(10);
		connectionPool = new GenericObjectPool<>(pcf);
		pcf.setPool(connectionPool);

		return new PoolingDataSource<>(connectionPool);
	}

	public void openConnection() {
		if (dataSource == null) {
			try {
				dataSource = setupConnectionPool();
				LOGGER.info("DB connection established!");

				Statement stmt = dataSource.getConnection().createStatement();

				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS LogEvent ( id VARCHAR(15) NOT NULL, duration BIGINT NOT NULL, "
						+ "type VARCHAR(20), host VARCHAR(20), alert BOOLEAN, PRIMARY KEY (id));");
				LOGGER.info("Table LogEvent created!");
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

	}
}
