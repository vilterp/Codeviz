package org.codeviz.model;

public class JavaObject extends ObjectRecord {
    
    private Object val;
    
    public JavaObject(Object type, Object val) {
        super(type);
        this.val = val;
    }
    
    public String toString() {
        return String.format("JavaObject[%s]", val);
    }
    
    public Object getVal() {
        return val;
    }
    
}
