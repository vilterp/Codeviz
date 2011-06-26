package org.codeviz.model;

import java.util.List;

public class LineStep extends StepEvent {

    private List<TimeDictChange<Long, String, ObjectRecord>> localVarChanges;

    public LineStep(long step, SourceLine line,
                    List<TimeDictChange<Long, String, ObjectRecord>> localVarChanges) {
        super(step, line);
        this.localVarChanges = localVarChanges;
    }
    
    public String toString() {
        return String.format("LineStep{step=%d, line=%s}", getStep(), getLine());
    }
    
    public List<TimeDictChange<Long, String, ObjectRecord>> getLocalVarChanges() {
        return localVarChanges;
    }

}
