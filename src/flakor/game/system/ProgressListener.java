package flakor.game.system;

/**
 * Created by saint on 8/28/13.
 * 进度条监听接口
 */
public interface ProgressListener
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int PROGRESS_MIN = 0;
    public static final int PROGRESS_MAX = 100;

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * @param progress between 0 and 100.
     */
    public void onProgressChanged(final int progress);
}
