package flakor.game.support.math;

import android.content.Context;
import android.util.FloatMath;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class MathUtils
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final Random RANDOM = new Random(System.nanoTime());

    // ===========================================================
    // Methods
    // ===========================================================

    public static final float atan2(final float dY, final float dX) {
        return (float)Math.atan2(dY, dX);
    }

    public static final float radToDeg(final float pRad) {
        return MathConstants.RAD_TO_DEG * pRad;
    }

    public static final float degToRad(final float pDegree) {
        return MathConstants.DEG_TO_RAD * pDegree;
    }

    public static final int signum(final int n) {
        if(n == 0) {
            return 0;
        } else if(n > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static final int randomSign() {
        if(RANDOM.nextBoolean()) {
            return 1;
        } else {
            return -1;
        }
    }

   //产生指定的随机数
    public static float randMinusOneToOne()
    {  
       return (float) Math.random() * 2.0F - 1.0F;  
    }
    
    public static float randZeroToOne()
    {  
       return (float) Math.random();  
    }  
    
    public static int rand(int max)
    {  
        return new Random().nextInt(max);  
    }  
    
    public static final float random(final float pMin, final float pMax) {
        return pMin + RANDOM.nextFloat() * (pMax - pMin);
    }

    /**
     * @param pMin inclusive!
     * @param pMax inclusive!
     * @return
     */
    public static final int random(final int pMin, final int pMax) {
        return pMin + RANDOM.nextInt(pMax - pMin + 1);
    }

    public static final boolean isPowerOfTwo(final int n) 
    {
        return ((n != 0) && (n & (n - 1)) == 0);
    }

    public static final int nextPowerOfTwo(final float f)
    {
        return MathUtils.nextPowerOfTwo((int)(FloatMath.ceil(f)));
    }

    public static final int nextPowerOfTwo(final int n)
    {
        int k = n;

        if (k == 0) {
            return 1;
        }

        k--;

        for (int i = 1; i < 32; i <<= 1) {
            k = k | k >> i;
        }

        return k + 1;
    }

    public static final int sum(final int[] pValues)
    {
        int sum = 0;
        for(int i = pValues.length - 1; i >= 0; i--) {
            sum += pValues[i];
        }

        return sum;
    }

    public static final void arraySumInternal(final int[] pValues)
    {
        final int valueCount = pValues.length;
        for(int i = 1; i < valueCount; i++) {
            pValues[i] = pValues[i-1] + pValues[i];
        }
    }

    public static final void arraySumInternal(final long[] pValues)
    {
        final int valueCount = pValues.length;
        for(int i = 1; i < valueCount; i++) {
            pValues[i] = pValues[i-1] + pValues[i];
        }
    }

    public static final void arraySumInternal(final long[] pValues, final long pFactor)
    {
        pValues[0] = pValues[0] * pFactor;
        final int valueCount = pValues.length;
        for(int i = 1; i < valueCount; i++) {
            pValues[i] = pValues[i-1] + pValues[i] * pFactor;
        }
    }

    public static final void arraySumInto(final long[] pValues, final long[] pTargetValues, final long pFactor)
    {
        pTargetValues[0] = pValues[0] * pFactor;
        final int valueCount = pValues.length;
        for(int i = 1; i < valueCount; i++) {
            pTargetValues[i] = pTargetValues[i-1] + pValues[i] * pFactor;
        }
    }

    public static final float arraySum(final float[] pValues) {
        float sum = 0;
        final int valueCount = pValues.length;
        for(int i = 0; i < valueCount; i++) {
            sum += pValues[i];
        }
        return sum;
    }

    public static final float arrayAverage(final float[] pValues) {
        return MathUtils.arraySum(pValues) / pValues.length;
    }

    public static float[] rotateAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY) {
        if(pRotation != 0) {
            final float rotationRad = MathUtils.degToRad(pRotation);
            final float sinRotationRad = FloatMath.sin(rotationRad);
            final float cosRotationInRad = FloatMath.cos(rotationRad);

            for(int i = pVertices.length - 2; i >= 0; i -= 2) {
                final float pX = pVertices[i];
                final float pY = pVertices[i + 1];
                pVertices[i] = pRotationCenterX + (cosRotationInRad * (pX - pRotationCenterX) - sinRotationRad * (pY - pRotationCenterY));
                pVertices[i + 1] = pRotationCenterY + (sinRotationRad * (pX - pRotationCenterX) + cosRotationInRad * (pY - pRotationCenterY));
            }
        }
        return pVertices;
    }

    public static float[] scaleAroundCenter(final float[] pVertices, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
        if(pScaleX != 1 || pScaleY != 1) {
            for(int i = pVertices.length - 2; i >= 0; i -= 2) {
                pVertices[i] = pScaleCenterX + (pVertices[i] - pScaleCenterX) * pScaleX;
                pVertices[i + 1] = pScaleCenterY + (pVertices[i + 1] - pScaleCenterY) * pScaleY;
            }
        }

        return pVertices;
    }

    public static float[] rotateAndScaleAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
        MathUtils.rotateAroundCenter(pVertices, pRotation, pRotationCenterX, pRotationCenterY);
        return MathUtils.scaleAroundCenter(pVertices, pScaleX, pScaleY, pScaleCenterX, pScaleCenterY);
    }

    public static float[] revertScaleAroundCenter(final float[] pVertices, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
        return MathUtils.scaleAroundCenter(pVertices, 1 / pScaleX, 1 / pScaleY, pScaleCenterX, pScaleCenterY);
    }

    public static float[] revertRotateAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY) {
        return MathUtils.rotateAroundCenter(pVertices, -pRotation, pRotationCenterX, pRotationCenterY);
    }

    public static float[] revertRotateAndScaleAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
        MathUtils.revertScaleAroundCenter(pVertices, pScaleX, pScaleY, pScaleCenterX, pScaleCenterY);
        return MathUtils.revertRotateAroundCenter(pVertices, pRotation, pRotationCenterX, pRotationCenterY);
    }

    public static final boolean isInBounds(final int pMinValue, final int pMaxValue, final int pValue) {
        return pValue >= pMinValue && pValue <= pMaxValue;
    }

    public static final boolean isInBounds(final float pMinValue, final float pMaxValue, final float pValue) {
        return pValue >= pMinValue && pValue <= pMaxValue;
    }

    /**
     * @param pMinValue inclusive!
     * @param pMaxValue inclusive!
     * @param pValue
     * @return
     */
    public static final int bringToBounds(final int pMinValue, final int pMaxValue, final int pValue) {
        return Math.max(pMinValue, Math.min(pMaxValue, pValue));
    }

    /**
     * @param pMinValue inclusive!
     * @param pMaxValue inclusive!
     * @param pValue
     * @return
     */
    public static final float bringToBounds(final float pMinValue, final float pMaxValue, final float pValue) {
        return Math.max(pMinValue, Math.min(pMaxValue, pValue));
    }

    /**
     * @return the euclidean distance between the points (pX1, pY1) and (pX2, pY2).
     */
    public static final float distance(final float pX1, final float pY1, final float pX2, final float pY2){
        final float dX = pX2 - pX1;
        final float dY = pY2 - pY1;
        return FloatMath.sqrt((dX * dX) + (dY * dY));
    }

    /**
     * @return the euclidean distance between the origin (0, 0) and (pX, pY).
     */
    public static final float length(final float pX, final float pY)
    {
        return FloatMath.sqrt((pX * pX) + (pY * pY));
    }

    /**
     * @param pX
     * @param pY
     * @param pMix [0...1]
     * @return pX * (1 - pMix) + pY * pMix
     */
    public static final float mix(final float pX, final float pY, final float pMix)
    {
        return pX * (1 - pMix) + pY * pMix;
    }

    /**
     * @param pX
     * @param pY
     * @param pMix [0...1]
     * @return (int)Math.round(pX * (1 - pMix) + pY * pMix)
     */
    public static final int mix(final int pX, final int pY, final float pMix)
    {
        return Math.round(pX * (1 - pMix) + pY * pMix);
    }

    public static final boolean isEven(final int n)
    {
        return n % 2 == 0;
    }

    public static final boolean isOdd(final int n)
    {
        return n % 2 == 1;
    }

    public static float dot(final float pXA, final float pYA, final float pXB, final float pYB)
    {
        return pXA * pXB + pYA * pYB;
    }

    public static float cross(final float pXA, final float pYA, final float pXB, final float pYB)
    {
        return pXA * pYB - pXB * pYA;
    }

    /**
     * 将byte数组转换成UTF8字符串
     */
    public static String getUTF8String(byte[] b)
    {
       if (b == null)  
           return "";  
       return getUTF8String(b, 0, b.length);  
    }

    public static String getUTF8String(byte[] b, int start, int length)
    {
       if (b == null)  
           return "";  
       try
       {
           return new String(b, start, length, "UTF-8");  
       }
       catch (UnsupportedEncodingException e)
       {
           e.printStackTrace();
       }  
       return "";  
    }

    //从Raw中读取一个资源文件转换为byte数组
    public static byte[] dataOfRawResource(Context context, int id)
    {
        InputStream in = null;
        byte[] bytes = (byte[]) null;
        try
        {
            in = context.getResources().openRawResource(id);
            byte[] buf = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (int i = 0; i != -1; i = in.read(buf))
            {
                out.write(buf, 0, i);
            }
            bytes = out.toByteArray();
            out.flush();
            out.close();
        }
        catch (Throwable e)
        {
            Log.e("Flakor Engine", "Can't read the raw resource: " + id);
        }
        finally
        {
            try
            {
                if (in != null)
                    in.close();
            }
            catch (IOException localIOException1)
            {
            }
        }
        return bytes;
    }
    /*指数
    public static int getClosest2Exp(int num) {  
       if ((num != 1) && ((num & num - 1) != 0)) {  
           int i = 1;  
           while (i < num)  
              i *= 2;  
           num = i;  
       }  
       return num;  
    }  
    //计算文字的尺寸  
    public static YFSSize calculateTextSize(String text, String fontname,  
           float fontSize) {  
       Typeface typeface = Typeface.create(fontname, 0);  
       Paint paint = new Paint();  
       paint.setTypeface(typeface);  
       paint.setTextSize(fontSize);  
       paint.setAntiAlias(true);  
       int ascent = (int) Math.ceil(-paint.ascent());  
       int descent = (int) Math.ceil(paint.descent());  
       int measuredTextWidth = (int) Math.ceil(paint.measureText(text));  
       return YFSSize.make(measuredTextWidth, ascent + descent);  
    }  
    //创建一个文字Label转换为Bitmap  
    public static Bitmap createLabelBitmap(String text, YFSSize dimensions,  
           Label.TextAlignment alignment, String fontname, float fontSize) {  
       Typeface typeface = Typeface.create(fontname, 0);  
       Paint paint = new Paint();  
       paint.setTypeface(typeface);  
       paint.setTextSize(fontSize);  
       paint.setAntiAlias(true);  
       paint.setColor(-1);  
       int ascent = 0;  
       int descent = 0;  
       int measuredTextWidth = 0;  
       ascent = (int) Math.ceil(-paint.ascent());  
       descent = (int) Math.ceil(paint.descent());  
       measuredTextWidth = (int) Math.ceil(paint.measureText(text));  
       int textWidth = measuredTextWidth;  
       int textHeight = ascent + descent;  
       if ((dimensions.width == 0.0F) || (dimensions.height == 0.0F)) {  
           YFSSize size = calculateTextSize(text, fontname, fontSize);  
           dimensions.width = size.width;  
           dimensions.height = size.height;  
       }  
       int width = getClosest2Exp((int) dimensions.width);  
       int height = getClosest2Exp((int) dimensions.height);  
       Bitmap.Config config = Bitmap.Config.ARGB_8888;  
       Bitmap bitmap = Bitmap.createBitmap(width, height, config);  
       Canvas canvas = new Canvas(bitmap);  
       bitmap.eraseColor(0);  
       int y = ascent + ((int) dimensions.height - textHeight) / 2;  
       int x = 0;  
       switch (alignment) {  
       case RIGHT:  
           x = (int) dimensions.width - textWidth;  
           break;  
       case LEFT:  
           x = ((int) dimensions.width - textWidth) / 2;  
       }  
       canvas.drawText(text, x, y, paint);  
       return bitmap;  
    }  
    //argb转换为rgba格式  
    public static ByteBuffer argb2rgba(Bitmap bmp) {  
       ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight()  
              * bmp.getWidth() * 4);  
       bb.order(ByteOrder.BIG_ENDIAN);  
       IntBuffer ib = bb.asIntBuffer();  
       for (int y = bmp.getHeight() - 1; y > -1; --y) {  
           for (int x = 0; x < bmp.getWidth(); ++x) {  
              int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);  
              int alpha = pix >> 24 & 0xFF;  
              int red = pix >> 16 & 0xFF;  
              int green = pix >> 8 & 0xFF;  
              int blue = pix & 0xFF;  
              ib.put(red << 24 | green << 16 | blue << 8 | alpha);  
           }  
       }  
       bb.position(0);  
       return bb;  
    }  
    //交换数据  
    public static void swap(float[] v, int index1, int index2) {  
       float tmp = v[index1];  
       v[index1] = v[index2];  
       v[index2] = tmp;  
    }
    */

}
