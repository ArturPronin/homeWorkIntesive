import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {
    private MyHashMap myHashMap;

    @BeforeEach
    public void setUp() {
        myHashMap = new MyHashMap<>();
    }

    @Test
    public void testPutAndGet() {
        myHashMap.put(2, "Hello, world!");
        myHashMap.put(1, "Empty");
        myHashMap.put(3, "Hello, YYYYYYYYYY!");
        myHashMap.put(2, "Hello, wor");
        myHashMap.put(5, 12345);
        myHashMap.put(6, 12.25);
        myHashMap.put("пробел", ' ');
        myHashMap.put("char", ';');

        assertEquals("Hello, wor", myHashMap.get(2));
        assertEquals("Empty", myHashMap.get(1));
        assertEquals("Hello, YYYYYYYYYY!", myHashMap.get(3));
        assertEquals(12345, myHashMap.get(5));
        assertEquals(12.25, myHashMap.get(6));
        assertEquals(" ", myHashMap.get("пробел"));
        assertEquals(';', myHashMap.get("char"));
    }

    @Test
    public void testPutAndRemove() {
        myHashMap.put(2, "Hello, world!");
        myHashMap.put(3, "Hello, YYYYYYYYYY!");

        assertEquals("Hello, world!", myHashMap.get(2));
        assertEquals("Hello, YYYYYYYYYY!", myHashMap.get(3));

        myHashMap.remove(2);
        assertNull(myHashMap.get(2));
    }

    @Test
    public void testResize() {
        for (int i = 0; i < 32; i++) {
            myHashMap.put(i, "Value " + i);
        }
        assertEquals("Value 0", myHashMap.get(0));
        assertEquals("Value 19", myHashMap.get(19));
        assertEquals("Value 31", myHashMap.get(31));
    }

    @Test
    public void testCustomObject() {
        Person person = new Person("Павел", 22);
        myHashMap.put("павел", person);

        assertEquals(person, myHashMap.get("павел"));

        myHashMap.remove("павел");
        assertNull(myHashMap.get("павел"));
    }


    @Test
    public void testOverwriteValue() {
        myHashMap.put(1, "Initial");
        assertEquals("Initial", myHashMap.get(1));

        myHashMap.put(1, "Updated");
        assertEquals("Updated", myHashMap.get(1));
    }

    @Test
    public void testNullKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            myHashMap.put(null, "nullValue");
        });
        assertEquals("Key cannot be null", exception.getMessage());
    }

    @Test
    public void testNullValue() {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("nullValueKey", null);
        assertNull(map.get("nullValueKey"));

        map.put("nullValueKey", "notNull");
        assertEquals("notNull", map.get("nullValueKey"));
    }

    @Test
    public void testCollision() throws IllegalAccessException, NoSuchFieldException {
        Person person = new Person("Павел", 22);
        MyHashMap myHashMap = new MyHashMap();
        myHashMap.put(2, "Hello, world!");
        myHashMap.put(1, "");
        myHashMap.put(3, "Hello, YYYYYYYYYY!");
        myHashMap.put("5", "Hello, Artur!");
        myHashMap.put(6, "Hello, Artur!");
        myHashMap.put("7", "Hello, World2!");
        myHashMap.put(2, "Hello, wor");
        myHashMap.put("павел", person);
        myHashMap.put(123253, "Hello, wor");
        myHashMap.put("abra ka dabra", 10);
        myHashMap.put("wave my wand", 20);

        Field arrayField = MyHashMap.class.getDeclaredField("array");
        arrayField.setAccessible(true);
        MyHashMap.Node[] array = (MyHashMap.Node[]) arrayField.get(myHashMap);

        System.out.println(myHashMap.get(123253));
        System.out.println(array[myHashMap.getIndex("5")].getNext().getValue());


        assertEquals(myHashMap.get(123253), array[myHashMap.getIndex("5")].getNext().getValue());

    }
}
