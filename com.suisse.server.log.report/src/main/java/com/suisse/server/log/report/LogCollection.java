package com.suisse.server.log.report;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.suisse.server.log.report.model.Log;

public class LogCollection {
	private static LogCollection sSoleInstance;
	Map<String, Log> logData = Collections.synchronizedMap(new HashMap<>());	 

	//private constructor.
    private <T> LogCollection(){
        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    } 

    public static LogCollection getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new LogCollection();
        }

        return sSoleInstance;
    }
    
    public void addLog(Log log) {
    	logData.put(log.getId(), log);
    }
    
    public boolean isLogExist(Log log) {
    	return logData.containsKey(log.getId());
    }
    
    public Log popLog(String id) {
    	return logData.remove(id);
    }
    
}
