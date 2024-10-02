package io.github.cakilgan.engine.console;

import io.github.cakilgan.cconsole.CakilganConsole;
import io.github.cakilgan.engine.system.CEComponent;

public class CEConsole implements CEComponent {
    Thread consoleThread;
    public void start(){
        consoleThread.start();
    }
    @Override
    public void init() {
        consoleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CakilganConsole.launch(CakilganConsole.class);
            }
        });
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }
}
