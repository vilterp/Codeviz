package org.codeviz.model;

import org.codeviz.model.SourceLine;

public class ExceptionStep extends StepEvent {

    private Object excType;
    private Object excVal;

    public ExceptionStep(long step, SourceLine line, Object excType, Object excVal) {
        super(step, line);
        this.excType = excType;
        this.excVal = excVal;
    }

    public Object getExcType() {
        return excType;
    }

    public Object getExcVal() {
        return excVal;
    }

}
