package org.codeviz.model;

import org.python.core.*;

import javax.swing.SwingUtilities;

import java.util.List;
import java.util.ArrayList;

public class ObjectGraphModel {
    
    private TimeDict<Long, Long, MutableObjectRecord> objects;
    
    private List<ObjectGraphListener> listenerList;
    
    public ObjectGraphModel() {
        objects = new TimeDict<Long, Long, MutableObjectRecord>(0L);
        listenerList = new ArrayList<ObjectGraphListener>();
    }

    public ObjectRecord getRecord(long step, SourceLine line, Object obj) {
        if(obj instanceof Integer) {
            return new ImmutableObject(Integer.TYPE, obj);
        } else if(obj instanceof Long) {
            return new ImmutableObject(Long.TYPE, obj);
        } else if(obj instanceof Float) {
            return new ImmutableObject(Float.TYPE, obj);
        } else if(obj instanceof String) {
            return new ImmutableObject(String.class, obj);
        } else if(obj instanceof PyInteger) {
            return new ImmutableObject(Integer.TYPE, ((PyInteger) obj).getValue());
        } else if(obj instanceof PyObject) {
            return getPyObjectRecord(step, line, (PyObject) obj);
        } else {
            return new JavaObject(obj.getClass(), obj);
        }
    }
    
    private ObjectRecord getPyObjectRecord(long step, SourceLine line, PyObject obj) {
        long id = Py.id(obj);
        MutableObjectRecord record = objects.get(id);
        if(record == null) {
            record = new GenericPythonObjectRecord(id, obj.getType(), step);
            objects.put(step, id, record);
            fireCreated(new ObjectGraphEvent(step, line, record));
        }
        return record;
    }
    
    private void fireCreated(final ObjectGraphEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(ObjectGraphListener l: listenerList) {
                    l.objectCreated(e);
                }
            }
        });
    }
    
    private void fireChanged(final ObjectGraphEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(ObjectGraphListener l: listenerList) {
                    l.objectChanged(e);
                }
            }
        });
    }
    
    private void fireDeleted(final ObjectGraphEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(ObjectGraphListener l: listenerList) {
                    l.objectDeleted(e);
                }
            }
        });
    }
    
    public void addObjectGraphListener(ObjectGraphListener l) {
        listenerList.add(l);
    }
    
    public void removeObjectGraphListener(ObjectGraphListener l) {
        listenerList.remove(l);
    }
    
}
