import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] tab;
    private int n;

    public RandomizedQueue() {
        tab = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (n == tab.length) resize(2 * tab.length);
        if (n == 0) {
            tab[n++] = item;
            return;
        }
        int x = StdRandom.uniform(n);
        Item aux = tab[x];
        tab[x] = item;
        tab[n++] = aux;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = tab[i];
        }
        tab = copy;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        if (n == tab.length / 4) resize(tab.length / 2);
        int x = StdRandom.uniform(n);
        Item item = tab[x];
        tab[x] = tab[--n];
        tab[n] = null; // to prevent loitering
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return tab[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i;
        private int[] x;
        public ArrayIterator() {
            i = 0;
            x = new int[n];
            for (int j = 0; j < n; j++) {
                x[j] = j;
            }
            StdRandom.shuffle(x);
        }
        public boolean hasNext() {return i < n;}
        public void remove() {throw new UnsupportedOperationException();}
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return tab[x[i++]];
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        System.out.println(queue.size());
        for (Integer i : queue) {
            System.out.println(i);
        }
        System.out.println("sample:" + queue.sample());
        System.out.println("dequeue");
        while (!queue.isEmpty()) System.out.println(queue.dequeue());
        System.out.println(queue.size());
    }
}
