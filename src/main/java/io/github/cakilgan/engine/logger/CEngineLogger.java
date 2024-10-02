package io.github.cakilgan.engine.logger;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.*;

import static io.github.cakilgan.engine.logger.EngineContexts.*;

public class CEngineLogger extends CLogger implements CEComponent, RunOnFirstLineOfInit, RunOnLastLineOfInit, RunOnFirstLineOfLoop,RunOnLastLineOfLoop, RunOnFirstLineOfDispose,RunOnLastLineOfDispose,RunOnExit {

    public CEngineLogger(Class<?> aClass) {
        super(CEngine.class);
    }

    @Override
    @Deprecated
    public void init() {

    }

    @Override
    @Deprecated
    public void loop() {

    }

    @Override
    @Deprecated
    public void dispose() {

    }


    @Override
    public void runFirstInit() {
        this.setCurrentContext(INIT);
        this.info("started");
    }
    @Override
    public void runLastInit() {
        this.info("finished");
    }

    @Override
    public void runFirstLoop() {
        this.setCurrentContext(LOOP);
        this.info("started");

    }
    @Override
    public void runLastLoop() {
        this.info("finished");
    }
    @Override
    public void runFirstDispose() {
        this.setCurrentContext(DISPOSE);
        this.info("started");
    }

    @Override
    public void runLastDispose() {
        this.info("finished");
    }

    @Override
    public void runExit() {
        this.setCurrentContext(STOP);
        this.info(" ;");
        this.setCurrentContext(null);
    }
}
