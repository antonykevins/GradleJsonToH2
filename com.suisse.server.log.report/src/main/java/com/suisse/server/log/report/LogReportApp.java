package com.suisse.server.log.report;

import java.io.IOException;

import org.apache.log4j.Logger;

public class LogReportApp {
	private static final Logger LOGGER = Logger.getLogger(LogReport.class);
	
	public static void main(String[] args) {
		LogReport report = new LogReport();
		try {
			report.readLogUsingScanner();
		} catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(),e);
		}
	}

}
