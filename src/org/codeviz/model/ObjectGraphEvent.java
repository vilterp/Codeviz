package org.codeviz.model;

public class ObjectGraphEvent extends PythonEvent {
    
    public static final int OBJECT_CREATED = 1;
    public static final int OBJECT_CHANGED = 2;
    public static final int OBJECT_DELETED = 3;
    
    private int type;
    private long objId;
    private MutableObjectRecord newObj;
    private MutableObjectEvent event;
    
    // changed;
    public ObjectGraphEvent(long step, SourceLine line, long objId, MutableObjectEvent event) {
        super(step, line);
        this.type = OBJECT_CHANGED;
        this.objId = objId;
        this.event = event;
        this.newObj = null;
    }
    
    // created
    public ObjectGraphEvent(long step, SourceLine line, MutableObjectRecord newObj) {
        super(step, line);
        this.type = OBJECT_CREATED;
        this.objId = newObj.getId();
        this.newObj = newObj;
        this.event = null;
    }
    
    // deleted
    public ObjectGraphEvent(long step, SourceLine line, long objId) {
        super(step, line);
        this.type = OBJECT_DELETED;
        this.objId = objId;
        this.event = null;
        this.newObj = null;
    }
    
    public int getType() {
        return type;
    }
    
    public long getObjId() {
        return objId;
    }
    
    public MutableObjectRecord getNewObj() {
        return newObj;
    }
    
    public MutableObjectEvent getEvent() {
        return event;
    }
    
}
