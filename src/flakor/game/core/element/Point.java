package flakor.game.core.element;

import flakor.game.system.graphics.TransformMatrix;

/**
 * <p>CopyRight (c) 2013.5 Saint Hsu
 * <p>点是一个二维的点，包含x，y轴。
 * <p>这个类提供了一些静态方法用于计算点的加减乘除等运算。
 * <p>Point is a 2D point which contains x,y axises.
 * <p>This class supply some static methods to calculate point such as add,sub etc.
 */
public class Point
{
    public float x;
    public float y;

    public static Point makeZero()
    {
        return new Point(0.0F, 0.0F);
    }

    /**
     * <p>通过弧度初始化一个点。
     * <p>make a point by radian.
     * <p>@param r radian
     * <p>@return Point
     */
    public static Point makeByRadian(float r)
    {
        return make((float) Math.cos(r), (float) Math.sin(r));
    }

    public static Point make(float x, float y)
    {
        return new Point(x, y);
    }

    protected Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "{\"x\":" + this.x + ",\"y\": " + this.y + "}";
    }

    public Point applyTransform(TransformMatrix matrix)
    {
        return matrix.transform(this);
    }

    public boolean equals(final Point other)
    {
        return isEqual(this,other);
    }

    public static boolean isEqual(Point p1, Point p2)
    {
        return (p1.x == p2.x) && (p1.y == p2.y);
    }

    public static Point negate(Point v)
    {
        return make(-v.x, -v.y);
    }

    public static Point add(Point v1, Point v2)
    {
        return make(v1.x + v2.x, v1.y + v2.y);
    }

    public static Point sub(Point v1, Point v2)
    {
        return make(v1.x - v2.x, v1.y - v2.y);
    }

    public static Point mul(Point v, float s)
    {
        return make(v.x * s, v.y * s);
    }

    public static Point mul(Point v, Point v2)
    {
        return make(v.x * v2.x, v.y * v2.y);
    }

    public static Point midpoint(Point v1, Point v2)
    {
        return mul(add(v1, v2), 0.5F);
    }

    /**
     * 点乘
     *
     * @param v1
     * @param v2
     * @return Point
     */
    public static float dot(Point v1, Point v2)
    {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static float cross(Point v1, Point v2)
    {
        return v1.x * v2.y - v1.y * v2.x;
    }

    public static Point perp(Point v)
    {
        return make(-v.y, v.x);
    }

    public static Point rperp(Point v)
    {
        return make(v.y, -v.x);
    }

    public static Point project(Point v1, Point v2)
    {
        return mul(v2, dot(v1, v2) / dot(v2, v2));
    }

    public static Point rotate(Point v1, Point v2)
    {
        return make(v1.x * v2.x - v1.y * v2.y, v1.x * v2.y + v1.y * v2.x);
    }

    public static Point rotateByAngle(Point v, Point pivot, float angle)
    {
        Point r = sub(v, pivot);
        float t = r.x;
        float cosa = (float) Math.cos(angle);
        float sina = (float) Math.sin(angle);
        r.x = (t * cosa - r.y * sina);
        r.y = (t * sina + r.y * cosa);
        r = add(r, pivot);
        return r;
    }

    public static Point unrotate(Point v1, Point v2)
    {
        return make(v1.x * v2.x + v1.y * v2.y, v1.y * v2.x - v1.x * v2.y);
    }

    public static Point lerp(Point a, Point b, float alpha)
    {
        return add(mul(a, 1.0F - alpha), mul(b, alpha));
    }

    public static Point slerp(Point a, Point b, float t)
    {
        float omega = (float) Math.acos(dot(a, b));

        if (omega != 0.0F) {
            float denom = 1.0F / (float) Math.sin(omega);
            return add(mul(a, (float) Math.sin((1.0F - t) * omega) * denom),
                    mul(b, (float) Math.sin(t * omega) * denom));
        }
        return a;
    }

    public static boolean fuzzyEqual(Point a, Point b, float var)
    {
        return (a.x - var <= b.x) && (b.x <= a.x + var) && (a.y - var <= b.y)
                && (b.y <= a.y + var);
    }

    public static boolean lineIntersect(Point p1, Point p2, Point p3,
                                        Point p4, float[] st)
    {
        Point p13 = sub(p1, p3);
        Point p43 = sub(p4, p3);

        Point zero = makeZero();
        if (fuzzyEqual(p43, zero, 1.192093E-07F)) {
            return false;
        }
        Point p21 = sub(p2, p1);

        if (fuzzyEqual(p21, zero, 1.192093E-07F)) {
            return false;
        }
        float d1343 = dot(p13, p43);
        float d4321 = dot(p43, p21);
        float d1321 = dot(p13, p21);
        float d4343 = dot(p43, p43);
        float d2121 = dot(p21, p21);

        float denom = d2121 * d4343 - d4321 * d4321;
        if (Math.abs(denom) < 1.192093E-07F)
            return false;
        float numer = d1343 * d4321 - d1321 * d4343;

        st[0] = (numer / denom);
        st[1] = ((d1343 + d4321 * st[0]) / d4343);

        return true;
    }

    public static float length(Point v)
    {
        return (float) Math.sqrt(dot(v, v));
    }

    public static float lengthsq(Point v)
    {
        return dot(v, v);
    }

    public static float distancesq(Point v1, Point v2)
    {
        return lengthsq(sub(v1, v2));
    }

    public static boolean near(Point v1, Point v2, float distance)
    {
        return distancesq(v1, v2) < distance * distance;
    }

    public static float distance(Point v1, Point v2)
    {
        return length(sub(v1, v2));
    }

    public static Point normalize(Point v)
    {
        return mul(v, 1.0F / length(v));
    }

    public static Point toAngle(float a)
    {
        return make((float) Math.cos(a), (float) Math.sin(a));
    }

    public static float toRadian(Point v)
    {
        return (float) Math.atan2(v.y, v.x);
    }
}
