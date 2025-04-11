package com.practice.stock_price_alert_system.config;

    import java.util.LinkedList;
import java.util.Iterator;

public class DemoLinkedListConfig {


    class Main1 {
        public static void main(String[] args) {
            LinkedList<String> animals= new LinkedList<>();

            // Add elements in LinkedList
            animals.add("Dog");
            animals.add("Horse");
            animals.add("Cat");

            // Create an object of ListIterator
            ListIterator<String> listIterate = animals.listIterator();
            System.out.print("LinkedList: ");

            while(listIterate.hasNext()) {
                System.out.print(listIterate.next());
                System.out.print(", ");
            }

            // Iterate backward
            System.out.print("\nReverse LinkedList: ");

            while(listIterate.hasPrevious()) {
                System.out.print(listIterate.previous());
                System.out.print(", ");
            }
        }
    }


    class Main2 {
        public static void main(String[] args) {
            LinkedList<String> animals= new LinkedList<>();

            // Add elements in LinkedList
            animals.add("Dog");
            animals.add("Horse");
            animals.add("Cat");

            // Creating an object of Iterator
            Iterator<String> iterate = animals.iterator();
            System.out.print("LinkedList: ");

            while(iterate.hasNext()) {
                System.out.print(iterate.next());
                System.out.print(", ");
            }
        }
    }
}


class Main3 {
    public static void main(String[] args) {
        LinkedList<String> languages = new LinkedList<>();

        // add elements in the LinkedList
        languages.add("Python");
        languages.add("Java");
        languages.add("JavaScript");
        System.out.println("LinkedList: " + languages);

        // get the element from the LinkedList
        String str = languages.get(1);
        System.out.print("Element at index 1: " + str);
    }
}
