

/**
 * Класс MyHashMap представляет собой кастомную реализацию универсальной хэш-мапы с использованием метода цепочек для разрешения коллизий.
 * MyHashMap поддерживает основные методы put(), get() и remove() для работы с элементами.
 * @param <K> Тип ключей.
 * @param <V> Тип значений.
 */
public class MyHashMap<K, V> {
    private Node[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;


    /**
     * Вложенный класс Node представляет собой узел, который хранит ключ, значение и ссылку на следующий узел в цепочке.
     * @param <K> Тип ключей.
     * @param <V> Тип значений.
     */
    static class Node<K, V> {
        private final K key;
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

    /**
     * Конструктор по умолчанию, инициализирующий хэш-мап с DEFAULT_CAPACITY по умолчанию.
     */
    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Основной метод добавления элементов в хэш-мап.
     * Добавляет, указанные в параметрах метода, ключ и значение в хэш-мап.
     * 1. Если ключ уже существует в таблице, то его значение перезаписывается на новое.
     * 2. Если таблица достигла порогового значения загрузки, то происходит увеличение размера таблицы.
     * @param key Ключ, по которому храниться значение (ключ не может быть null или пустым).
     * @param value Значение, которое будет сохранено с ключом (значение не может быть пустым, но можно положить null).
     * @throws IllegalStateException Выбрасывается исключение, если рассчитанный индекс выходит за пределы массива.
     * @throws NullPointerException Выбрасывается исключение, если ключ равен null.
     * @throws IllegalArgumentException Выбрасывается исключение, если ключ или значение пустые.
     */
    public void put(K key, V value) {
        ifIsValueEmpty(value);
        ifIsKeyNull(key);
        ifIsKeyEmpty(key);
        if (checkResize()) resize();
        int index = getIndex(key);
        if (index >= table.length) throw new IllegalStateException("Array index out of bounds");
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
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

    /**
     * Основной метод получения значения по ключу, указанному в параметрах метода.
     * Возвращает значение, связанное с указанным ключом.
     * Если ключ не найден, возвращает null.
     * @param key Ключ для поиска значения.
     * @return Значение, связанное с ключом, или null, если ключ не найден.
     * @throws NullPointerException  Выбрасывается исключение, если ключ равен null.
     * @throws IllegalArgumentException Выбрасывается исключение, если ключ пустой.
     */
    public V get(Object key) {
        ifIsKeyNull(key);
        ifIsKeyEmpty(key);
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }


    /**
     * Основной метод удаления значения по ключу, указанному в параметрах метода.
     * Удаляет элемент с указанным ключом из хэш-мапы.
     * Если ключ не найден, метод ничего не делает.
     * @param key Ключ элемента для удаления.
     * @throws NullPointerException  Выбрасывается исключение, если ключ равен null.
     * @throws IllegalArgumentException Выбрасывается исключение, если ключ пустой.
     */
    public void remove(Object key) {
        ifIsKeyNull(key);
        ifIsKeyEmpty(key);
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        Node<K, V> prevNode = null;
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                if (prevNode == null) {
                    table[index] = currentNode.next;
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

    /**
     * Проверяет, является ли ключ равным null, и если это так, выбрасывает исключение IllegalArgumentException.
     */
    public void ifIsKeyNull(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
    }

    /**
     * Проверяет, является ли ключ пустым, и если это так, выбрасывает исключение IllegalArgumentException.
     */
    public void ifIsKeyEmpty(Object key) {
        if (key == "") {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }

    /**
     * Проверяет, является ли значение пустым, и если это так, выбрасывает исключение IllegalArgumentException.
     */
    public void ifIsValueEmpty(Object value) {
        if (value == "") {
            throw new IllegalArgumentException("Value cannot be empty");
        }
    }

    /**
     * Метода для получения индекса из заданного ключа.
     * У указанного ключа рассчитывается hashCode и рассчитывается индекс.
     * Возвращает индекс в таблице для указанного ключа.
     * @param key Ключ для расчета индекса.
     * @return Индекс в таблице для указанного ключа.
     * @throws NullPointerException Выбрасывается исключение, если ключ равен null.
     */
    int getIndex(Object key) {
        if (key.equals(null)) {
            throw new NullPointerException();
        }
        int hashCode = key.hashCode();
        return Math.abs(this.table.length - 1) & hashCode;
    }

    /**
     * Проверяет, необходимо ли увеличить размер таблицы.
     * @return true, если количество элементов в хэш мапе больше или равно "длине массива * Load Factor", иначе false.
     */
    private boolean checkResize() {
        return size >= table.length * LOAD_FACTOR;
    }

    /**
     * Возвращает количество элементов в хэш-мапе.
     */
    public int size() {
        return size;
    }

    /**
     * Увеличивает размер таблицы и перераспределяет все элементы.
     */
    private void resize() {
        Node<K, V>[] newArray = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);
                node.next = newArray[index];
                newArray[index] = node;
                node = next;
            }
        }
        table = newArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyHashMap{");
        for (Node<K, V> node : table) {
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