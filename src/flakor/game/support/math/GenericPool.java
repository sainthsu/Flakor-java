package flakor.game.support.math;

import flakor.game.console.config.EngineConfig;
import flakor.game.tool.Debug;

import java.util.ArrayList;
import java.util.Collections;


public abstract class GenericPool<T>
{
	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<T> availableItems;
	private final int growth;
	private final int availableItemCountMaximum;

	private int unrecycledItemCount;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public GenericPool() 
	{
		this(0);
	}

	public GenericPool(final int initialSize)
	{
		this(initialSize, 1);
	}

	public GenericPool(final int initialSize, final int growth)
	{
		this(initialSize, growth, Integer.MAX_VALUE);
	}

	public GenericPool(final int initialSize, final int growth, final int availableItemsMaximum)
	{
		if(growth <= 0)
		{
			throw new IllegalArgumentException("Growth must be greater than 0!");
		}
		if(availableItemsMaximum < 0) 
		{
			throw new IllegalArgumentException("AvailableItemsMaximum must be at least 0!");
		}

		this.growth = growth;
		this.availableItemCountMaximum = availableItemsMaximum;
		this.availableItems = new ArrayList<T>(initialSize);

		if(initialSize > 0)
		{
			this.batchAllocatePoolItems(initialSize);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public synchronized int getUnrecycledItemCount() 
	{
		return unrecycledItemCount;
	}
	
	public synchronized int getAvailableItemCount()
	{
		return this.availableItems.size();
	}
	
	public int getAvailableItemCountMaximum() 
	{
		return availableItemCountMaximum;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onAllocatePoolItem();

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pItem every item passes this method just before it gets recycled.
	 */
	protected void onHandleRecycleItem(final T pItem) 
	{

	}

	protected T onHandleAllocatePoolItem()
	{
		return this.onAllocatePoolItem();
	}

	/**
	 * @param pItem every item that was just obtained from the pool, passes this method.
	 */
	protected void onHandleObtainItem(final T pItem)
	{

	}

	public synchronized void batchAllocatePoolItems(final int count)
	{
		final ArrayList<T> availableItems = this.availableItems;

		int allocationCount = this.availableItemCountMaximum - availableItems.size();
		if(count < allocationCount)
		{
			allocationCount = count;
		}

		for(int i = allocationCount - 1; i >= 0; i--) 
		{
			availableItems.add(this.onHandleAllocatePoolItem());
		}
	}

	public synchronized T obtainPoolItem()
	{
		final T item;

		if(this.availableItems.size() > 0) 
		{
			item = this.availableItems.remove(this.availableItems.size() - 1);
		} else {
			if(this.growth == 1 || this.availableItemCountMaximum == 0) {
				item = this.onHandleAllocatePoolItem();
			} else {
				this.batchAllocatePoolItems(this.growth);
				item = this.availableItems.remove(this.availableItems.size() - 1);
			}
			if(EngineConfig.DEBUG)
			{
				Debug.d(this.getClass().getName() + "<" + item.getClass().getSimpleName() +"> was exhausted, with " + this.unrecycledItemCount + " item not yet recycled. Allocated " + this.growth + " more.");
			}
		}
		this.onHandleObtainItem(item);

		this.unrecycledItemCount++;
		return item;
	}

	public synchronized void recyclePoolItem(final T pItem)
	{
		if(pItem == null)
		{
			throw new IllegalArgumentException("Cannot recycle null item!");
		}

		this.onHandleRecycleItem(pItem);

		if(this.availableItems.size() < this.availableItemCountMaximum)
		{
			this.availableItems.add(pItem);
		}

		this.unrecycledItemCount--;

		if(this.unrecycledItemCount < 0) 
		{
			Debug.e("More items recycled than obtained!");
		}
	}

	public synchronized void shufflePoolItems()
	{
		Collections.shuffle(this.availableItems);
	}

}
