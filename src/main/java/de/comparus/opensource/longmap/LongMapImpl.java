package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
public class LongMapImpl<V> implements LongMap<V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_THRESHOLD = 0.75f;

    private Node<Long, V>[] hashTable;
    private int size = 0;
    private float threshold;
    private Class<? extends V> genericType;

    public LongMapImpl() {
        hashTable = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_THRESHOLD;
    }

    @Override
    public V put(long key, V value) {

        if (size + 1 >= threshold) {
            threshold *= 2;
            arrayDoubling();
        }
        if (null == genericType) {
            genericType = (Class<? extends V>) value.getClass();
        }

        Node<Long, V> newNode = new Node<>(key, value);
        int index = getBucket(key);

        if (null == hashTable[index]) {
            return simplePut(index, newNode);
        }
        List<Node<Long, V>> nodeList = hashTable[index].getBucket();

        for (Node<Long, V> node : nodeList) {
            if (isKeyExistsButValueNew(node, newNode)
                    || isCollisionProcessed(node, newNode, nodeList)) {
                return newNode.getValue();
            }
        }
        return null;
    }

    @Override
    public V get(long key) {
        if (size == 0) {
            return null;
        }

        int index = getBucket(key);

        if (index >= hashTable.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (null != hashTable[index]) {

            if (hashTable[index].getBucket().size() == 1) {
                return hashTable[index].getBucket().get(0).getValue();
            }
            List<Node<Long, V>> nodes = hashTable[index].getBucket();

            return nodes.stream()
                    .filter(node -> key == node.getKey())
                    .map(Node::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public V remove(long key) {
        if (size == 0) {
            return null;
        }

        int index = getBucket(key);

        if (null == hashTable[index]) {
            return null;
        }

        if (hashTable[index].getBucket().size() == 1) {
            V value = hashTable[index].getBucket().get(0).getValue();
            hashTable[index] = null;
            size--;
            return value;
        }
        List<Node<Long, V>> nodes = hashTable[index].getBucket();

        for (Node<Long, V> node : nodes) {
            if (key == node.getKey()) {
                nodes.remove(node);
                size--;
                return node.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(long key) {
        return null != get(key);
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) {
            return false;
        }
        return Arrays.stream(hashTable)
                .filter(Objects::nonNull)
                .flatMap(nodes -> nodes.getBucket().stream())
                .anyMatch(node -> value == node.getValue());
    }

    @Override
    public long[] keys() {
        if (size == 0) {
            return new long[0];
        }
        long[] keys = new long[size];
        int i = 0;

        for (Node<Long, V> nodes : hashTable) {
            if (null != nodes) {
                List<Node<Long, V>> bucket = nodes.getBucket();

                for (Node<Long, V> node : bucket) {
                    if (null != node) {
                        keys[i++] = node.getKey();
                    }
                }
            }
        }
        return keys;
    }

    @Override
    public V[] values() {
        if (size == 0) {
            return null;
        }

        List<V> values = Arrays.stream(hashTable)
                .filter(Objects::nonNull)
                .flatMap(basket -> basket.getBucket().stream())
                .map(Node::getValue)
                .collect(Collectors.toList());

        return values.toArray((V[]) Array.newInstance(genericType, values.size()));
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void clear() {
        hashTable = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    private int getBucket(final Long key) {
        int hash = 31 * 17 + Math.abs(key.hashCode());
        return hash % hashTable.length;
    }

    private void arrayDoubling() {
        Node<Long, V>[] oldNodes = hashTable;
        hashTable = new Node[oldNodes.length * 2];
        size = 0;

        for (Node<Long, V> node : oldNodes) {
            if (null != node) {
                for (Node<Long, V> kvNode : node.getBucket()) {
                    put(kvNode.getKey(), kvNode.getValue());
                }
            }
        }
    }

    private V simplePut(int index, Node<Long, V> newNode) {
        hashTable[index] = new Node<>(newNode.getKey(), newNode.getValue());
        hashTable[index].getBucket().add(newNode);
        size++;
        return newNode.getValue();
    }

    private boolean isKeyExistsButValueNew(final Node<Long, V> existsNode, final Node<Long, V> newNode) {
        V value = newNode.getValue();

        if (newNode.getKey().equals(existsNode.getKey())
                && !value.equals(existsNode.getValue())) {

            existsNode.setValue(value);
            return true;
        }
        return false;
    }

    private boolean isCollisionProcessed(final Node<Long, V> existsNode,
                                         final Node<Long, V> newNode,
                                         final List<Node<Long, V>> nodes) {

        if (getBucket(existsNode.getKey()) == getBucket(newNode.getKey())
                && !Objects.equals(newNode.getKey(), existsNode.getKey())) {

            nodes.add(newNode);
            size++;
            return true;
        }
        return false;
    }
}
