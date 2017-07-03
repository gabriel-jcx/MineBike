package org.ngs.bigx.utility;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    public int hashCode() {
        int hashkey = key != null ? key.hashCode() : 0;
        int hashvalue = value != null ? value.hashCode() : 0;

        return (hashkey + hashvalue) * hashvalue + hashkey;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return 
            ((  this.key == otherPair.key ||
                ( this.key != null && otherPair.key != null &&
                  this.key.equals(otherPair.key))) &&
             (  this.value == otherPair.value ||
                ( this.value != null && otherPair.value != null &&
                  this.value.equals(otherPair.value))) );
        }

        return false;
    }

    public String toString()
    { 
           return "(" + key + ", " + value + ")"; 
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}