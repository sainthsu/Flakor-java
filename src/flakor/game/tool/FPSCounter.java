package flakor.game.tool;

import flakor.game.system.graphics.UpdatableInterface;

public class FPSCounter implements UpdatableInterface {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    protected float mSecondsElapsed;

    protected int mFrames;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public float getFPS() {
            return this.mFrames / this.mSecondsElapsed;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdate(final float pSecondsElapsed) {
            this.mFrames++;
            this.mSecondsElapsed += pSecondsElapsed;
    }

    @Override
    public void reset() {
            this.mFrames = 0;
            this.mSecondsElapsed = 0;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
