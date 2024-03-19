package deque;

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
        if (value == null){
            throw new DoubleLinkedQueueException("ERROR: no se puede añadir un elemento nulo");
        }

        if (first == null){ // Si no hay elementos
            LinkedNode<T> nuevo = new LinkedNode<>(value, null, null);
            first = nuevo;
            last = nuevo;
        }else if (first.isLastNode()){ // Si solo hay un elemento
            LinkedNode<T> nuevo = new LinkedNode<>(value, null, first);
            first.setPrevious(nuevo);
            last = first;
            first = nuevo;
        }else { // Si hay mas de un elemento
            LinkedNode<T> nuevo = new LinkedNode<>(value, null, first);
            first.setPrevious(nuevo);
            first = nuevo;
        }
        size++;
    }

    @Override
    public void append(T value) {
        if (value == null){
            throw new DoubleLinkedQueueException("ERROR: no se puede añadir un elemento nulo");
        }

        if (first == null){ // Si no hay elementos
            LinkedNode<T> nuevo = new LinkedNode<>(value, null, null);
            first = nuevo;
            last = nuevo;
        }else if (first.isLastNode()){ // Si solo hay un elemento
            LinkedNode<T> nuevo = new LinkedNode<>(value, last, null);
            last.setNext(nuevo);
            last = nuevo;
        }else { // Si hay mas de un elemento
            LinkedNode<T> nuevo = new LinkedNode<>(value, last, null);
            last.setNext(nuevo);
            last = nuevo;
        }
        size++;
    }

    @Override
    public void deleteFirst() {
        if (first == null){
            throw new DoubleLinkedQueueException("ERROR: no se puede eliminar un elemento de una lista vacia");
        }else if (first.isLastNode()){
            first = null;
            last = null;
            size--;
        }else {
            LinkedNode<T> next = first.getNext();
            next.setPrevious(null);
            first = next;
            size--;
        }
    }

    @Override
    public void deleteLast() {
        if (first == null){
            throw new DoubleLinkedQueueException("ERROR: no se puede eliminar un elemento de una lista vacia");
        }else if (first.isLastNode()){
            first = null;
            last = null;
            size--;
        }else {
            LinkedNode<T> previous = last.getPrevious();
            previous.setNext(null);
            last = previous;
            size--;
        }
    }

    @Override
    public T first() {
        if (first == null){
            throw new DoubleLinkedQueueException("ERROR: la lista esta vacia, no se puede acceder al primer elemento");
        }else {
            return first.getItem();
        }
    }

    @Override
    public T last() {
        if (first == null){
            throw new DoubleLinkedQueueException("ERROR: la lista esta vacia, no se puede acceder al ultimo elemento");
        }else {
            return last.getItem();
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    /*
    @Override
    public String toString(){
        LinkedNode<T> elem = first;
        int n = size;
        String res ="{ ";
        while (size > 0){
            res +=  " " + elem.getItem();
            if (size != 1){
                elem = elem.getNext();
            }
            size--;
        }
        res += " }";
        return res;
    }

     */
}
