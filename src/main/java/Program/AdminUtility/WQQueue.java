package Program.AdminUtility;

import java.util.LinkedList;

public class WQQueue<E> {
    private LinkedList<E> list = new LinkedList<>();

    public int getSize(){
        return list.size();
    }

    public void enqueue(E e){
        list.addLast(e);
    }

    public E dequeue(){
        return list.removeFirst();
    }

    @Override
    public String toString(){
        return "Queue: " + list.toString();
    }
}
