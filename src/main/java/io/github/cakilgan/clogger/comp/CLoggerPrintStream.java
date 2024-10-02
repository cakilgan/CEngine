package io.github.cakilgan.clogger.comp;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.clogger.format.CLoggerFormatter;
import io.github.cakilgan.clogger.format.ElementWithType;
import io.github.cakilgan.clogger.util.Text;
import org.lwjgl.opencl.CL;

import java.io.PrintStream;
import java.util.Locale;

public class CLoggerPrintStream extends PrintStream {
    public CLoggerPrintStream(PrintStream out) {
        super(out);
    }

    public void println(String x, CLoggerFormatter formatter){
        String logStr = formatter.parse(x);
        CLogger.getGlobalLogHistory().add(Text.removeColorCodes(logStr));
        super.println(logStr);
    }
    @Override
    public void println(String x) {
        ((ElementWithType<String>)CLoggerFormatter.getSystemOutFormatter().getElements().get("class")).setType(getCallingClassName());
        super.println(CLoggerFormatter.getSystemOutFormatter().parse(x));
    }

    private String getCallingClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length > 3) {
            return stackTrace[3].getClassName();
        } else {
            return "UnknownClass";
        }
    }
}
