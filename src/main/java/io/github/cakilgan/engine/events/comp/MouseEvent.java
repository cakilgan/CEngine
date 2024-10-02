package io.github.cakilgan.engine.events.comp;

import io.github.cakilgan.engine.events.Event;
import io.github.cakilgan.engine.events.EventResult;
import io.github.cakilgan.engine.input.CEMouseCallback;

public abstract class MouseEvent extends Event<CEMouseCallback> {
    public EventResult call(CEMouseCallback context){
        return EventResult.PASS;
    }
}
