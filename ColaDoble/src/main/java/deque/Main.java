package deque;

public class Main {
    public static void main(String[] args){
        DoubleLinkedList<Integer> cola = new DoubleLinkedList<Integer>();
        cola.prepend(1);
        cola.prepend(2);
        cola.prepend(3);
        cola.append(10);
        cola.append(7);
        cola.deleteLast();
        cola.deleteFirst();
        cola.deleteLast();
        cola.deleteFirst();
        cola.deleteFirst();
        System.out.println(cola.size());
        //System.out.println(cola.toString());
    }
}
