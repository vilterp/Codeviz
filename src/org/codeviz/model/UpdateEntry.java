package org.codeviz.model;

public class UpdateEntry<T, K, V> extends TimeDictChange<T, K, V> {

    private T time;
    private K key;
    private V newVal;

    public UpdateEntry(T time, K key, V newVal) {
        this.time = time;
        this.key = key;
        this.newVal = newVal;
    }

    @Override
    public String toString() {
        return "UpdateEntry{" +
                "time=" + time +
                ", key=" + key +
                ", newVal=" + newVal +
                '}';
    }

    public T getTime() {
        return time;
    }

    public K getKey() {
        return key;
    }

    public V getNewVal() {
        return newVal;
    }

}
