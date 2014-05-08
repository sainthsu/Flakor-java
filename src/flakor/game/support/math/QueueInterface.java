package flakor.game.support.math;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface QueueInterface<T> extends ListInterface<T>
{
    // ===========================================================
    // Methods
    // ===========================================================

    public T peek();
    public T poll();
    public void enter(final T pItem);
    public void enter(final int pIndex, final T pItem) throws IndexOutOfBoundsException;

}
