package flakor.game.core.element;


import flakor.game.support.math.ColorMath;
import flakor.game.tool.Debug;

/**
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.12.18:00
 * @version 0.0.1beta
 */

public class Color
{
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final float COLOR_FACTOR_INT_TO_FLOAT = 255.0f;
	
	public static final int ABGR_PACKED_RED_SHIFT = 0;
	public static final int ABGR_PACKED_GREEN_SHIFT = 8;
	public static final int ABGR_PACKED_BLUE_SHIFT = 16;
	public static final int ABGR_PACKED_ALPHA_SHIFT = 24;

	public static final int ABGR_PACKED_RED_CLEAR = 0XFFFFFF00;
	public static final int ABGR_PACKED_GREEN_CLEAR = 0XFFFF00FF;
	public static final int ABGR_PACKED_BLUE_CLEAR = 0XFF00FFFF;
	public static final int ABGR_PACKED_ALPHA_CLEAR = 0X00FFFFFF;
	
	public static final int RGBA_PACKED_ALPHA_SHIFT = 0;
	public static final int RGBA_PACKED_BLUE_SHIFT = 8;
	public static final int RGBA_PACKED_GREEN_SHIFT = 16;
	public static final int RGBA_PACKED_RED_SHIFT = 24;
	
	public static final int RGBA_PACKED_ALPHA_CLEAR = 0XFFFFFF00;
	public static final int RGBA_PACKED_BLUE_CLEAR = 0XFFFF00FF;
	public static final int RGBA_PACKED_GREEN_CLEAR = 0XFF00FFFF;
	public static final int RGBA_PACKED_RED_CLEAR = 0X00FFFFFF;
	
	public static final int ARGB_PACKED_BLUE_SHIFT = 0;
	public static final int ARGB_PACKED_GREEN_SHIFT = 8;
	public static final int ARGB_PACKED_RED_SHIFT = 16;
	public static final int ARGB_PACKED_ALPHA_SHIFT = 24;

