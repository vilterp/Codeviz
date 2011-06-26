package org.codeviz.model;

public class MutableObjectEvent<K> extends PythonEvent {
    
    public static final int ATTR_UPDATED = 1;
    public static final int ATTR_DELETED = 2;
    public static final int OBJECT_DELETED = 3;
    
    private int type;
    private K key;
    private ObjectRecord newVal;
    
    // attr updated
    public MutableObjectEvent(long step, SourceLine line, K key, ObjectRecord newVal) {
        super(step, line);
        this.type = ATTR_UPDATED;
        this.key = key;
        this.newVal = newVal;
    }
    
    // attr deleted
    public MutableObjectEvent(long step, SourceLine line, K key) {
        super(step, line);
        this.type = ATTR_DELETED;
        this.key = key;
        this.newVal = null;
    }
    
    // object deleted
    public MutableObjectEvent(long step, SourceLine line) {
        super(step, line);
        this.type = OBJECT_DELETED;
        this.key = null;
        this.newVal = null;
    }
    
    public int getType() {
        return type;
    }
    
    public K getKey() {
        return key;
    }
    
    public ObjectRecord getNewVal() {
        return newVal;
    }
    
}
