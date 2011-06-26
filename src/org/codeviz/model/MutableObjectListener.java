package org.codeviz.model;

import java.util.EventListener;

public interface MutableObjectListener<K> extends EventListener {
    
    void attrUpdated(MutableObjectEvent<K> e);
    
    void attrDeleted(MutableObjectEvent<K> e);
    
    void objectDeleted(MutableObjectEvent<K> e);
    
}
