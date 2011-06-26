package org.codeviz.model;

public abstract class IOEvent extends PythonEvent {
    
    private IOStream stream;
    private String contents;
    
    protected IOEvent(long step, SourceLine line, IOStream stream, String contents) {
        super(step, line);
        this.stream = stream;
        this.contents = contents;
    }
    
    public IOStream getStream() {
        return stream;
    }
    
    public String getContents() {
        return contents;
    }
    
}
