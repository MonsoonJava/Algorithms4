package queues;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;

public class Subset {

    public static void main(String[] args) {

        if (args.length != 1) {
            throw new RuntimeException("the comment arguments must be an int ");
        }
        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        if(k == 0){
            return;
        }
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            randomQueue.enqueue(str);
        }
        Iterator<String> iterator = randomQueue.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            if (i >= k) {
                break;
            }
            i++;
        }

    }
}
