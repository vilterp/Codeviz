package org.codeviz.model;

import org.python.core.PyString;

public class InputEvent extends IOEvent {
    
    public InputEvent(long step, SourceLine line, IOStream stream, String contents) {
        super(step, line, stream, contents);
    }
    
    public String toString() {
        return String.format("Input[%s to %s from %s]",
                    new PyString(getContents()).__repr__(), getStream(), getLine());
    }
    
}
