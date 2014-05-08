package flakor.game.system;

/**
 * Created by saint on 8/28/13.
 */
public interface ProgressCallable<T>
{
    /**
     * Computes a result, or throws an exception if unable to do so.
     * @param progressListener
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    public T call(final ProgressListener progressListener) throws Exception;
}
