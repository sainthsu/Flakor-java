package flakor.game.support.math;

public interface ListInterface<T>
{
	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public T get(final int index) throws IndexOutOfBoundsException;
	public void set(final int index, final T item) throws IndexOutOfBoundsException;
	public void add(final T item);
	public void add(final int index, final T item) throws IndexOutOfBoundsException;
	public boolean remove(final T item);
	public T removeFirst();
	public T removeLast();
	public T remove(final int index) throws IndexOutOfBoundsException;
	public int size();
	public int indexOf(final T item);
	public void clear();
}
