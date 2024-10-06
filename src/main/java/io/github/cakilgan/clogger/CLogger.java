package io.github.cakilgan.clogger;

import io.github.cakilgan.clogger.format.CLoggerFormatter;
import io.github.cakilgan.clogger.comp.CLoggerPrintStream;
import io.github.cakilgan.clogger.format.ElementWithType;
import io.github.cakilgan.clogger.context.CLoggerContext;
import io.github.cakilgan.clogger.format.CLColor;
import io.github.cakilgan.clogger.intf.ICLogger;
import io.github.cakilgan.clogger.format.Level;
import io.github.cakilgan.clogger.util.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.io.PrintStream;
import java.util.*;

public class CLogger implements ICLogger {
    public static String NAMESPACE = "[cakilgan;::;CLogger]";
    public static String COLORIZED_NAMESPACE = Text.colorize(NAMESPACE, CLColor.PURPLE_BRIGHT);
    public static CLogger getInstance() {
        return INSTANCE;
    }
    static CLogger INSTANCE = new CLogger();

    private static PrintStream old;
    private static CLoggerPrintStream New;
    private static List<String> GLOBAL_LOG_HISTORY;
    private static Level GLOBAL_LOG_LEVEL;
    private static boolean globalDebugMode = true;

    private final Class<?> aClass;
    private List<String> LOG_HISTORY;
    private Level LOG_LEVEL;
    private CLoggerFormatter formatter;
    private CLoggerContext currentContext;
    private boolean debugMode = true;
    private boolean loggerValue = true;

    public CLogger(Class<?> aClass,CLoggerFormatter formatter) {
        this.aClass = aClass;
        this.formatter = formatter;
        this.LOG_HISTORY =  Collections.synchronizedList(new ArrayList<>());
    }
    public CLogger(Class<?> aClass){
        this.aClass = aClass;
        this.formatter= CLoggerFormatter.getDefaultFormatter();
        this.LOG_HISTORY =  Collections.synchronizedList(new ArrayList<>());
    }


    private CLogger(){
        if (GLOBAL_LOG_HISTORY==null){
            GLOBAL_LOG_HISTORY =  Collections.synchronizedList(new ArrayList<>());
        }
        this.LOG_HISTORY = new ArrayList<>();
        this.aClass = CLogger.class;
        old = System.out;
        New = new CLoggerPrintStream(old);
        System.setOut(New);
    }
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private void asyncLog(Level level, String msg) {
        executor.submit(() -> {
            _log(level, msg);
        });
    }

    private void packet(){
        String logString = this.formatter.parse(this.formatter.unparse(this.formatter.getElements()));
        if (LOG_HISTORY.size()>500){
            LOG_HISTORY = new ArrayList<>();
        }
        LOG_HISTORY.add(Text.removeColorCodes(logString));
        New.println(this.formatter.unparse(this.formatter.getElements()),this.formatter);
    }
    private void _log(Level level,String msg){
        if (!loggerValue){
            return;
        }
        ElementWithType<String> context = (ElementWithType<String>) formatter.getElements().get("context");
        context.setType(Objects.requireNonNullElse(currentContext,new CLoggerContext("")).format());
        ElementWithType<String> classElement = (ElementWithType<String>) formatter.getElements().get("className");
        classElement.setType(aClass.getSimpleName());
        ElementWithType<String> msgElement = (ElementWithType<String>) formatter.getElements().get("msg");
        msgElement.setType(msg);
        ElementWithType<String> levelElement = (ElementWithType<String>) formatter.getElements().get("level");
        levelElement.setType(level.toString().toLowerCase(Locale.ROOT));
        formatter.getElements().put("msg",msgElement);
        formatter.getElements().put("level",levelElement);
        packet();
    }
    private boolean _shouldLog(Level level){
        return LOG_LEVEL == level||GLOBAL_LOG_LEVEL==level;
    }

    @Override
    public void log(Level level, String msg) {
        if (LOG_LEVEL!=null||GLOBAL_LOG_LEVEL!=null){
            if (!_shouldLog(level)){
                return;
            }
        }
        _log(level, msg);
    }

    @Override
    public void log(Level level, CLoggerContext context, String msg) {
        if (LOG_LEVEL!=null||GLOBAL_LOG_LEVEL!=null){
            if (!_shouldLog(level)){
                return;
            }
        }
        _log(level, context.format()+msg);
    }

    @Override
    public void _alog(Level level, String msg) {
        asyncLog(level,msg);
    }

