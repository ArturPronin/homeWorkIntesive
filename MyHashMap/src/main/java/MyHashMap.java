import java.lang.reflect.Array;

public class MyHashMap<K, V> {
    private Node<K, V>[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        @Override
        public String toString() {
            return "Node{ " + key + "=" + value + " }";
        }
    }

    public MyHashMap() {
        this.array = (Node<K, V>[]) Array.newInstance(Node.class, DEFAULT_CAPACITY);
        this.size = 0;
    }

    public void put(K key, V value) {
        ifIsKeyNull(key);
        if (checkResize()) resize();
        int index = getIndex(key);
        if (index >= array.length) throw new IllegalStateException("Array index out of bounds");
        Node<K, V> newNode = new Node<>(key, value, null);

        if (array[index] == null) {
            array[index] = newNode;
        } else {
            Node<K, V> currentNode = array[index];
            Node<K, V> prevNode = null;
            while (currentNode != null && !currentNode.key.equals(key)) {
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            if (currentNode != null && currentNode.key.equals(key)) {
                currentNode.value = value;
            } else {
                prevNode.next = newNode;
            }
        }
        size++;
    }

    public V get(Object key) {
        ifIsKeyNull(key);
        int index = getIndex(key);
        Node<K, V> currentNode = array[index];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public void remove(Object key) {
        ifIsKeyNull(key);
        int index = getIndex(key);
        Node<K, V> currentNode = array[index];
        Node<K, V> prevNode = null;
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                if (prevNode == null) {
                    array[index] = currentNode.next;
                } else {
                    prevNode.next = currentNode.next;
                }
                size--;
                break;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
    }

    public void ifIsKeyNull(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    int getIndex(Object key) {
        if (key.equals(null)) {
            throw new NullPointerException();
        }
        int hashCode = key.hashCode();
        return (this.array.length - 1) & hashCode;
    }

    private boolean checkResize() {
        return size >= array.length * LOAD_FACTOR;
    }

    private void resize() {
        Node<K, V>[] newArray = (Node<K, V>[]) Array.newInstance(Node.class, array.length * 2);
        for (Node<K, V> node : array) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);
                node.next = newArray[index];
                newArray[index] = node;
                node = next;
            }
        }
        array = newArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyHashMap{");
        for (Node<K, V> node : array) {
            if (node != null) {
                Node<K, V> currentNode = node;
                while (currentNode != null) {
                    sb.append(" [").append(currentNode).append("],");
                    currentNode = currentNode.next;
                }
            }
        }
        if (sb.length() > 10) sb.setLength(sb.length() - 1);
        sb.append(" }");
        return sb.toString();
    }

}