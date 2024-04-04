package deque;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        DoubleLinkedList<Integer> list = new DoubleLinkedList<>();

        list.append(6);
        list.append(4);
        list.append(3);
        list.append(1);
        list.append(8);

        System.out.println(list);
        System.out.println(list.get(2));

        list.remove(8);
        System.out.println(list);

        Comparator<Integer> comparator = Comparator.naturalOrder();
        list.sort(comparator);
        System.out.println(list);
    }
}
