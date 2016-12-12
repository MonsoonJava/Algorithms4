package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head = null;

    private Node tail = null;

    private int size = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (null == item) {
            throw new NullPointerException();
        } else if (size == 0) {
            Node current = new Node(item, null, null);
            head = current;
            tail = current;
            incSize();
        } else {
            Node current = new Node(item, head, null);
            head.setAhead(current);
            head = current;
            incSize();
        }

    }

    // add the item to the end
    public void addLast(Item item) {
        if (null == item) {
            throw new NullPointerException();
        } else if (size == 0) {
            Node current = new Node(item, null, null);
            head = current;
            tail = current;
            incSize();
        } else {
            Node current = new Node(item, null, tail);
            tail.setNext(current);
            tail = current;
            incSize();
        }
    }

    private void incSize() {
        size++;
    }

    private void decSize() {
        size--;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Node oldHead = head;
        Item item = head.getItem();
        head = head.getNext();
        // reset the pointer of the head to let GC the oldHead memory
        if (size != 1) {
            head.setAhead(null);
        }
        oldHead.setNext(null);
        decSize();
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if ( size == 0) {
            throw new NoSuchElementException();
        }
        Item item = tail.getItem();
        Node oldTail = tail;
        tail = tail.getAhead();
        // reset the pointer of the head to let GC the oldHead memory
        if (size != 1) {
            tail.setNext(null);
        }
        oldTail.setAhead(null);
        decSize();
        return item;
    }

    // unit testing
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("hello");
        deque.addFirst("world");
        deque.addLast("fucj");
        for (String string : deque) {
            System.out.println(string);
        }
        System.out.println(deque.size);
        String First1 = deque.removeLast();
        String First2 = deque.removeLast();
        String First3 = deque.removeLast();
        System.out.println(First1);
        System.out.println(First2);
        System.out.println(First3);

    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (null == current) {
                throw new NoSuchElementException();
            }
            Item item = current.getItem();
            current = current.getNext();
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Node {

        private Item item;

        private Node next;

        private Node ahead;

        public Node getAhead() {
            return ahead;
        }

        public void setAhead(Node ahead) {
            this.ahead = ahead;
        }

        public Node() {
        }

        public Node(Item item) {
            this.item = item;
        }

        public Node(Item item, Node next, Node ahead) {
            this.item = item;
            this.next = next;
            this.ahead = ahead;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

    }

}
