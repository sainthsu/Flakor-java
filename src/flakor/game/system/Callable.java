package flakor.game.system;

/**
 * Created by saint on 8/28/13.
 */
public interface Callable<T>
{
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return the computed result.
     * @throws Exception if unable to compute a result.
     */
    public T call() throws Exception;
}
