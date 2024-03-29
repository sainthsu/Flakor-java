package flakor.game.support.math;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class SynchronizedQueue<T> implements QueueInterface<T>
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final QueueInterface<T> mQueue;

    // ===========================================================
    // Constructors
    // ===========================================================

    public SynchronizedQueue(final QueueInterface<T> pQueue)
    {
        this.mQueue = pQueue;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public synchronized boolean isEmpty() {
        return this.mQueue.isEmpty();
    }

    @Override
    public synchronized T get(final int pIndex) throws IndexOutOfBoundsException {
        return this.mQueue.get(pIndex);
    }

    @Override
    public synchronized void set(int pIndex, T pItem) throws IndexOutOfBoundsException {
        this.mQueue.set(pIndex, pItem);
    }

    @Override
    public synchronized int indexOf(final T pItem) {
        return this.mQueue.indexOf(pItem);
    }

    @Override
    public synchronized void add(final T pItem) {
        this.mQueue.add(pItem);
    }

    @Override
    public synchronized void add(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
        this.mQueue.add(pIndex, pItem);
    }

    @Override
    public synchronized T peek() {
        return this.mQueue.peek();
    }

    @Override
    public synchronized T poll() {
        return this.mQueue.poll();
    }

    @Override
    public synchronized void enter(final T pItem) {
        this.mQueue.enter(pItem);
    }

    @Override
    public synchronized void enter(final int pIndex, final T pItem) throws IndexOutOfBoundsException{
        this.mQueue.enter(pIndex, pItem);
    }

    @Override
    public synchronized T removeFirst() {
        return this.mQueue.removeFirst();
    }

    @Override
    public synchronized T removeLast() {
        return this.mQueue.removeLast();
    }

    @Override
    public synchronized boolean remove(final T pItem) {
        return this.mQueue.remove(pItem);
    }

    @Override
    public synchronized T remove(final int pIndex) throws IndexOutOfBoundsException{
        return this.mQueue.remove(pIndex);
    }

    @Override
    public synchronized int size() {
        return this.mQueue.size();
    }

    @Override
    public synchronized void clear() {
        this.mQueue.clear();
    }
}
