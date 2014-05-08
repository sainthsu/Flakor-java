package flakor.game.system.graphics;


import android.util.FloatMath;

import flakor.game.core.element.Point;
import flakor.game.support.math.MathConstants;

import java.io.Serializable;

/**
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.12.18:00
 * @copyright Saint Hsu
 * This class represents an affine transformation with the following matrix:
 * <pre>[ a , b , u ]
 * 		[ c , d , v ]
 * 		[ tx, ty, w ]</pre>
 * where:
 * <ul>
 *  <li><b>a</b> is the <b>x scale</b></li>
 *  <li><b>b</b> is the <b>y skew</b></li>
 *  <li><b>c</b> is the <b>x skew</b></li>
 *  <li><b>d</b> is the <b>y scale</b></li>
 *  <li><b>tx</b> is the <b>x translation (position)</b></li>
 *  <li><b>ty</b> is the <b>y translation (position)</b></li>
 *  <li>The u, v, and w positions are static values that remain at 0, 0, and 1 respectively<li>
 * </ul>
 *  x' = x*a + y*c + tx
 *  y' = x*b + y*d + ty
 */

public class TransformMatrix implements Serializable
{
	// ===========================================================
	// Fields
	// ===========================================================
    private static final long serialVersionUID = 1330973210523860834L;

    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_MASK_SCALE = TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE;
    public static final int TYPE_MASK_ROTATION = TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION;
    static final int TYPE_UNKNOWN = -1;
    static final double ZERO = 1.0E-10D;

    transient int type;

    private float a = 1.0f; /* x scale */
	private float b = 0.0f; /* y skew */
	private float c = 0.0f; /* x skew */
	private float d = 1.0f; /* y scale */
	private float tx = 0.0f; /* x translation */
	private float ty = 0.0f; /* y translation */
	//private float u = 0.0f;
	//private float v = 0.0f;
	//private float w = 1.0f;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public TransformMatrix()
	{
		// TODO Auto-generated constructor stub
        this.identity();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
		
	public String toString()
	{
		return "Transformation{[" + this.a + ", " + this.c + ", " + this.tx + "][" + this.b + ", " + this.d + ", " + this.ty + "][0.0, 0.0, 1.0]}";
	}
	
	/******************************************************
	 * set to Identity Matrix
	 * 		[ 1 , 0 , 0 ]
	 * 		[ 0 , 1 , 0 ]
	 * 		[ 0 , 0 , 1 ]
	 *****************************************************/
	
	public void identity()
	{
		this.a = 1.0f;
		this.d = 1.0f;

		this.b = 0.0f;
		this.c = 0.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;
	}
	
	public final void set(final TransformMatrix matrix)
	{
		this.a = matrix.a;
		this.d = matrix.d;

		this.b = matrix.b;
		this.c = matrix.c;
		this.tx = matrix.tx;
		this.ty = matrix.ty;
	}
	
	public final void reset()
	{
		this.identity();
	}
	
	public void translate(final float tX, final float tY)
	{
		this.tx += tX;
		this.ty += tY;
	}
	
	public void scale(final float sX, final float sY)
	{
		this.a *= sX;
		this.b *= sY;
		this.c *= sX;
		this.d *= sY;
		this.tx *= sX;
		this.ty *= sY;
	}
	
	public final void skew(final float skewX, final float skewY)
	{
		final float tanX = (float) Math.tan(-MathConstants.DEG_TO_RAD * skewX);
		final float tanY = (float) Math.tan(-MathConstants.DEG_TO_RAD * skewY);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a + b * tanX;
		this.b = a * tanY + b;
		this.c = c + d * tanX;
		this.d = c * tanY + d;
		this.tx = tx + ty * tanX;
		this.ty = tx * tanY + ty;
	}
	
	public void rotate(final float angle)
	{
		final float angleRad = MathConstants.DEG_TO_RAD * angle;

		final float sin = FloatMath.sin(angleRad);
		final float cos = FloatMath.cos(angleRad);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a * cos - b * sin;
		this.b = a * sin + b * cos;
		this.c = c * cos - d * sin;
		this.d = c * sin + d * cos;
		this.tx = tx * cos - ty * sin;
		this.ty = tx * sin + ty * cos;
	}
	

	public void concat(TransformMatrix matrix)
	{
		this.concat(matrix.a, matrix.b, matrix.c, matrix.d, matrix.tx, matrix.ty);
	}

    /**
     * Concatenated Matrices
     * @param pA
     * @param pB
     * @param pC
     * @param pD
     * @param pTX
     * @param pTY
     */

	public void concat(final float pA, final float pB, final float pC, final float pD, final float pTX, final float pTY)
	{
		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a * pA + b * pC;
		this.b = a * pB + b * pD;
		this.c = c * pA + d * pC;
		this.d = c * pB + d * pD;
		this.tx = tx * pA + ty * pC + pTX;
		this.ty = tx * pB + ty * pD + pTY;
	}
	
	public final void preConcat(final TransformMatrix matrix)
	{
		this.preConcat(matrix.a, matrix.b, matrix.c, matrix.d, matrix.tx, matrix.ty);
	}

	private void preConcat(final float pA, final float pB, final float pC, final float pD, final float pTX, final float pTY)
	{
		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = pA * a + pB * c;
		this.b = pA * b + pB * d;
		this.c = pC * a + pD * c;
		this.d = pC * b + pD * d;
		this.tx = pTX * a + pTY * c + tx;
		this.ty = pTX * b + pTY * d + ty;
	}
	
	public void inverse()
	{
		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;
		final float det = (a*d-b*c); //determinant

		this.a = d/det;
		this.b = -b/det;
		this.c = -c/det;
		this.d = a/det;
		this.tx = (c*ty-d*tx)/det;
		this.ty = (b*tx-a*ty)/det;
	}
	
	public final void transform(final float[] vertices)
	{
		int count = vertices.length >> 1;
		int i = 0;
		int j = 0;
		while(--count >= 0)
		{
			final float x = vertices[i++];
			final float y = vertices[i++];
			vertices[j++] = x * this.a + y * this.c + this.tx;
			vertices[j++] = x * this.b + y * this.d + this.ty;
		}
	}
	
	public final Point transform(final Point point)
	{
		float x = point.x*this.a + point.y * this.c + this.tx;
		float y = point.x * this.b + point.y * this.d + this.ty;
		
		point.x = x;
		point.y = y;
		
		return point;
	}
	
}
