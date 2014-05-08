package flakor.game.support.math;

import java.util.Comparator;
import java.util.List;

public class InsertionSorter<T> extends Sorter<T>
{

	@Override
	public void sort(T[] array, int start, int end, Comparator<T> comparator)
	{
		// TODO Auto-generated method stub
		for(int i = start + 1; i < end; i++)
		{
			final T current = array[i];
			T prev = array[i - 1];
			if(comparator.compare(current, prev) < 0)
			{
				int j = i;
				do 
				{
					array[j--] = prev;
				} 
				while(j > start && comparator.compare(current, prev = array[j - 1]) < 0);
				array[j] = current;
			}
		}
		return;
	}

	@Override
	public void sort(List<T> list, int start, int end, Comparator<T> comparator)
	{
		// TODO Auto-generated method stub
		for(int i = start+1;i<end;i++)
		{
			final T current = list.get(i);
			T prev = list.get(i - 1);
			if(comparator.compare(current, prev) < 0)
			{
				int j = i;
				do 
				{
					list.set(j--, prev);
				}
				while(j > start && comparator.compare(current, prev = list.get(j - 1)) < 0);
				list.set(j, current);
			}
		}
		
	}

	@Override
	public void sort(ListInterface<T> list, int start, int end,
			Comparator<T> comparator)
	{
		// TODO Auto-generated method stub
		for(int i = start + 1; i < end; i++)
		{
			final T current = list.get(i);
			T prev = list.get(i - 1);
			if(comparator.compare(current, prev) < 0)
			{
				int j = i;
				do 
				{
					list.set(j--, prev);
				} 
				while(j > start && comparator.compare(current, prev = list.get(j - 1)) < 0);
				list.set(j, current);
			}
		}
		return;
	}

}
