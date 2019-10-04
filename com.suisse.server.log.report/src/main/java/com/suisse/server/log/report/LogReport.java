package com.suisse.server.log.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.suisse.server.log.report.connection.HSQLDBConnectionManager;
import com.suisse.server.log.report.connection.HSQLDBServer;

public class LogReport {
	private static final Logger LOGGER = Logger.getLogger(LogReport.class);
	ExecutorService executor = Executors.newCachedThreadPool();
	
	public void readLogUsingScanner() throws IOException {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			
			Scanner sc1 = new Scanner(System.in);
	 		System.out.println("Enter the path of the logfile.txt  (Eg. E:/Programming/)"); 
	 		String s = sc1.nextLine();
	 		
			inputStream = new FileInputStream(s+"/logfile.txt");
			sc1 = new Scanner(inputStream, "UTF-8");
			int lineCount=0;
			List<String> content = new ArrayList<String>();
			HSQLDBServer.getInstance().start();
			HSQLDBConnectionManager.getInstance().openConnection();
			
			while (sc1.hasNextLine()) {
				String line = sc1.nextLine();
				content.add(line);
				lineCount++;
				if(lineCount == 100) {
					lineCount=0;
					executor.execute(new EventFlagTask(content));
					content=new ArrayList<String>();
				}
			}
			if(lineCount!=0) {
				lineCount=0;
				executor.execute(new EventFlagTask(content));
				content=new ArrayList<String>();
			}
			
			// note that Scanner suppresses exceptions
			if (sc1.ioException() != null) {
				throw sc1.ioException();
			}
		} finally {
			executor.shutdown();
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {}
			
			HSQLDBConnectionManager.getInstance().closeConnection();
			HSQLDBServer.getInstance().shutdown();
			if (inputStream != null) {
				inputStream.close();
			}
//			if (sc1 != null) {
//				sc1.close();
//			}
		}
	}

	public boolean someLibraryMethod() {
		return true;
	}

}
