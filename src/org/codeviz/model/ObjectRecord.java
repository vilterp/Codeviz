package org.codeviz.model;

public abstract class ObjectRecord {
    
    private Object type;
    
    protected ObjectRecord(Object type) {
        this.type = type;
    }
    
    public Object getType() {
        return type;
    }
    
}
