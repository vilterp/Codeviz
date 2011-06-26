package org.codeviz.model;

public class ImmutableObject extends ObjectRecord {
    
    private Object val;
    
    protected ImmutableObject(Object type, Object val) {
        super(type);
        this.val = val;
    }
    
    public Object getVal() {
        return val;
    }
    
    public String toString() {
        return String.format("ImmutableObject{val=%s, type %s}", val, getType());
    }
    
    public boolean equals(Object other) {
        if(other instanceof ImmutableObject) {
            return ((ImmutableObject) other).getVal().equals(this);
        }
        return false;
    }
    
}
