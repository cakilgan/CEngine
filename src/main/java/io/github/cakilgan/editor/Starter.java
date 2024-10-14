package io.github.cakilgan.editor;

import io.github.cakilgan.editor.component.C2DMapEditor;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.game.comp.SnakeGame;

public class Starter {
    public static void main(String[] args) {
        CEngine.WINDOW.getConfig().title = "C2DMap Editor";
        CEngine.RESOURCE_MANAGER.setWriteLogs(true);
        CEngine.setParentPublicComponent(new C2DMapEditor());
        CEngine.ENGINE.run();
    }
}
