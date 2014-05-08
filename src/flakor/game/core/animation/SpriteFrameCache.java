/*
 * SpriteFrameCache.java
 * Created on 9/4/13 8:10 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.animation;

/**
 * Created by saint on 9/4/13.
 */
public class SpriteFrameCache
{
    /*
    private static SpriteFrameCache cache = new SpriteFrameCache();

    //SpriteFrame的Hashtable
    private Hashtable<String, SpriteFrame> spriteFrames = new Hashtable();

    public static SpriteFrameCache getInstance()
    {
        return cache;
    }
    //从字符串中得到一个矩形，精灵的尺寸
    private Rect rectFromString(String value)
    {
        try
        {
            value = value.replaceAll("\\{", "").replaceAll("\\}", "");
            String[] items = value.split(",");
            float x = ResolutionIndependent.resolve(Float.parseFloat(items[0]));
            float y = ResolutionIndependent.resolve(Float.parseFloat(items[1]));
            float w = ResolutionIndependent.resolve(Float.parseFloat(items[2]));
            float h = ResolutionIndependent.resolve(Float.parseFloat(items[3]));
            return new Rect(x, y, w, h);
        } catch (Exception e)
        {
        }
        return new Rect(0.0F, 0.0F, 0.0F, 0.0F);
    }
    //取得点
    private Point pointFromString(String value)
    {
        value = value.replaceAll("\\{", "").replaceAll("\\}", "");
        String[] items = value.split(",");
        float x = ResolutionIndependent.resolve(Float.parseFloat(items[0]));
        float y = ResolutionIndependent.resolve(Float.parseFloat(items[1]));
        return Point.make(x, y);
    }
    //取得尺寸
    private Size sizeFromString(String value)
    {
        value = value.replaceAll("\\{", "").replaceAll("\\}", "");
        String[] items = value.split(",");
        float w = ResolutionIndependent.resolve(Float.parseFloat(items[0]));
        float h = ResolutionIndependent.resolve(Float.parseFloat(items[1]));
        return Size.make(w, h);
    }
    //向SpriteFrames的哈希表中添加一个元素，即添加一个SpriteFrames
    public void addSpriteFramesWithDictionary(Hashtable dictionary)
    {
        Hashtable metadataDict = (Hashtable) dictionary.get("metadata");
        Hashtable framesDict = (Hashtable) dictionary.get("frames");
        int format = 0;
        if (metadataDict != null) {
            Integer value = (Integer) metadataDict.get("format");
            format = value.intValue();
        }
        if ((format < 0) || (format > 1)) {
            return;
        }
        Enumeration keys = framesDict.keys();
        while (keys.hasMoreElements())
        {
            String frameDictKey = (String) keys.nextElement();
            SpriteFrame spriteFrame = null;
            if (format == 0)
            {
                Hashtable frameDict = (Hashtable) framesDict.get(frameDictKey);
                float x = ResolutionIndependent.resolve(((Float) frameDict
                        .get("x")).floatValue());
                float y = ResolutionIndependent.resolve(((Float) frameDict
                        .get("y")).floatValue());
                float w = ResolutionIndependent.resolve(((Float) frameDict
                        .get("width")).floatValue());
                float h = ResolutionIndependent.resolve(((Float) frameDict
                        .get("height")).floatValue());
                float ox = ResolutionIndependent.resolve(((Float) frameDict
                        .get("offsetX")).floatValue());
                float oy = ResolutionIndependent.resolve(((Float) frameDict
                        .get("offsetY")).floatValue());
                float ow = ResolutionIndependent.resolve(((Float) frameDict
                        .get("originalWidth")).floatValue());
                float oh = ResolutionIndependent.resolve(((Float) frameDict
                        .get("originalHeight")).floatValue());
                ow = Math.abs(ow);
                oh = Math.abs(oh);
                spriteFrame = new SpriteFrame(new Rect(x, y, w, h),
                        Point.make(ox, oy), Size.make(ow, oh));
            }
            else if (format == 1)
            {
                Hashtable frameDict = (Hashtable) framesDict.get(frameDictKey);
                Rect frame = rectFromString((String) frameDict.get("frame"));
                Point offset = pointFromString((String) frameDict
                        .get("offset"));
                Size sourceSize = sizeFromString((String) frameDict
                        .get("sourceSize"));
                spriteFrame = new SpriteFrame(frame, offset, sourceSize);
            }
            this.spriteFrames.put(frameDictKey, spriteFrame);
        }
    }
    //从一个资源中添加一个SpriteFrame
    public void addSpriteFrameFromRes(Context context,int resId)
    {
        PListLoader loader = new PListLoader();
        Hashtable dict = (Hashtable) loader.loadFromResId(context, resId);
        addSpriteFramesWithDictionary(dict);
    }
    //得到精灵的Rect
    public Rect getFrameRect(String frameName)
    {
        SpriteFrame f = (SpriteFrame) this.spriteFrames.get(frameName);
        return (f == null) ? Rect.makeZero() : (Rect) f.getFrame().copy();
    }
    //构建一个AtlasSprite，大块图片中的一个小块
    public AtlasSprite makeAtlasSprite(String frameName, TextureAtlas atlas)
    {
        SpriteFrame frame = (SpriteFrame) this.spriteFrames.get(frameName);
        if (frame != null)
        {
            AtlasSprite sprite = AtlasSprite.make(frame.getFrame(), atlas);
            return sprite;
        }
        return null;
    }
    */
}
