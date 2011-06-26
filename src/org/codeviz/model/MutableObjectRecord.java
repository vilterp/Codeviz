package org.codeviz.model;

import javax.swing.SwingUtilities;

import java.util.List;
import java.util.ArrayList;

public abstract class MutableObjectRecord<K> extends ObjectRecord {
    
    private long id;
    private long createdStep;
    private long deletedStep;
    protected TimeDict<Long, K, ObjectRecord> dict;
    
    private List<MutableObjectListener<K>> listenerList;
    
    protected MutableObjectRecord(long id, Object type, long createdStep) {
        super(type);
        this.id = id;
        this.createdStep = createdStep;
        this.deletedStep = -1;
        dict = new TimeDict<Long, K, ObjectRecord>(createdStep);
        listenerList = new ArrayList<MutableObjectListener<K>>();
    }

    public long getId() {
        return id;
    }
    
    public TimeDict<Long, K, ObjectRecord> getDict() {
        return dict;
    }
    
    public List<TimeDictChange<Long, K, ObjectRecord>> getRecord() {
        return dict.getRecord();
    }
    
    public long getCreatedStep() {
        return createdStep;
    }
    
    public long getDeletedStep() throws IllegalAccessException {
        if(isAlive())
            throw new IllegalAccessException("object still alive");
        return deletedStep;
    }
    
    public boolean isAlive() {
        return deletedStep == -1;
    }

    // only fires a change if the attr has actually changed.
    public MutableObjectEvent updateAttr(long step, SourceLine line, K key, ObjectRecord newVal) {
        TimeDictChange<Long, K, ObjectRecord> change = dict.updateIfDifferent(step, key, newVal);
        if(change != null) {
            MutableObjectEvent<K> moE = new MutableObjectEvent<K>(step, line, key, newVal);
            fireAttrUpdated(moE);
            return moE;
        }
        return null;
    }
    
    public MutableObjectEvent delAttr(long step, SourceLine line, K key) {
        TimeDictChange<Long, K, ObjectRecord> change = dict.remove(step, key);
        if(change != null) {
            MutableObjectEvent<K> moE = new MutableObjectEvent<K>(step, line, key);
            fireAttrDeleted(moE);
            return moE;
        }
        return null;
    }
    
    public void addMutableObjectListener(MutableObjectListener l) {
        listenerList.add(l);
    }
    
    public void removeMutableObjectListener(MutableObjectListener l) {
        listenerList.remove(l);
    }
    
    public void fireAttrUpdated(final MutableObjectEvent<K> evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(MutableObjectListener<K> l: listenerList) {
                    l.attrUpdated(evt);
                }
            }
        });
    }
    
    public void fireAttrDeleted(final MutableObjectEvent<K> evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(MutableObjectListener l: listenerList) {
                    l.attrDeleted(evt);
                }
            }
        });
    }
    
}
