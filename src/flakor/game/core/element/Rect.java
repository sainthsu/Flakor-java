/*
 * Rect.java
 * Created on 8/25/13 12:45 PM
 *
 * ver0.0.1beta 8/25/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.element;

/**
 * Created by saint on 8/25/13.<br>
 * origin :左下角顶点<br>
 * size:矩形宽，高<br>
 * origin :left-bottom Point<br>
 * size:rectangle width height<br>
 * ______w______<br>
 * |***********|<br>
 * |***********h<br>
 * |o__________|<br>
 * @see flakor.game.core.element.Size
 * @see flakor.game.core.element.Point
 */
public class Rect
{
    public Point origin;
    public Size size;
    //private static Rect sRect = new Rect();
    //private static Rect rRect = new Rect();

    public Rect()
    {
        this(0f,0f,0f,0f);
    }

    public Rect(float x,float y,float width,float height)
    {
        this.origin = new Point(x,y);
        this.size = new Size(width,height);
    }

    public Rect(final Rect other)
    {
        this.origin = other.origin;
        this.size = other.size;
    }

    public static Rect makeZero()
    {
        return new Rect();
    }

    public void setRect(float x, float y, float width, float height)
    {
        this.origin.x = x;
        this.origin.y = y;
        this.size.width = width;
        this.size.height = height;
    }

    public static Rect make(float x, float y, float width, float height)
    {
        return new Rect(x,y, width, height);
    }
    /**
     * @return the leftmost x-value of current rect
      */
    public float getMinX()
    {
        return this.origin.x;
    }

    /**
     * @return the midpoint x-value of current rect
     */
    public float getMidX()
    {
        return (origin.x + size.width/2.0f);
    }

    /**
     * @return the rightmost x-value of current rect
     */
    public float getMaxX()
    {
        return (origin.x + size.width);
    }

    /**
     * @return the bottommost y-value of current rect
     */
    public float getMinY()
    {
        return origin.y;
    }

    /**
     * @return the midpoint y-value of current rect
     */
    public float getMidY()
    {
        return origin.y+size.height/2.0f;
    }

    /**
     * @return the topmost y-value of current rect
     */
    public float getMaxY()
    {
        return origin.y + size.height;
    }

    public boolean equals(Rect rect)
    {
        return this.origin.equals(rect.origin) && this.size.equals(rect.size);
    }

    /**
     * check whether a point is in the Rect or not
     * <p>检查点是否在矩形内</p>
     * @param point a Point for checking
     * @return true if in
     */
    public boolean containsPoint(Point point)
    {
        return point.x>=getMidX() && point.x<=getMaxX()
                && point.y >= getMinY() && point.y <= getMaxY();
    }

    /**
     * check whether this Rect intersects a given Rect or not
     * <p>检查参数矩形是否与此矩形交叉</p>
     * @param rect
     * @return
     */
    public boolean intersectsRect(Rect rect)
    {
        return !(    getMaxX() < rect.getMinX() ||
                rect.getMaxX() <      getMinX() ||
                getMaxY() < rect.getMinY() ||
                rect.getMaxY() <      getMinY());
    }

    public Object copy()
    {
        return new Rect(this.origin.x, this.origin.y, this.size.width,
                this.size.height);
    }
}
