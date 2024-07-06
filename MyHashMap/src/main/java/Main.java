public class Main {
    public static void main(String[] args) {

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

        System.out.println(myHashMap.get(1));
        System.out.println(myHashMap.get(2));
        System.out.println(myHashMap.get(3));
        System.out.println(myHashMap.get("павел"));
        System.out.println(myHashMap.get("5"));
        System.out.println(myHashMap.get(123253));


        System.out.println(myHashMap);

    }


}
