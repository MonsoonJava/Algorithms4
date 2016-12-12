package queues;



import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

        private Item[] rq = null;
        private int N = 0;

        // construct an empty randomized queue
        public RandomizedQueue() {
                rq = (Item[]) new Object[1];
        }

        // is the queue empty?
        public boolean isEmpty() {
                return N == 0;
        }

        private void resizing(int capacity) {
                Item[] copy = (Item[]) new Object[capacity];
                for (int i = 0; i < N; i++) {
                        copy[i] = rq[i];
                }
                rq = copy;
        }

        // return the number of items on the queue
        public int size() {
                return N;
        }

        // add the item
        public void enqueue(Item item) {
                if (null == item) {
                        throw new NullPointerException();
                }
                if (N == rq.length) {
                        resizing(2 * rq.length);
                }
                rq[N] = item;
                N++;
        }

        // remove and return a random item
        public Item dequeue() {
                if (N == 0) {
                        throw new NoSuchElementException();
                }
                if (N > 0 && N == (rq.length / 4)) {
                        resizing(rq.length / 2);
                }
                int randomNum = StdRandom.uniform(0, N);
                Item retItem = rq[randomNum];
                copyAfter(randomNum);
                N--;
                return retItem;
        }

        private void copyAfter(int index) {
                for (int i = index; i < N - 1; i++) {
                        rq[i] = rq[i + 1];
                }
        }

        // return (but do not remove) a random item
        public Item sample() {
                if (N == 0) {
                        throw new NoSuchElementException();
                }
                int randomNum = StdRandom.uniform(0, N);
                Item retItem = rq[randomNum];
                return retItem;
        }

        // return an independent iterator over items in random order
        public Iterator<Item> iterator() {
                return new RandomizedQueueIterator();
        }

        // unit testing
        public static void main(String[] args) {
                RandomizedQueue<Integer> integerRandom = new RandomizedQueue<>();
                System.out.println(integerRandom.isEmpty());
                integerRandom.enqueue(10);
                integerRandom.enqueue(20);
                integerRandom.enqueue(11);
                integerRandom.enqueue(13);
                System.out.println(integerRandom.isEmpty());
                System.out.println(integerRandom.size());
                Iterator<Integer> iterator = integerRandom.iterator();
                while (iterator.hasNext()) {
                        System.out.println(iterator.next());
                }
                Integer dequeue = integerRandom.dequeue();
                System.out.println("dequeue" + dequeue);
                System.out.println(integerRandom.size());
                for (Integer integer : integerRandom) {
                        System.out.println(integer);
                }
        }

        private class RandomizedQueueIterator implements Iterator<Item> {

                private int[] randomIndex = null;
                private int index = 0;

                RandomizedQueueIterator() {
                        randomIndex = new int[N];
                        for (int i = 0; i < N; i++) {
                                randomIndex[i] = i;
                        }
                        StdRandom.shuffle(randomIndex);
                }

                @Override
                public boolean hasNext() {
                        return index != randomIndex.length;
                }

                @Override
                public Item next() {
                        if (index == randomIndex.length) {
                                throw new NoSuchElementException();
                        }
                        Item item = rq[randomIndex[index]];
                        index++;
                        return item;
                }

                @Override
                public void remove() {
                        throw new UnsupportedOperationException();
                }
        }

}
