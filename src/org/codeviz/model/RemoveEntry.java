package org.codeviz.model;

public class RemoveEntry<T, K, V> extends TimeDictChange<T, K, V> {

    private T time;
    private K key;

    public RemoveEntry(T time, K key) {
        this.time = time;
        this.key = key;
    }

    @Override
    public String toString() {
        return "RemoveEntry{" +
                "time=" + time +
                ", key=" + key +
                '}';
    }

    public T getTime() {
        return time;
    }

    public K getKey() {
        return key;
    }

}
