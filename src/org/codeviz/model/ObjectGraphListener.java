package org.codeviz.model;

import java.util.EventListener;

public interface ObjectGraphListener extends EventListener {
    
    void objectCreated(ObjectGraphEvent e);
    
    void objectChanged(ObjectGraphEvent e);
    
    void objectDeleted(ObjectGraphEvent e);
    
}
