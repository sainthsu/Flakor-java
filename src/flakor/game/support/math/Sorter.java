package flakor.game.support.math;

import java.util.Comparator;
import java.util.List;


public abstract class Sorter<T>
{
	public abstract void sort(final T[] array, final int start, final int end, final Comparator<T> comparator);
	public abstract void sort(final List<T> list, final int start, final int end, final Comparator<T> comparator);
	public abstract void sort(final ListInterface<T> list, final int start, final int end, final Comparator<T> comparator);

	// ===========================================================
	// Methods
	// ===========================================================

	public final void sort(final T[] array, final Comparator<T> comparator)
	{
		this.sort(array, 0, array.length, comparator);
	}

	public final void sort(final List<T> list, final Comparator<T> comparator)
	{
		this.sort(list, 0, list.size(), comparator);
	}

	public final void sort(final ListInterface<T> list, final Comparator<T> comparator)
	{
		this.sort(list, 0, list.size(), comparator);
	}
}
