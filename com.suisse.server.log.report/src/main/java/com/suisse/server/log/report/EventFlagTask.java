/**
 * 
 */
package com.suisse.server.log.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suisse.server.log.report.connection.HSQLDBConnectionManager;
import com.suisse.server.log.report.model.ApplicationLog;
import com.suisse.server.log.report.model.Log;

/**
 * @author kevin
 *
 */
public class EventFlagTask implements Runnable {

	private List<String> content;
	private static final Logger LOGGER = Logger.getLogger(LogReport.class);
	
	public EventFlagTask(List<String> content) {
		this.content = content;
	}

	@Override
	public void run() {
		// Process the list and add it into Collection or DB
		ObjectMapper mapper = new ObjectMapper();
		for (String string : content) {
			try {
				
				Log log = mapper.readValue(string, ApplicationLog.class);
				
				LogCollection logCollection = LogCollection.getInstance();
				
				if(logCollection.isLogExist(log)) {
					Log log2 = logCollection.popLog(log.getId());
					long duration = Math.abs(log.getTimestamp()-log2.getTimestamp());

					//insert into DB
					HSQLDBConnectionManager connectionManager = HSQLDBConnectionManager.getInstance();
					Connection connection = connectionManager.getConnection();
					synchronized (connection) {
						
						String query ="INSERT INTO LogEvent (id, duration, type, host, alert) VALUES (?,?,?,?,?)";
						PreparedStatement insertStatement = connection.prepareStatement(query);
						LOGGER.info("LogEvent insert statement prepared!");

						insertStatement.setString(1, log.getId());
						insertStatement.setLong(2, duration);
						if(log instanceof ApplicationLog) {
							insertStatement.setString(3, ((ApplicationLog)log).getType());	
							insertStatement.setString(4, ((ApplicationLog)log).getHost());
						}else {
							insertStatement.setString(3, "");
							insertStatement.setString(4, "");
						}
						if(duration > 4) {
							insertStatement.setBoolean(5, true);
						}else {
							insertStatement.setBoolean(5, false);
						}
						int i = insertStatement.executeUpdate();
						LOGGER.info(log.getId()+" :Rows affected: "+i);
					}
				}else {
					logCollection.addLog(log);
				}
				
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}

}
