package io.github.cakilgan.clogger.system;

import io.github.cakilgan.clogger.CLogger;

import java.util.HashMap;

public class CLoggerSystem {
    static final CLoggerSystem INSTANCE = new CLoggerSystem();
    public static CLogger logger(Class<?> loggerclass){
        return INSTANCE.getLogger(loggerclass);
    }
    private final CLogger MAIN_LOGGER = CLogger.getInstance();
    private final HashMap<Class<?>,CLogger> LOGGERS = new HashMap<>();
    public void addLogger(CLogger logger){
        if (LOGGERS.containsKey(logger.getaClass()))
            return;
        this.LOGGERS.put(logger.getaClass(),logger);
    }
    public CLogger getLogger(Class<?> aClass){
        if (!LOGGERS.containsKey(aClass)){
            CLogger rtrn = new CLogger(aClass);
            addLogger(rtrn);
        }
        return LOGGERS.get(aClass);
    }
}
