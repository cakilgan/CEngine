package io.github.cakilgan.engine.logger;

import io.github.cakilgan.clogger.context.CLoggerContext;

public class EngineContexts {
    public static CLoggerContext START,INIT,LOOP,DISPOSE,EXCEPTION, STOP;
    static {
        START = new CLoggerContext("start"){
            @Override
            public String format() {
                return "cengine/start# ";
            }
        };
        INIT = new CLoggerContext("init"){
            @Override
            public String format() {
                return "cengine/init# ";
            }
        };
        LOOP = new CLoggerContext("loop"){
            @Override
            public String format() {
                return "cengine/loop# ";
            }
        };
        DISPOSE = new CLoggerContext("dispose"){
            @Override
            public String format() {
                return "cengine/dispose# ";
            }
        };
        EXCEPTION = new CLoggerContext("exc"){
            @Override
            public String format() {
                return "cengine/exception# ";
            }
        };
        STOP = new CLoggerContext("stop"){

            @Override
            public String format() {
                return "cengine/stop# ";
            }
        };
    }
}
