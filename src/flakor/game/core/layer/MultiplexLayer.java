/*
 * MultiplexLayer.java
 * Created on 9/4/13 11:53 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.layer;

import flakor.game.core.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by saint on 9/4/13.
 */
public class MultiplexLayer extends Layer
{
    //图层列表
    private ArrayList<Layer> mLayers;
    //当然显示的图层索引
    private int mEnabledLayer;
    public static MultiplexLayer make(Layer[] params)
    {
        return new MultiplexLayer(params);
    }

    public MultiplexLayer()
    {
    }
    //参数是一系列图层的一个数组
    protected MultiplexLayer(Layer[] params)
    {
        this.mLayers = new ArrayList<Layer>();
        this.mLayers.addAll(Arrays.asList(params));
        if (this.mLayers.isEmpty())
        {
            this.mEnabledLayer = -1;
        }
        else
        {
            this.mEnabledLayer = 0;
            attachChild((Entity) this.mLayers.get(this.mEnabledLayer));
        }
    }
    //添加图层
    public void addLayer(Layer layer)
    {
        this.mLayers.add(layer);
        if (this.mEnabledLayer == -1)
        {
            this.mEnabledLayer = 0;
            attachChild(layer);
        }
    }

    //索引
    public int getCurrentLayerIndex()
    {
        return this.mEnabledLayer;
    }

    //选择指定索引的图层
    public void switchTo(int n)
    {
        if (n < 0) {
            return;
        }
        n %= this.mLayers.size();
        detachChild((Entity) this.mLayers.get(this.mEnabledLayer));
        this.mEnabledLayer = n;
        attachChild((Entity) this.mLayers.get(this.mEnabledLayer));
    }
}
