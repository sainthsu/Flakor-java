package flakor.game.core.entity;

import flakor.game.support.math.PoolUpdateHandler;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public abstract class RunnablePoolUpdateHandler<T extends RunnablePoolItem> extends PoolUpdateHandler<T>
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public RunnablePoolUpdateHandler() {

    }

    public RunnablePoolUpdateHandler(final int pInitialPoolSize) {
        super(pInitialPoolSize);
    }

    public RunnablePoolUpdateHandler(final int pInitialPoolSize, final int pGrowth) {
        super(pInitialPoolSize, pGrowth);
    }

    public RunnablePoolUpdateHandler(final int pInitialPoolSize, final int pGrowth, final int pAvailableItemCountMaximum) {
        super(pInitialPoolSize, pGrowth, pAvailableItemCountMaximum);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected abstract T onAllocatePoolItem();

    @Override
    protected void onHandlePoolItem(final T pRunnablePoolItem) {
        pRunnablePoolItem.run();
    }}