	public static final int ARGB_PACKED_BLUE_CLEAR = 0XFFFFFF00;
	public static final int ARGB_PACKED_GREEN_CLEAR = 0XFFFF00FF;
	public static final int ARGB_PACKED_RED_CLEAR = 0XFF00FFFF;
	public static final int ARGB_PACKED_ALPHA_CLEAR = 0X00FFFFFF;
	
	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0, 1);
	public static final Color GREEN = new Color(0, 1, 0, 1);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color PINK = new Color(1, 0, 1, 1);
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	public static final int WHITE_ARGB_PACKED_INT = Color.WHITE.getRGBAPackedInt();
	public static final int BLACK_ARGB_PACKED_INT = Color.BLACK.getRGBAPackedInt();
	public static final int RED_ARGB_PACKED_INT = Color.RED.getRGBAPackedInt();
	public static final int YELLOW_ARGB_PACKED_INT = Color.YELLOW.getRGBAPackedInt();
	public static final int GREEN_ARGB_PACKED_INT = Color.GREEN.getRGBAPackedInt();
	public static final int CYAN_ARGB_PACKED_INT = Color.CYAN.getRGBAPackedInt();
	public static final int BLUE_ARGB_PACKED_INT = Color.BLUE.getRGBAPackedInt();
	public static final int PINK_ARGB_PACKED_INT = Color.PINK.getRGBAPackedInt();
	public static final int TRANSPARENT_ARGB_PACKED_INT = Color.TRANSPARENT.getRGBAPackedInt();
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private float red;
	private float green;
	private float blue;
	private float alpha;
	
	private int ARGBPackedInt;
	private float ARGBPackedFloat;
	private int ABGRPackedInt;
	private float ABGRPackedFloat;
	
    public Color()
    {
        this(Color.WHITE);
    }

	public Color(final Color color)
	{
		this.set(color);
	}
	
	public Color(final float fRed, final float fGreen, final float fBlue)
	{
		this(fRed, fGreen, fBlue,1);
	}
	
	public Color(final float fRed, final float fGreen, final float fBlue, final float fAlpha)
	{
		this.set(fRed, fGreen, fBlue, fAlpha);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public final float getRed()
	{
		return red;
	}

	public final void setRed(float red)
	{
		this.red = red;
	}

    /**
     * check before set value
     * @param red
     * @return true if not same
     */
	public final boolean setRedChecking(final float red)
	{
		if(this.red != red)
		{
			this.red = red;
			this.packARGBRed();
			return true;
		}
		return false;
	}
	
	public final float getGreen()
	{
		return green;
	}

	public final void setGreen(final float green)
	{
		this.green = green;
	}

	public final boolean setGreenChecking(final float green)
	{
		if(this.green != green)
		{
			this.green = green;

			this.packARGBGreen();
			return true;
		}
		return false;
	}
	
	public final float getBlue() {
		return blue;
	}

	public final void setBlue(final float blue)
	{
		this.blue = blue;
	}

	public final boolean setBlueChecking(final float blue)
	{
		if(this.blue != blue)
		{
			this.blue = blue;

			this.packARGBBlue();
			return true;
		}
		return false;
	}
	
	public final float getAlpha()
	{
		return alpha;
	}

	public final void setAlpha(final float alpha)
	{
		this.alpha = alpha;
	}


	public final boolean setAlphaChecking(final float alpha)
	{
		if(this.alpha != alpha)
		{
			this.alpha = alpha;

			this.packARGBAlpha();
			return true;
		}
		return false;
	}
	
	public final void set(final float fRed,final float fGreen,final float fBlue)
	{
		this.red = fRed;
		this.green = fGreen;
		this.blue = fBlue;

		this.packARGB();
        this.packABGR();
	}

	public final void set(final float fRed,final float fGreen,final float fBlue,final float fAlpha)
	{
		this.red = fRed;
		this.green = fGreen;
		this.blue = fBlue;
		this.alpha = fAlpha;
		
		this.packARGB();
        this.packABGR();
	}

	public final void set(final Color color)
	{
		this.red = color.red;
		this.green = color.green;
		this.blue = color.blue;
		this.alpha = color.alpha;
		
		this.packARGB();
		this.packABGR();
	}
	
	public final boolean setChecking(final float red, final float green, final float blue)
	{
		if((this.red != red) || (this.green != green) || (this.blue != blue))
		{
			this.red = red;
			this.green = green;
			this.blue = blue;

			this.packARGB();
            this.packABGR();
			return true;
		}
		return false;
	}
	
	public final boolean setChecking(final float red, final float green, final float blue,final float alpha)
	{
		if((this.red != red) || (this.green != green) || (this.blue != blue)||(this.alpha != alpha))
		{
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;

			this.packARGB();
            this.packABGR();
			return true;
		}
		return false;
	}
	
	public final boolean setChecking(final Color color)
	{
		if(this.ARGBPackedInt != color.ARGBPackedInt)
		{
			this.red = color.red;
			this.green = color.green;
			this.blue = color.blue;
			this.alpha = color.alpha;
			this.ARGBPackedInt = color.ARGBPackedInt;
			this.ARGBPackedFloat = color.ARGBPackedFloat;

			return true;
		}
		return false;
	}
	
	
	public final void reset()
	{
		this.set(Color.WHITE);
	}
	
	public final int getRGBAPackedInt()
	{
		return ((int)(255 * this.alpha) << Color.RGBA_PACKED_ALPHA_SHIFT) | ((int)(255 * red) << Color.RGBA_PACKED_RED_SHIFT) | ((int)(255 * green) << Color.RGBA_PACKED_GREEN_SHIFT) | ((int)(255 * blue) << Color.RGBA_PACKED_BLUE_SHIFT);
	}
	
	public final int getARGBPackedInt()
	{
		return this.ARGBPackedInt;
	}

	public final float getARGBPackedFloat()
	{
		return this.ARGBPackedFloat;
	}
	
	public final int getABGRPackedInt()
	{
		return this.ABGRPackedInt;
	}

	public final float getABGRPackedFloat()
	{
		return this.ABGRPackedFloat;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public int hashCode()
	{
		return this.ARGBPackedInt;
	}
	
	@Override
	public boolean equals(final Object object)
	{
		if(this == object)
		{
			return true;
		}
		else if(object == null)
		{
			return false;
		} 
		else if(this.getClass() != object.getClass())
		{
			return false;
		}

		return this.equals((Color) object);
	}
	
	public boolean equals(final Color color)
	{
		return this.ARGBPackedInt == color.ARGBPackedInt;
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("[Red: ")
			.append(this.red)
			.append(", Green: ")
			.append(this.green)
			.append(", Blue: ")
			.append(this.blue)
			.append(", Alpha: ")
			.append(this.alpha)
			.append("]")
			.toString();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	private final void packARGBRed()
	{
		this.ARGBPackedInt = (this.ARGBPackedInt & Color.ARGB_PACKED_RED_CLEAR)|((int)(255*this.red) << Color.ARGB_PACKED_RED_SHIFT);
		this.ARGBPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ARGBPackedInt);
	}
	
	private final void packARGBGreen()
	{
		this.ARGBPackedInt = (this.ARGBPackedInt & Color.ARGB_PACKED_GREEN_CLEAR)|((int)(255*this.red) << Color.ARGB_PACKED_GREEN_SHIFT); 
		this.ARGBPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ARGBPackedInt);
	}
	
	private final void packARGBBlue()
	{
		this.ARGBPackedInt = (this.ARGBPackedInt & Color.ARGB_PACKED_BLUE_CLEAR)|((int)(255*this.red) << Color.ARGB_PACKED_BLUE_SHIFT); 
		this.ARGBPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ARGBPackedInt);
	}
	
	private final void packARGBAlpha()
	{
		this.ARGBPackedInt = (this.ARGBPackedInt & Color.ARGB_PACKED_ALPHA_CLEAR)|((int)(255*this.red) << Color.ARGB_PACKED_ALPHA_SHIFT); 
		this.ARGBPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ARGBPackedInt);
	}
	
	private final void packARGB()
	{
		this.ARGBPackedInt = ColorMath.convertRGBAToARGBPackedInt(this.red,this.green, this.blue, this.alpha);
		this.ARGBPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ARGBPackedInt);
	}
	
	private final void packABGR()
	{
		this.ABGRPackedInt = ColorMath.convertRGBAToABGRPackedInt(this.red, this.green, this.blue, this.alpha);
		this.ABGRPackedFloat = ColorMath.convertPackedIntToPackedFloat(this.ABGRPackedInt);
	}

    /**
     * mix two colors by each percentage
     * @param pColorA
     * @param pPercentageA
     * @param pColorB
     * @param pPercentageB
     */
	public final void mix(final Color pColorA, final float pPercentageA, final Color pColorB, final float pPercentageB)
	{
		final float red = (pColorA.red * pPercentageA) + (pColorB.red * pPercentageB);
		final float green = (pColorA.green * pPercentageA) + (pColorB.green * pPercentageB);
		final float blue = (pColorA.blue * pPercentageA) + (pColorB.blue * pPercentageB);
		final float alpha = (pColorA.alpha * pPercentageA) + (pColorB.alpha * pPercentageB);

		this.set(red, green, blue, alpha);
	}
	
	public class RGBA
	{
		public static final int GL_SIZE=4;
		public int R;
		public int G;
		public int B;
		public int A;
		
		public RGBA()
		{
			this.R=
			this.G=
			this.B=
			this.A=255;
		}
	}
	
	public class RGBAf
	{
		public static final int GL_SIZE=4;
		public float R;
		public float G;
		public float B;
		public float A;
		
		public RGBAf()
		{
			this.R=
			this.G=
			this.B=
			this.A=1.0f;
		}
	}
}
