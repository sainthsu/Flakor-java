package flakor.game.support.math;

import flakor.game.system.graphics.UpdatableInterface;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public abstract class PoolUpdateHandler<T extends PoolItem> implements UpdatableInterface
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final Pool<T> mPool;
    private final QueueInterface<T> mScheduledPoolItemQueue = new SynchronizedQueue<T>(new ShiftList<T>());

    // ===========================================================
    // Constructors
    // ===========================================================

    public PoolUpdateHandler()
    {
        this.mPool = new Pool<T>() {
            @Override
            protected T onAllocatePoolItem() {
                return PoolUpdateHandler.this.onAllocatePoolItem();
            }
        };
    }

    public PoolUpdateHandler(final int pInitialPoolSize)
    {
        this.mPool = new Pool<T>(pInitialPoolSize)
        {
            @Override
            protected T onAllocatePoolItem()
            {
                return PoolUpdateHandler.this.onAllocatePoolItem();
            }
        };
    }

    public PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth)
    {
        this.mPool = new Pool<T>(pInitialPoolSize, pGrowth) {
            @Override
            protected T onAllocatePoolItem() {
                return PoolUpdateHandler.this.onAllocatePoolItem();
            }
        };
    }

    public PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth, final int pAvailableItemCountMaximum)
    {
        this.mPool = new Pool<T>(pInitialPoolSize, pGrowth, pAvailableItemCountMaximum)
        {
            @Override
            protected T onAllocatePoolItem() {
                return PoolUpdateHandler.this.onAllocatePoolItem();
            }
        };
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract T onAllocatePoolItem();

    protected abstract void onHandlePoolItem(final T pPoolItem);

    @Override
    public void onUpdate(final float pSecondsElapsed)
    {
        final QueueInterface<T> scheduledPoolItemQueue = this.mScheduledPoolItemQueue;
        final Pool<T> pool = this.mPool;

        T item;
        while((item = scheduledPoolItemQueue.poll()) != null)
        {
            this.onHandlePoolItem(item);
            pool.recyclePoolItem(item);
        }
    }

    @Override
    public void reset()
    {
        final QueueInterface<T> scheduledPoolItemQueue = this.mScheduledPoolItemQueue;
        final Pool<T> pool = this.mPool;

        T item;
        while((item = scheduledPoolItemQueue.poll()) != null)
        {
            pool.recyclePoolItem(item);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public T obtainPoolItem()
    {
        return this.mPool.obtainPoolItem();
    }

    public void postPoolItem(final T poolItem)
    {
        if(poolItem == null)
        {
            throw new IllegalArgumentException("PoolItem already recycled!");
        }
        else if(!this.mPool.ownsPoolItem(poolItem))
        {
            throw new IllegalArgumentException("PoolItem from another pool!");
        }

        this.mScheduledPoolItemQueue.enter(poolItem);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
