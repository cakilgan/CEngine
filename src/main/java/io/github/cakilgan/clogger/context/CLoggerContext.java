package io.github.cakilgan.clogger.context;

public class CLoggerContext {
    String contextName;

    public CLoggerContext(String contextName) {
        this.contextName = contextName;
    }
    public String format(){
        return contextName;
    }
}
