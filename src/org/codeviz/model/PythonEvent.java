package org.codeviz.model;

public abstract class PythonEvent {
    
    private long step;
    private SourceLine line;
    
    protected PythonEvent(long step, SourceLine line) {
        this.step = step;
        this.line = line;
    }
    
    public SourceLine getLine() {
        return line;
    }
    
    public long getStep() {
        return step;
    }
    
}
