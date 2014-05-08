package flakor.game.support.math;

import flakor.game.core.entity.MatcherInterface;
import flakor.game.core.entity.ParameterCallable;

import java.util.ArrayList;
import java.util.List;

public class SmartList<T> extends ArrayList<T>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5171479668836179513L;

	public SmartList()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SmartList(int capacity)
	{
		super(capacity);
		// TODO Auto-generated constructor stub
	}
	
	
	public void addFirst(final T item)
	{
		this.add(0, item);
	}

	public T getFirst()
	{
		return this.get(0);
	}
	
	public void addLast(final T item)
	{
		this.add(this.size(),item);
	}
	
	public T getLast() throws IndexOutOfBoundsException
	{
		return this.get(this.size() - 1);
	}
	
	public T get(final MatcherInterface<T> matcher)
	{
		final int size = this.size();
		for(int i = 0; i < size; i++)
		{
			final T item = this.get(i);
			if(matcher.matches(item))
			{
				return item;
			}
		}
		return null;
	}
	
	public T removeFirst() throws IndexOutOfBoundsException
	{
		return this.remove(0);
	}

	public T removeLast() throws IndexOutOfBoundsException
	{
		return this.remove(this.size() - 1);
	}
	
	/**
	 * @param item the item to remove.
	 * @param parameterCallable to be called with the removed item, if it was removed.
	 */
	public boolean remove(final T item, final ParameterCallable<T> parameterCallable)
	{
		final boolean removed = this.remove(item);
		if(removed)
		{
			parameterCallable.call(item);
		}
		return removed;
	}

	public T remove(final MatcherInterface<T> matcher)
	{
		for(int i = 0; i < this.size(); i++)
		{
			if(matcher.matches(this.get(i)))
			{
				return this.remove(i);
			}
		}
		return null;
	}

	public T remove(final MatcherInterface<T> matcher, final ParameterCallable<T> parameterCallable) 
	{
		for(int i = this.size() - 1; i >= 0; i--) 
		{
			if(matcher.matches(this.get(i))) 
			{
				final T removed = this.remove(i);
				parameterCallable.call(removed);
				return removed;
			}
		}
		return null;
	}

	public boolean removeAll(final MatcherInterface<T> matcher)
	{
		boolean result = false;
		for(int i = this.size() - 1; i >= 0; i--)
		{
			if(matcher.matches(this.get(i)))
			{
				this.remove(i);
				result = true;
			}
		}
		return result;
	}

	/**
	 * @param matcher to find the items.
	 * @param parameterCallable to be called with each matched item after it was removed.
	 */
	public boolean removeAll(final MatcherInterface<T> matcher, final ParameterCallable<T> parameterCallable)
	{
		boolean result = false;
		for(int i = this.size() - 1; i >= 0; i--)
		{
			if(matcher.matches(this.get(i)))
			{
				final T removed = this.remove(i);
				parameterCallable.call(removed);
				result = true;
			}
		}
		return result;
	}

	public void clear(final ParameterCallable<T> parameterCallable)
	{
		for(int i = this.size() - 1; i >= 0; i--) 
		{
			final T removed = this.remove(i);
			parameterCallable.call(removed);
		}
	}

	public int indexOf(final MatcherInterface<T> matcher)
	{
		final int size = this.size();
		for(int i = 0; i < size; i++)
		{
			final T item = this.get(i);
			if(matcher.matches(item))
			{
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(final MatcherInterface<T> matcher)
	{
		for(int i = this.size() - 1; i >= 0; i--)
		{
			final T item = this.get(i);
			if(matcher.matches(item))
			{
				return i;
			}
		}
		return -1;
	}

	public ArrayList<T> query(final MatcherInterface<T> matcher)
	{
		return this.query(matcher, new ArrayList<T>());
	}

	public <L extends List<T>> L query(final MatcherInterface<T> matcher, final L result)
	{
		final int size = this.size();
		for(int i = 0; i < size; i++) 
		{
			final T item = this.get(i);
			if(matcher.matches(item)) 
			{
				result.add(item);
			}
		}

		return result;
	}

	public <S extends T> ArrayList<S> queryForSubclass(final MatcherInterface<T> matcher)
	{
		return this.queryForSubclass(matcher, new ArrayList<S>());
	}

	@SuppressWarnings("unchecked")
	public <L extends List<S>, S extends T> L queryForSubclass(final MatcherInterface<T> matcher, final L result)
	{
		final int size = this.size();
		for(int i = 0; i < size; i++) 
		{
			final T item = this.get(i);
			if(matcher.matches(item)) 
			{
				result.add((S)item);
			}
		}

		return result;
	}

	public void call(final ParameterCallable<T> parameterCallable)
	{
		for(int i = this.size() - 1; i >= 0; i--) 
		{
			final T item = this.get(i);
			parameterCallable.call(item);
		}
	}

	public void call(final MatcherInterface<T> matcher, final ParameterCallable<T> parameterCallable)
	{
		for(int i = this.size() - 1; i >= 0; i--) 
		{
			final T item = this.get(i);
			if(matcher.matches(item))
			{
				parameterCallable.call(item);
			}
		}
	}

	
}
