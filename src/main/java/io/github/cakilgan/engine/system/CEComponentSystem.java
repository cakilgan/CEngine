package io.github.cakilgan.engine.system;

import io.github.cakilgan.clogger.util.Text;
import io.github.cakilgan.engine.logger.CommonContexts;
import io.github.cakilgan.engine.logger.EngineContexts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CEComponentSystem <T extends CEComponent>implements CEComponent, RunOnLastLineOfLoop,RunOnFirstLineOfLoop,RunOnLastLineOfInit,RunOnFirstLineOfInit,RunOnFirstLineOfDispose,RunOnLastLineOfDispose,RunOnLastLineOfWhileLoop {
    List<CEComponent> COMPONENTS= new ArrayList<>();
    T PARENT;
    public CEComponentSystem(T PARENT){
        this.PARENT = PARENT;
    }
    public <T extends CEComponent>T addComponent( T comp){
        COMPONENTS.add(comp);
        return comp;
    }
    public <T extends CEComponent>T getComponent(Class<T> comp){
        final CEComponent[] component_ = new CEComponent[1];
        COMPONENTS.forEach(new Consumer<CEComponent>() {
            @Override
            public void accept(CEComponent component) {
                if (component.getClass().equals(comp)){
                  component_[0] = component;
                }
            }
        });
        return (T) component_[0];
    }
    public T getPARENT() {
        return PARENT;
    }

    public void setPARENT(T PARENT) {
        this.PARENT = PARENT;
    }

    @Override
    public void init() {
        for (int i = 0; i < COMPONENTS.size(); i++) {
            CEComponent ceComponent = COMPONENTS.get(i);
            if (ceComponent!=null){
                if (getPARENT() instanceof HasLogger){
                    ((HasLogger) getPARENT()).getLogger().setCurrentContext(CommonContexts.COMPONENT_SYSTEM);
                    ((HasLogger) getPARENT()).getLogger().lifecycle(Text.surroundWithRectalBracket(getPARENT().getClass().getSimpleName())+"--:"+ceComponent.getClass().getSimpleName()+"; "+Text.surroundWithBracket(i+""));
                    ((HasLogger) getPARENT()).getLogger().setCurrentContext(EngineContexts.INIT);
                }
            }
        }
        for (int i = 0; i < COMPONENTS.size(); i++) {
            CEComponent ceComponent = COMPONENTS.get(i);
            ceComponent.init();
        }
    }
    @Override
    public void loop() {
        COMPONENTS.forEach(component -> component.loop());
    }
    @Override
    public void dispose() {
        COMPONENTS.forEach(component -> component.dispose());
    }
    @Override
    public void runFirstInit() {COMPONENTS.forEach(component -> {if (component instanceof RunOnFirstLineOfInit) {((RunOnFirstLineOfInit) component).runFirstInit();}});}
    @Override
    public void runFirstLoop() {COMPONENTS.forEach(component -> {if (component instanceof RunOnFirstLineOfLoop) {((RunOnFirstLineOfLoop) component).runFirstLoop();}});}
    @Override
    public void runFirstDispose() {COMPONENTS.forEach(ceComponent -> {if (ceComponent instanceof RunOnFirstLineOfDispose) {((RunOnFirstLineOfDispose) ceComponent).runFirstDispose();}});}
    @Override
    public void runLastInit() {COMPONENTS.forEach(ceComponent -> {if (ceComponent instanceof RunOnLastLineOfInit) {((RunOnLastLineOfInit) ceComponent).runLastInit();}});}
    @Override
    public void runLastLoop() {COMPONENTS.forEach(component -> {if (component instanceof RunOnLastLineOfLoop) {((RunOnLastLineOfLoop) component).runLastLoop();}});}
    @Override
    public void runLastDispose() {COMPONENTS.forEach(ceComponent -> {if (ceComponent instanceof RunOnLastLineOfDispose) {((RunOnLastLineOfDispose) ceComponent).runLastDispose();}});}
    @Override
    public void runLastLineOfWhileLoop() {COMPONENTS.forEach(ceComponent -> {if (ceComponent instanceof RunOnLastLineOfWhileLoop) ((RunOnLastLineOfWhileLoop) ceComponent).runLastLineOfWhileLoop();});}
}
