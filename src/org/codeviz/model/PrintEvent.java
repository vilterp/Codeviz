package org.codeviz.model;

import org.python.core.PyString;

public class PrintEvent extends IOEvent {
    
    public PrintEvent(long step, SourceLine line, IOStream stream, String contents) {
        super(step, line, stream, contents);
    }
    
    public String toString() {
        return String.format("Print[%s to %s from %s]",
                    new PyString(getContents()).__repr__(), getStream(), getLine());
    }
    
}
