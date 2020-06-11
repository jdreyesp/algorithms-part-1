package week2;

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

/**
 * To set this solution:
 *
 * 1. Copy this week2 folder in src/main/java
 * 2. Run:
 * javac -cp ../../../algs4.jar *.java
 * java -cp ../../../algs4.jar:. Permutation 8 < duplicates.txt
 */
public class Permutation {

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) throw new IllegalArgumentException();
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        for (int i = 0; i < k; i++) {
            randomizedQueue.enqueue(StdIn.readString());
        }

        Iterator<String> iterator = randomizedQueue.iterator();

        for (int i=0; i < k && iterator.hasNext(); i++) {
            System.out.println(iterator.next());
        }

    }
}
