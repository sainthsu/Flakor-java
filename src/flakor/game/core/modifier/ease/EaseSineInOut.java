package flakor.game.core.modifier.ease;

import android.util.FloatMath;

import flakor.game.support.math.MathConstants;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseSineInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseSineInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseSineInOut() {

	}

	public static EaseSineInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseSineInOut();
		}
		return INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getPercentage(final float pSecondsElapsed, final float pDuration) {
		final float percentage = pSecondsElapsed / pDuration;

		return -0.5f * (FloatMath.cos(percentage * MathConstants.PI) - 1);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
