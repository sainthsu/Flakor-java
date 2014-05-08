/*
 * PListLoader.java
 * Created on 9/4/13 7:57 PM
 *
 * ver0.0.1beta 9/4/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.support;

import android.content.Context;
import android.util.Log;
import android.view.InflateException;

import flakor.game.support.math.MathUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Created by saint on 9/4/13.
 * <p>plist xml file loader
 */
public class PListLoader
{
    /**
     * 节点的栈
     */
    private Stack<Item> sNodeStack = new Stack<Item>();
    private static void skipUnknownTag(XmlPullParser parser) throws Exception
    {
        String tag = parser.getName();
        while (parser.next() > 0)
            if ((parser.getEventType() == 3) && (parser.getName().equals(tag)))
                return;
    }

    /**
     * 从res\raw中读取一个plist资源文件
     */
    public Object loadFromResId(Context context, int resId)
    {
        Item result = null;
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance()
                    .newPullParser();
            String body = MathUtils.getUTF8String(MathUtils.dataOfRawResource(
                    context, resId));
            parser.setInput(new StringReader(body));
            parser.nextTag();
            parser.nextTag();
            result = parseNode(context, parser);
            if (result == null) {
                throw new InflateException("failed to inflate");
            }
            inflate(context, parser, result);
        }
        catch (Exception e)
        {
            Log.d("Flakor Engine", "failed to parse xml raw resource");
        }
        return result.node;
    }
    //解析核心
    private void inflate(Context context, XmlPullParser parser, Item parent)
            throws Exception {
        while (parser.nextTag() == 2) {
            String nodeName1 = parser.getName();
            parser.require(2, "", nodeName1);
            Item node = parseNode(context, parser);
            if (node != null) {
                if (node.node != null) {
                    if (parent.node instanceof ArrayList)
                    {
                        ArrayList array = (ArrayList) parent.node;
                        array.add(node.node);
                    }
                    else if (parent.node instanceof Hashtable)
                    {
                        String key = ((Item) this.sNodeStack.lastElement()).value;
                        Hashtable dict = (Hashtable) parent.node;
                        dict.put(key, node.node);
                        this.sNodeStack.pop();
                    }
                }

                if (node.name.equals("dict")) {
                    inflate(context, parser, node);
                } else if (node.name.equals("array")) {
                    inflate(context, parser, node);
                }

            }
            parser.require(3, "", nodeName1);
        }
    }
    //解析每个节点，即对应的dict、array...
    private Item parseNode(Context context, XmlPullParser parser)   throws Exception
    {
        String nodeName = parser.getName();
        Item ret = new Item(nodeName, null);
        if ("dict".equals(nodeName)) {
            ret.node = new Hashtable();
        } else if ("array".equals(nodeName)) {
            ret.node = new ArrayList();
        } else if ("key".equals(nodeName)) {
            ret.value = parser.nextText();
            this.sNodeStack.push(ret);
        } else if ("string".equals(nodeName)) {
            ret.value = parser.nextText();
            ret.node = ret.value;
        } else if ("integer".equals(nodeName)) {
            ret.value = parser.nextText();
            ret.node = new Integer(ret.value);
        }
        else
        {
            skipUnknownTag(parser);
            return null;
        }
        return ret;
    }

    //文件对应的某个Item
    class Item
    {
        //键
        public String name;
        //值
        public String value;
        //节点,,dict
        public Object node;
        public Item(String name, String value)
        {
            this.name = name;
            this.value = value;
        }
    }
}