    @Override
    public void info(String msg) {
        log(Level.INFO,msg);
    }

    @Override
    public void info(CLoggerContext context, String msg) {
        log(Level.INFO,context,msg);
    }

    @Override
    public void _ainfo(String msg) {
        _alog(Level.INFO,msg);
    }

    @Override
    public void warn(String s) {
        log(Level.WARNING,s);
    }

    @Override
    public void warn(CLoggerContext context, String msg) {
        log(Level.WARNING,context,msg);
    }

    @Override
    public void _awarn(String msg) {
        _alog(Level.WARNING,msg);
    }

    @Override
    public void debug(String s) {
        if (debugMode&&globalDebugMode){
            log(Level.DEBUG,s);
        }
    }

    @Override
    public void debug(CLoggerContext context, String msg) {
        if (debugMode&&globalDebugMode){
            log(Level.DEBUG,context,msg);
        }
    }

    @Override
    public void _adebug(String msg) {
        _alog(Level.DEBUG,msg);
    }

    @Override
    public void error(String s) {
        log(Level.ERROR,s);
    }

    @Override
    public void error(CLoggerContext context, String msg) {
        log(Level.ERROR,context,msg);
    }

    @Override
    public void _aerror(String msg) {
        _alog(Level.ERROR,msg);
    }

    @Override
    public void fatal(String s) {
        log(Level.FATAL,s);
    }

    @Override
    public void fatal(CLoggerContext context, String msg) {
        log(Level.FATAL,context,msg);
    }

    @Override
    public void _afatal(String msg) {
        _alog(Level.FATAL,msg);
    }


    private void _exc(boolean bool,Exception e){
        fatal(e.getLocalizedMessage());
        if (bool){
            try {
                throw  e;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    @Override
    public void exc(Exception e) {
        _exc(true,e);
    }

    @Override
    public void exc(Exception e, String msg) {
        fatal(msg+" - "+e.getLocalizedMessage());
        try {
            throw e ;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void exc(Exception e, String msg, CLoggerContext context) {
        fatal(context.format()+" "+msg+" - "+e.getLocalizedMessage());
        try {
            throw  e;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void excFalse(Exception e) {
        _exc(false,e);
    }

    @Override
    public void excFalse(Exception e, String msg) {
        fatal(msg+" - "+e.getLocalizedMessage());
    }

    @Override
    public void excFalse(Exception e, String msg, CLoggerContext context) {
        fatal(context.format()+" "+msg+" - "+e.getLocalizedMessage());
    }

    @Override
    public void _aexcFalse(Exception e) {
        _afatal(e.getMessage());
    }

    @Override
    public void lifecycle(String msg) {
        log(Level.LIFECYCLE,msg);
    }

    @Override
    public void lifecycle(CLoggerContext context, String msg) {
        log(Level.LIFECYCLE,context,msg);
    }

    @Override
    public void _alifecycle(String msg) {
        _alog(Level.LIFECYCLE,msg);
    }

    @Override
    public <T, R> void execute(T returnObject, Supplier<R> supplier) {
        info(returnObject.toString()+" - "+supplier.get());
    }

    public Class<?> getaClass() {
        return aClass;

    }
    public List<String> getLogHistory() {
        return LOG_HISTORY;
    }
    public Level getLogLevel() {
        return LOG_LEVEL;
    }
    public CLoggerContext getCurrentContext() {
        return currentContext;
    }

    public void disableLogger(){
        this.loggerValue = false;
    }
    public void enableLogger(){
        this.loggerValue = true;
    }
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    public void setLogLevel(Level LOG_LEVEL) {
        this.LOG_LEVEL = LOG_LEVEL;
    }
    public void setCurrentContext(CLoggerContext currentContext) {
        this.currentContext = currentContext;
    }
    public void setExecutorService(ExecutorService executorService) {
        this.executor.shutdownNow();
        this.executor = executorService;
    }

    public static void setGlobalDebugMode(boolean globalDebugMode) {
        CLogger.globalDebugMode = globalDebugMode;
    }
    public static void setGlobalLogLevel(Level globalLogLevel) {
        GLOBAL_LOG_LEVEL = globalLogLevel;
    }

    public static List<String> getGlobalLogHistory() {
        return GLOBAL_LOG_HISTORY;
    }
    public static Level getGlobalLogLevel() {
        return GLOBAL_LOG_LEVEL;
    }
}
