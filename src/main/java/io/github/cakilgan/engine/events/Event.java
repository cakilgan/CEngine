package io.github.cakilgan.engine.events;

public abstract class Event<T> {
    boolean canCall;
    public void setCanCall(boolean canCall) {
        this.canCall = canCall;
    }
    public boolean canCall(){
        return canCall;
    }
}
