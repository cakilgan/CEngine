package io.github.cakilgan.engine.events.comp;

import io.github.cakilgan.engine.events.Event;
import io.github.cakilgan.engine.events.EventResult;
import io.github.cakilgan.engine.input.CEKeyCallback;
import io.github.cakilgan.engine.input.CEMouseCallback;

public class KeyboardEvent extends Event<CEKeyCallback> {
    public EventResult call(CEKeyCallback context){
        return EventResult.PASS;
    }
}
