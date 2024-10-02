package io.github.cakilgan.engine.logger;

import io.github.cakilgan.clogger.context.CLoggerContext;

public class CommonContexts {
    public static CLoggerContext INPUT,RESOURCE,EVENT,NETWORK,COMPONENT_SYSTEM;
    static{
        INPUT = new CLoggerContext("input"){
            @Override
            public String format() {
                return "common/input$";
            }
        };

        EVENT = new CLoggerContext("event"){
            @Override
            public String format() {
                return "common/event$";
            }
        };

        RESOURCE = new CLoggerContext("input"){
            @Override
            public String format() {
                return "common/resource$";
            }
        };

        NETWORK= new CLoggerContext("network"){
            @Override
            public String format() {
                return "common/network$";
            }
        };
        COMPONENT_SYSTEM = new CLoggerContext("compsystem"){
            @Override
            public String format() {
                return "common/comp_system$";
            }
        };
    }
}
