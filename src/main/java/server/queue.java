package server;

import java.util.LinkedList;

/**
 * This class was taken from the Estructuras de Datos Lineales presentation.
 * @param <T>
 */
public class queue<T> {
    private LinkedList<T> list = new LinkedList<>();
    public void enqueue(T element){
        list.addLast(element);
    }
    public T dequeue(){
        return list.removeFirst();
    }
    public boolean isEmpty(){
        return list.isEmpty();
    }
    public int size(){
        return list.size();
    }
    public T peek(){
        return list.getFirst();
    }
    @Override
    public String toString() {
        return list.toString();
    }
}
