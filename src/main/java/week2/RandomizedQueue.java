package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node<Item> first;
    private int size;

    private class Node<Item> {

        private Item item;
        private Node<Item> next;
        private Node<Item> prev;

        public Node(Node<Item> prev, Item item, Node<Item> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

    }

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            first = new Node<>(null, item, null);
        } else {
            Node oldFirstNode = first;
            first = new Node(null, item, oldFirstNode);
            oldFirstNode.prev = first;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        Random random = new Random();
        int searchIndex = random.nextInt(size);
        Node<Item> currentItem = first;
        for (int i = 0; i < searchIndex; i++) currentItem = currentItem.next;

        //dequeing
        Node<Item> prevItem = currentItem.prev;
        Node<Item> nextItem = currentItem.next;
        if(prevItem != null) {
            prevItem.next = nextItem;
        }
        if(nextItem != null) {
            nextItem.prev = prevItem;
        }

        size --;
        return currentItem.item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        Random random = new Random();
        int searchIndex = random.nextInt(size);
        Node<Item> currentItem = first;
        for (int i = 0; i < searchIndex; i++) currentItem = currentItem.next;

        return currentItem.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {

//        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
//
//        randomizedQueue.enqueue("Hello");
//        randomizedQueue.enqueue("World");
//        randomizedQueue.enqueue("Marvelous");
//        randomizedQueue.enqueue("Incredible");
//
//        Iterator<String> randomIterator = randomizedQueue.iterator();
//
//        while (randomIterator.hasNext()) {
//            System.out.println("Iterating: " + randomIterator.next());
//        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Node[] shuffledItems;
        private Node<Item> next;
        private int nextIndex = 0;

        public RandomizedQueueIterator() {
            if (isEmpty()) {
                next = null;
                return;
            }

            Node[] originalItems = new Node[size];
            Node<Item> currentItem = first;
            originalItems[0] = currentItem;

            for (int i = 1; i < originalItems.length; i++) {
                Node<Item> nextNode = currentItem.next;
                originalItems[i] = currentItem = nextNode;
                if (currentItem == null) break;
            }

            shuffledItems = new Node[originalItems.length];
            Random random = new Random();

            for (int i = 0; i < originalItems.length; i++) {
                Node item = originalItems[i];
                int possibleIndex = random.nextInt(originalItems.length);
                while (shuffledItems[possibleIndex] != null) {
                    possibleIndex = random.nextInt(originalItems.length);
                }
                shuffledItems[possibleIndex] = item;
            }

            //Update references
            for (int i = 0; i < shuffledItems.length; i++) {
                Node currentNode = shuffledItems[i];
                if (i == 0) currentNode.prev = null;
                if (i + 1 == shuffledItems.length) {
                    currentNode.next = null;
                    break;
                }
                Node nextItem = shuffledItems[i+1];
                currentNode.next = nextItem;
                nextItem.prev = currentNode;
            }

        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public Item next() {

            if (!hasNext())
                throw new NoSuchElementException();

            next = shuffledItems[nextIndex];
            nextIndex++;
            return next.item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
