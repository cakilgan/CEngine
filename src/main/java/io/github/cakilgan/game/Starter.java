package io.github.cakilgan.game;

import io.github.cakilgan.clogger.CLogger;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.game.comp.MainGame;

public class Starter {
    public static void main(String[] args) {
        CLogger.setGlobalDebugMode(true);
        CEngine.RESOURCE_MANAGER.setWriteLogs(true);
        CEngine.setParentPublicComponent(new MainGame());
        CEngine.ENGINE.run();
    }
}
