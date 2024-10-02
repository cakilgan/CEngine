package io.github.cakilgan.clogger.intf;

import io.github.cakilgan.clogger.context.CLoggerContext;
import io.github.cakilgan.clogger.format.Level;

import java.util.function.Supplier;

public interface ICLogger {
    void log(Level level,String msg);
    void log(Level level, CLoggerContext context, String msg);
    void _alog(Level level,String msg);
    void info(String msg);
    void info (CLoggerContext context,String msg);
    void _ainfo(String msg);
    void warn(String s);
    void warn(CLoggerContext context,String msg);
    void _awarn(String msg);
    void debug(String s);
    void debug(CLoggerContext context,String msg);
    void _adebug(String msg);
    void error(String s);
    void error(CLoggerContext context,String msg);
    void _aerror(String msg);
    void fatal(String s);
    void fatal(CLoggerContext context,String msg);
    void _afatal(String msg);
    void exc(Exception e);
    void exc(Exception e,String msg);
    void exc(Exception e,String msg,CLoggerContext context);
    void excFalse(Exception e);
    void excFalse(Exception e,String msg);
    void excFalse(Exception e,String msg,CLoggerContext context);
    void _aexcFalse(Exception e);
    void lifecycle(String msg);
    void lifecycle(CLoggerContext context,String msg);
    void _alifecycle(String msg);
    <T,R>void execute(T returnObject, Supplier<R> supplier);
}
