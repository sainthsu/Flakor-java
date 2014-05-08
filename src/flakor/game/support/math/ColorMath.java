package flakor.game.support.math;

import flakor.game.core.element.Color;


public class ColorMath
{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float[] HSV_TO_COLOR = new float[3];
	private static final int HSV_TO_COLOR_HUE_INDEX = 0;
	private static final int HSV_TO_COLOR_SATURATION_INDEX = 1;
	private static final int HSV_TO_COLOR_VALUE_INDEX = 2;

	private static final int INT_BITS_TO_FLOAT_MASK = 0xFFFFFFFF;
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	* @param hue [0 .. 360)
	* @param saturation [0...1]
	* @param value [0...1]
	*/
	public static final int convertHSVToARGBPackedInt(final float hue, final float saturation, final float value)
	{
			ColorMath.HSV_TO_COLOR[ColorMath.HSV_TO_COLOR_HUE_INDEX] = hue;
			ColorMath.HSV_TO_COLOR[ColorMath.HSV_TO_COLOR_SATURATION_INDEX] = saturation;
			ColorMath.HSV_TO_COLOR[ColorMath.HSV_TO_COLOR_VALUE_INDEX] = value;

			return android.graphics.Color.HSVToColor(ColorMath.HSV_TO_COLOR);
	}

	public static final Color convertHSVToColor(final float hue, final float saturation, final float value)
	{
			return ColorMath.convertARGBPackedIntToColor(ColorMath.convertHSVToARGBPackedInt(hue, saturation, value));
	}


	public static Color convertARGBPackedIntToColor(final int pARGBPackedInt)
	{
			final float alpha = ColorMath.extractAlphaFromARGBPackedInt(pARGBPackedInt);
			final float red = ColorMath.extractRedFromARGBPackedInt(pARGBPackedInt);
			final float green = ColorMath.extractGreenFromARGBPackedInt(pARGBPackedInt);
			final float blue = ColorMath.extractBlueFromARGBPackedInt(pARGBPackedInt);

			return new Color(red, green, blue, alpha);
	}


	public static final int convertRGBAToARGBPackedInt(final float red, final float green, final float blue, final float alpha)
	{
		return ((int)(255 * alpha) << Color.ARGB_PACKED_ALPHA_SHIFT) |
				((int)(255 * red) << Color.ARGB_PACKED_RED_SHIFT) | 
				((int)(255 * green) << Color.ARGB_PACKED_GREEN_SHIFT) | 
				((int)(255 * blue) << Color.ARGB_PACKED_BLUE_SHIFT);
	}

	public static final float convertRGBAToARGBPackedFloat(final float pRed, final float pGreen, final float pBlue, final float pAlpha)
	{
		return ColorMath.convertPackedIntToPackedFloat(ColorMath.convertRGBAToARGBPackedInt(pRed, pGreen, pBlue, pAlpha));
	}

	public static final int convertRGBAToABGRPackedInt(final float pRed, final float pGreen, final float pBlue, final float pAlpha) 
	{
		return ((int)(255 * pAlpha) << Color.ABGR_PACKED_ALPHA_SHIFT) | ((int)(255 * pBlue) << Color.ABGR_PACKED_BLUE_SHIFT) | ((int)(255 * pGreen) << Color.ABGR_PACKED_GREEN_SHIFT) | ((int)(255 * pRed) << Color.ABGR_PACKED_RED_SHIFT);
	}
	
	public static final float convertPackedIntToPackedFloat(final int packedInt)
	{
		return Float.intBitsToFloat(packedInt & ColorMath.INT_BITS_TO_FLOAT_MASK);
	}



	public static float extractBlueFromARGBPackedInt(final int pARGBPackedInt)
	{
		return ((pARGBPackedInt >> Color.ARGB_PACKED_BLUE_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractGreenFromARGBPackedInt(final int ARGBPackedInt)
	{
		return ((ARGBPackedInt >> Color.ARGB_PACKED_GREEN_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractRedFromARGBPackedInt(final int ARGBPackedInt)
	{
		return ((ARGBPackedInt >> Color.ARGB_PACKED_RED_SHIFT) & 0xFF) / 255.0f;
	}

	public static float extractAlphaFromARGBPackedInt(final int ARGBPackedInt)
	{
		return ((ARGBPackedInt >> Color.ARGB_PACKED_ALPHA_SHIFT) & 0xFF) / 255.0f;
	}

}
