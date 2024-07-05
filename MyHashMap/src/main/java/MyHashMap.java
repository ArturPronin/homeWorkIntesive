import java.util.Objects;

public class MyHashMap<K, V> {
    private V[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public MyHashMap() {
        this.array = (V[]) new Object[DEFAULT_CAPACITY];
        this.size = array.length;
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        System.out.println(index);
        if (index >= this.size) throw new IllegalStateException("Array index out of bounds");
        if (value.toString().isEmpty()) {
            array[index] = null;
        } else {
            array[index] = value;
        }
    }

    public V get(K key) {
        int index = getIndex(key);
        if (index >= size) return null;
        return array[index];
    }

    public void remove(K key) {
        int index = getIndex(key);
        if (array[index] != null) {
            array[index] = null;
        }
    }

    // Так как ключ может быть любым типом данных,
    // нам необходимо преобразовать его в int и сделать уникальным
    private int getIndex(K key) {
        int hashCode = hash(key);
        return (this.array.length - 1) & hashCode; // Нормирование из реализации HashMap в Java метода putVal()
    }

    // Автогенерация IDEA с доработкой, в будущих реализациях наверное возьму нативный метод hashCode()
    public int hash(K key) {
        int result = Objects.hash(size + 1);
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyHashMap{");
        for (int i = 0; i < this.size; i++) {
            if (array[i] != null) {
                sb.append(" [").append(array[i]).append("],");
            }
        }
        sb.append("}");
        return sb.toString();
    }


    public static void main(String[] args) {
        Person person = new Person("Павел", 22);

        MyHashMap hashMap = new MyHashMap();
        hashMap.put(2, "Hello, world!");
        hashMap.put(1, "");
        hashMap.put(3, "Hello, Artur!");
        hashMap.put("5", "Hello, Artur!");
        hashMap.put(6, "Hello, Artur!");
        hashMap.put("7", "Hello, World2!");
        hashMap.put(2, "Hello, wor");
        hashMap.put("павел", person);

        System.out.println(hashMap.get(1));
        System.out.println(hashMap.get(2));
        System.out.println(hashMap.get(3));
        System.out.println(hashMap.get("павел"));
        hashMap.remove(3);

        System.out.println(hashMap);
    }

}

// Для теста
class Person {
    private int age;
    private String name;

    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
