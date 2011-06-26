package org.codeviz.model;

import java.util.ArrayList;
import java.util.List;

public class CallNode {

    private long callStep;
    private long returnStep;

    private PythonCallable callable;

    private CallNode parent;
    private List<CallNode> children;
    private List<StepEvent> steps;
    private LocalsRecord localsRecord;
    private ObjectRecord retVal;

    public CallNode(long callStep, PythonCallable callable, LocalsRecord initLocals) {
        this.callStep = callStep;
        this.callable = callable;
        this.localsRecord = initLocals;
        parent = null;
        returnStep = -1;
        children = new ArrayList<CallNode>();
        steps = new ArrayList<StepEvent>();
        retVal = null;
    }

    public String toString() {
        return toString(0);
    }
    
    public String toString(int indent) {
        StringBuffer indentBuf = new StringBuffer();
        for(int i=0; i < indent; i++)
            indentBuf.append('\t');        
        StringBuffer argsRepr = new StringBuffer();
        String[] argNames = callable.getArgNames();
        for(int i=0; i < argNames.length; i++) {
            argsRepr.append(localsRecord.get(argNames[i]));
            if(i < argNames.length -1)
                argsRepr.append(", ");
        }
        String retRepr;
        if(hasReturned())
            retRepr = String.format(" -> %s", retVal);
        else
            retRepr = "";
        return String.format("%sCallNode[%s(%s)%s]", indentBuf, callable.getName(), argsRepr, retRepr);
    }
    
    public String treeRepr(int indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(toString(indent));
        buf.append('\n');
        for(CallNode child: getChildren())
            buf.append(child.treeRepr(indent + 1));
        return buf.toString();
    }
    
    public boolean equals(CallNode other) {
        return callStep == other.getCallStep();
    }

    public CallNode getParent() {
        return parent;
    }

    public void setParent(CallNode parent) {
        this.parent = parent;
    }

    public LocalsRecord getLocalsRecord() {
        return localsRecord;
    }

    public TimeDictChange<Long, String, ObjectRecord>
                    acceptLocalVarChange(Long time, String key, ObjectRecord newVal) {
        return localsRecord.put(time, key, newVal);
    }

    public boolean hasReturned() {
        return returnStep != -1;
    }

    public long getCallStep() {
        return callStep;
    }

    public long getReturnStep() {
        if(hasReturned())
            return returnStep;
        else
            throw new IllegalAccessError("call hasn't returned yet");
    }

    public List<CallNode> getChildren() {
        return children;
    }

    public ObjectRecord getRetVal() {
        if(hasReturned())
            return retVal;
        else
            throw new IllegalAccessError("call hasn't returned yet");
    }

    public void setRetVal(long step, ObjectRecord retVal) {
        returnStep = step;
        this.retVal = retVal;
    }

    public void addChildCall(CallNode newCall) {
        children.add(newCall);
    }

    public void addStep(StepEvent s) {
        steps.add(s);
    }

    public List<StepEvent> getSteps() {
        return steps;
    }

    public PythonCallable getCallable() {
        return callable;
    }

}
