package flakor.game.tool;

import flakor.game.BuildConfig;
import flakor.game.support.util.TimeConstants;
import flakor.game.tool.Debug;

public class FPSLogger extends AverageFPSCounter {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    protected float mShortestFrame = Float.MAX_VALUE;
    protected float mLongestFrame = Float.MIN_VALUE;

    // ===========================================================
    // Constructors
    // ===========================================================

    public FPSLogger() {
            super();
    }

    public FPSLogger(final float pAverageDuration) {
            super(pAverageDuration);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onHandleAverageDurationElapsed(final float pFPS) {
            this.onLogFPS();

            this.mLongestFrame = Float.MIN_VALUE;
            this.mShortestFrame = Float.MAX_VALUE;
    }

    @Override
    public void onUpdate(final float pSecondsElapsed) {
            super.onUpdate(pSecondsElapsed);

            this.mShortestFrame = Math.min(this.mShortestFrame, pSecondsElapsed);
            this.mLongestFrame = Math.max(this.mLongestFrame, pSecondsElapsed);
    }

    @Override
    public void reset() {
            super.reset();

            this.mShortestFrame = Float.MAX_VALUE;
            this.mLongestFrame = Float.MIN_VALUE;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void onLogFPS() {
            if(BuildConfig.DEBUG) {
                    Debug.d(String.format("FPS: %.2f (MIN: %.0f ms | MAX: %.0f ms)",
                            this.mFrames / this.mSecondsElapsed,
                            this.mShortestFrame * TimeConstants.MILLISECONDS_PER_SECOND,
                            this.mLongestFrame * TimeConstants.MILLISECONDS_PER_SECOND));
            }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
