
package com.mbach231.cardinal;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class CardinalLogger {

    public enum LogID {

        Debug,
        Initialization,
        SeasonTemperatureInfo,
        Database,
        Scheduler,
        RitualEvent,
        DateChangeEvent,
        ChangeSeasonEvent
    }

    private static Logger logger_;
    private static Map<LogID, Boolean> enabledLoggingMap_;

    public static void initialize(Logger logger) {

            logger_ = logger;

            enabledLoggingMap_ = new HashMap();
            for (LogID id : LogID.values()) {
                enabledLoggingMap_.put(id, false);
            }

            for (String idString : ConfigManager.getDefaultConfig().getConfigurationSection("EnableLogging").getValues(false).keySet()) {
                LogID id = LogID.valueOf(idString);
                if(id != null) {
                    enabledLoggingMap_.put(id, ConfigManager.getDefaultConfig().getBoolean("EnableLogging." + idString));
                }
            }
        
    }

    public static void log(LogID id, String msg) {
        if (enabledLoggingMap_.get(id)) {
            logger_.log(Level.INFO, msg);
        }
    }
    
    public static boolean isEnabled(LogID id) {
        return enabledLoggingMap_.get(id);
    }

}
