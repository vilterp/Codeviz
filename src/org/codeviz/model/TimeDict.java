package org.codeviz.model;

import java.util.*;

/**
 * Each entry is mapped to a list of changes, each of which has a time:
 * created, set to a new value, destroyed. In this way, a TimeDict keeps track
 * of how all its attributes change over time.
 * @param <T>
 * @param <K>
 * @param <V>
 */
// TODO: are are these "synchronized"'s necessary? Are they a significant performance penalty?
public class TimeDict<T, K, V> {

    private Map<K, List<TimeDictChange<T, K, V>>> changes;
    private List<TimeDictChange<T, K, V>> record;
    private T created;
    private T destroyed;

    public TimeDict(Map<K, V> initState, T initTime) {
        created = initTime;
        changes = new HashMap<K, List<TimeDictChange<T, K, V>>>();
        record = new ArrayList<TimeDictChange<T, K, V>>();
        if(initState != null) {
            for(K key: initState.keySet()) {
                put(initTime, key, initState.get(key));
            }
        }
        destroyed = null;
    }

    public TimeDict(T initTime) {
        this(null, initTime);
    }

    public String toString() {
        return String.format("TimeDict[%d changes; %s]", record.size(), getLatestState());
    }

    public synchronized boolean isDestroyed() {
        return destroyed != null;
    }

    public synchronized T getCreated() {
        return created;
    }

    public synchronized T getDestroyed() {
        return destroyed;
    }

    public synchronized List<TimeDictChange<T, K, V>> getRecord() {
        return record;
    }
    
    public synchronized V get(K key) {
        List<TimeDictChange<T, K, V>> keyRecord = changes.get(key);
        if(keyRecord == null) {
            return null;
        } else {
            TimeDictChange<T, K, V> lastChange = keyRecord.get(keyRecord.size()-1);
            if(lastChange instanceof RemoveEntry) {
                return null;
            } else if(lastChange instanceof UpdateEntry) {
                return ((UpdateEntry<T, K, V>) lastChange).getNewVal();
            }
        }
        return null; // needed to make the compiler happy
    }

    public synchronized TimeDictChange<T, K, V> put(T time, K key, V val) {
        List<TimeDictChange<T, K, V>> keyRecord = changes.get(key);
        TimeDictChange<T, K, V> change = new UpdateEntry<T, K, V>(time, key, val);;
        if(keyRecord == null) {
            List<TimeDictChange<T, K, V>> newKeyRecord = new ArrayList<TimeDictChange<T, K, V>>();
            newKeyRecord.add(change);
            changes.put(key, newKeyRecord);
        } else {
            change = new UpdateEntry<T, K, V>(time, key, val);
            keyRecord.add(change);
        }
        record.add(change);
        return change;
    }

    public synchronized TimeDictChange<T, K, V> updateIfDifferent(T time, K key, V newVal) {
        V val = get(key);
        if(val == null)
            return put(time, key, newVal);
        else {
            if(val.equals(newVal)) // no update needed
                return null;
            else // update
                return put(time, key, val);
        }
    }
    
    public synchronized TimeDictChange<T, K, V> remove(T time, K key) {
        List<TimeDictChange<T, K, V>> changeList = changes.get(key);
        if(changeList == null)
            return null;
        TimeDictChange<T, K, V> change = new RemoveEntry<T, K, V>(time, key);
        record.add(change);
        changeList.add(change);
        return change;
    }
    
    public synchronized boolean needsUpdate(K key, V newVal) {
        V val = get(key);
        if(val == null)
            return true;
        else {
            return !val.equals(newVal);
        }
    }

    public synchronized boolean containsKey(K key) {
        return get(key) != null;
    }

    public synchronized Set<K> keySet() {
        HashSet<K> set = new HashSet<K>();
        for(K key: changes.keySet())
            if(containsKey(key))
                set.add(key);
        return set;
    }

    public synchronized int size() {
        return keySet().size();
    }

    public synchronized Map<K, V> getLatestState() {
        Map<K, V> st = new HashMap<K, V>();
        for(K key: changes.keySet()) {
            V val = get(key);
            if(val != null)
                st.put(key, val);
        }
        return st;
    }

}
