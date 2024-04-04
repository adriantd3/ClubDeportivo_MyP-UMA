// Authors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package deque;

import java.util.Comparator;

public class DoubleLinkedList<T> implements DoubleLinkedQueue<T> {

    private LinkedNode<T> first;
    private LinkedNode<T> last;
    private int size;

    public DoubleLinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    @Override
    public void prepend(T value) {
        if (value == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede añadir un elemento nulo");
        }

        LinkedNode<T> nuevo;
        if (first == null) { // Si no hay elementos
            nuevo = new LinkedNode<>(value, null, null);
            first = nuevo;
            last = nuevo;
        } else { // Si hay uno o más elementos
            nuevo = new LinkedNode<>(value, null, first);
            first.setPrevious(nuevo);
            first = nuevo;
        }
        size++;
    }

    @Override
    public void append(T value) {
        if (value == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede añadir un elemento nulo");
        }

        LinkedNode<T> nuevo;
        if (last == null) { // Si no hay elementos
            nuevo = new LinkedNode<>(value, null, null);
            first = nuevo;
        } else { // Si hay uno o más elementos
            nuevo = new LinkedNode<>(value, last, null);
            last.setNext(nuevo);
        }
        last = nuevo;
        size++;
    }

    @Override
    public void deleteFirst() {
        if (first == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede eliminar un elemento de una lista vacia");
        } else if (first.isLastNode()) {
            first = null;
            last = null;
            size = 0;
        } else {
            LinkedNode<T> next = first.getNext();
            next.setPrevious(null);
            first = next;
            size--;
        }
    }

    @Override
    public void deleteLast() {
        if (last == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede eliminar un elemento de una lista vacia");
        } else if (last.isFirstNode()) {
            first = null;
            last = null;
            size = 0;
        } else {
            LinkedNode<T> previous = last.getPrevious();
            previous.setNext(null);
            last = previous;
            size--;
        }
    }

    @Override
    public T first() {
        if (first == null) {
            throw new DoubleLinkedQueueException("ERROR: la lista esta vacia, no se puede acceder al primer elemento");
        } else {
            return first.getItem();
        }
    }

    @Override
    public T last() {
        if (first == null) {
            throw new DoubleLinkedQueueException("ERROR: la lista esta vacia, no se puede acceder al ultimo elemento");
        } else {
            return last.getItem();
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T get(int index) {
        if (index + 1 > size || index < 0) {
            throw new IndexOutOfBoundsException("ERROR: índice fuera de rango de lista");
        }

        LinkedNode<T> current = first;
        int cont = 0;
        while (cont != index) {
            current = current.getNext();
            cont++;
        }

        return current.getItem();
    }

    @Override
    public boolean contains(T value) {
        if (value == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede buscar un elemento nulo");
        }
        if (this.size == 0) {
            return false;
        }

        boolean found = false;
        LinkedNode<T> current = first;
        while (found == false && current != null) {
            if (current.getItem().equals(value)) {
                found = true;
            }
            current = current.getNext();
        }

        return found;
    }

    @Override
    public void remove(T value) {
        if (value == null) {
            throw new DoubleLinkedQueueException("ERROR: no se puede eliminar un elemento nulo");
        }

        LinkedNode<T> current = first;
        boolean found = false;

        while (!found && current != null) {
            if (current.getItem().equals(value)) {
                removeNode(current);
                found = true;
            } else {
                current = current.getNext();
            }
        }
    }

    private void removeNode(LinkedNode<T> node) {
        if (node.isFirstNode() && node.isLastNode()) {
            first = null;
            last = null;
        } else if (node.isFirstNode()) {
            first = first.getNext();
            first.setPrevious(null);
        } else if (node.isLastNode()) {
            last = last.getPrevious();
            last.setNext(null);
        } else {
            //Is not a terminal node
            LinkedNode<T> prev = node.getPrevious();
            LinkedNode<T> next = node.getNext();
            prev.setNext(next);
            next.setPrevious(prev);
        }

        //Update list size
        size--;
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        //BUBBLE SORT
        if (size < 2) {
            //Lista vacia o de un elemento
            return;
        }

        boolean swapped;
        do {
            swapped = false;
            LinkedNode<T> current = first;

            while (!current.isLastNode()) {
                LinkedNode<T> next = current.getNext();
                if (comparator.compare(current.getItem(), next.getItem()) > 0) {
                    //Si current es mayor que next, se intercambian
                    T aux = current.getItem();
                    current.setItem(next.getItem());
                    next.setItem(aux);

                    swapped = true;
                }

                current = next;
            }

        } while (swapped);
    }

    /*
    @Override
    public String toString() {
        LinkedNode<T> elem = first;
        int n = size;
        String res = "{";
        while (n > 0) {
            res += " " + elem.getItem();
            if (size != 1) {
                elem = elem.getNext();
            }
            n--;
        }
        res += " }";
        return res;
    }
     */

}
