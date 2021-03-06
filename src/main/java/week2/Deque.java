package week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
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

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            initializeFirstAndLast(item);
        } else {
            Node oldFirstNode = first;
            first = new Node(null, item, oldFirstNode);
            oldFirstNode.prev = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (isEmpty()) {
            initializeFirstAndLast(item);
        } else {
            Node oldLastNode = last;
            last = new Node<>(oldLastNode, item, null);
            oldLastNode.next = last;
        }
        size++;
    }

    private void initializeFirstAndLast(Item item) {
        Node<Item> node = new Node<>(null, item, null);
        first = node;
        last = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Node<Item> oldFirst = first;
        Item item = oldFirst.item;
        first = oldFirst.next;
        oldFirst.item = null;
        oldFirst.next = null;
        if (first == null)
            last = null;
        else
            first.prev = null;

        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Node<Item> oldLast = last;
        Item item = oldLast.item;
        last = oldLast.prev;
        oldLast.item = null;
        oldLast.prev = null;
        if (last == null)
            first = null;

        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        final Deque<String> deque = new Deque<>();
        deque.addFirst("Hello");
        deque.addLast("World");
        deque.addLast("BOOM");
        deque.addLast("World");
        deque.addLast("World");

        System.out.println(deque.size());
        deque.removeFirst();
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
        System.out.println(deque.size());

        Iterator<String> iterator = deque.iterator();

        while (iterator.hasNext()) {
            String item = iterator.next();
            System.out.println(item);
        }

    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> lastReturned;
        private Node<Item> next = first;
        private int nextIndex;

        public boolean hasNext() {
            return nextIndex < size;
        }

        public Item next() {

            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
