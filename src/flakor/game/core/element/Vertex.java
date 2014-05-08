/*
 * Vertex.java
 * Created on 9/4/13 8:34 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.element;

/**
 * Created by saint on 9/4/13.
 * a 3D point
 * 三维顶点。包括x,y,z
 */
public class Vertex
{
    public static final int GL_SIZE = 12;
    public float x;
    public float y;
    public float z;

    public static Vertex makeZero()
    {
        return new Vertex(0.0F, 0.0F, 0.0F);
    }

    public Vertex(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString()
    {
        return "<" + this.x + ", " + this.y + ", " + this.z + ">";
    }

    /**
     * convert Vertex to Point ignores the z axis
     * @return Point
     */
    public Point toPoint()
    {
        return Point.make(this.x,this.y);
    }
}
