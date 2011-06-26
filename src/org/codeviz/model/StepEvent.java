package org.codeviz.model;

public abstract class StepEvent extends PythonEvent {

    protected StepEvent(long step, SourceLine line) {
        super(step, line);
    }

}
