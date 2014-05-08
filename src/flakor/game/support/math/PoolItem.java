package flakor.game.support.math;

/**
 * Created by longjiyang on 13-7-11.
 */
public abstract class PoolItem
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    Pool<? extends PoolItem> mParent;
    boolean mRecycled = true;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public Pool<? extends PoolItem> getParent()
    {
        return this.mParent;
    }

    public boolean isRecycled()
    {
        return this.mRecycled;
    }

    public boolean isFromPool(final Pool<? extends PoolItem> pPool) {
        return pPool == this.mParent;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    protected void onRecycle() {

    }

    protected void onObtain() {

    }

    public void recycle() {
        if(this.mParent == null) {
            throw new IllegalStateException("Item already recycled!");
        }

        this.mParent.recycle(this);
    }
}
