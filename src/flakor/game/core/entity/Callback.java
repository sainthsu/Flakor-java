package flakor.game.core.entity;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface Callback<T>
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void onCallback(final T pCallbackValue);
}
