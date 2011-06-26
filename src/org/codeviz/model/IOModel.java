package org.codeviz.model;

import java.util.List;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

public class IOModel implements ListModel {
    
    private List<IOEvent> events;
    
    private List<ListDataListener> listenerList;
    
    public IOModel() {
        events = new ArrayList<IOEvent>();
        listenerList = new ArrayList<ListDataListener>();
    }
    
    public int getSize() {
        return events.size();
    }
    
    public IOEvent getElementAt(int index) {
        return events.get(index);
    }
    
    public void addListDataListener(ListDataListener l) {
        listenerList.add(l);
    }
    
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(l);
    }
    
    public void addEvent(IOEvent e) {
        events.add(e);
        int ind = events.size() - 1;
        fireAdded(new ListDataEvent(IOModel.this, ListDataEvent.INTERVAL_ADDED, ind, ind));
    }
    
    private void fireAdded(final ListDataEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(ListDataListener l: listenerList) {
                    l.intervalAdded(evt);
                }
            }
        });
    }
    
}
