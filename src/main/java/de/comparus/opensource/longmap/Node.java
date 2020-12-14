package de.comparus.opensource.longmap;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * List<Node<K, V>> bucket created to store objects in the case of a collision
 */

@Data
public class Node<K, V> {
    private List<Node<K, V>> bucket;
    private K key;
    private V value;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        bucket = new LinkedList<>();
    }
}

