/*
 * EntityCamera.java
 * Created on 9/5/13 12:35 AM
 *
 * ver0.0.1beta 9/5/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.camera;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by saint on 9/5/13.
 * 实体相机，用于渲染实体元素，实现类3D效果，目前未启用
 */
public class EntityCamera
{
    //眼睛位置
    private float eyeX;
    private float eyeY;
    private float eyeZ;
    //观察点位置
    private float centerX;
    private float centerY;
    private float centerZ;
    //观察方向
    private float upX;
    private float upY;
    private float upZ;
    //是否需要更新
    private boolean dirty;
    private static Camera camera;

    public boolean isDirty()
    {
        return this.dirty;
    }

    public void setDirty(boolean value)
    {
        this.dirty = value;
    }

    public EntityCamera(Camera camera)
    {
        EntityCamera.camera = camera;
        restore();
    }

    //重置摄像头
    public void restore()
    {
        float halfW = camera.getWidth() / 2.0F;
        float halfH = camera.getHeight() / 2.0F;
        this.eyeX = halfW;
        this.eyeY = halfH;
        this.eyeZ = getZEye();
        this.centerX = halfW;
        this.centerY = halfH;
        this.centerZ = 0.0F;
        this.upX = 0.0F;
        this.upY = 1.0F;
        this.upZ = 0.0F;
        this.dirty = false;
    }

    public void locate(GL10 gl)
    {
        if (this.dirty)
        {
            //更新摄像头
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, this.eyeX, this.eyeY, this.eyeZ,
                    this.centerX, this.centerY, this.centerZ, this.upX,
                    this.upY, this.upZ);
        }
    }

    public static float getZEye()
    {
        return camera.getHeight() / 1.1547F;
    }

    public void setEye(float x, float y, float z)
    {
        this.eyeX = x;
        this.eyeY = y;
        this.eyeZ = z;
        this.dirty = true;
    }

    public void setCenter(float x, float y, float z)
    {
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
        this.dirty = true;
    }

    public void setUp(float x, float y, float z)
    {
        this.upX = x;
        this.upY = y;
        this.upZ = z;
        this.dirty = true;
    }

    public void getEye(float[] v)
    {
        v[0] = this.eyeX;
        v[1] = this.eyeY;
        v[2] = this.eyeZ;
    }

    public void getCenter(float[] v)
    {
        v[0] = this.centerX;
        v[1] = this.centerY;
        v[2] = this.centerZ;
    }

    public void getUp(float[] v)
    {
        v[0] = this.upX;
        v[1] = this.upY;
        v[2] = this.upZ;
    }

    public String toString()
    {
        return String.format(
                "<%s = %08X | center = (%.2f,%.2f,%.2f)>",
                new Object[] { super.getClass(), this,
                        Float.valueOf(this.centerX),
                        Float.valueOf(this.centerY),
                        Float.valueOf(this.centerZ) });
    }
}
