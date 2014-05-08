package flakor.game.support;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TGALoader
{
	 //装载tga文件头  
    private static void loadHeader(InputStream f, TGA info) throws IOException
    {
       f.skip(2L);  
       info.type = (byte) f.read();  
       f.skip(9L);  
       info.width = (f.read() & 0xFF | (f.read() & 0xFF) << 8);  
       info.height = (f.read() & 0xFF | (f.read() & 0xFF) << 8);  
       info.pixelDepth = (f.read() & 0xFF);  
       info.bytesPerPixel = (info.pixelDepth / 8);  
       int garbage = f.read();  
       info.flipped = false;  
       if ((garbage & 0x20) != 0)  
           info.flipped = true;  
    }  
    //装载图片数据  
    private static void loadImageData(InputStream f, TGA info)  
           throws IOException
    {
       int total = info.height * info.width * info.bytesPerPixel;  
       f.read(info.imageData, 0, total);  
       if (info.bytesPerPixel >= 3)  
           for (int i = 0; i < total; i += info.bytesPerPixel) {  
              byte aux = info.imageData[i];  
              info.imageData[i] = info.imageData[(i + 2)];  
              info.imageData[(i + 2)] = aux;  
           }  
    }  
    //装载RLE格式的图像数据  
    private static void loadRLEImageData(InputStream f, TGA info)  
           throws IOException {  
       int total = info.height * info.width;  
       int pixel = 0;  
       int bytes = 0;  
       int runlength = 0;  
       byte[] buf = new byte[4];  
       while (pixel < total) {  
           runlength = f.read() & 0xFF;  
           if (runlength < 128) {  
              ++runlength;  
              for (int i = 0; i < runlength; ++i) {  
                  if (f.read(buf, 0, info.bytesPerPixel) != info.bytesPerPixel) {  
                     throw new IOException("Failed to read TAGLoader file");  
                  }  
                  info.imageData[bytes] = buf[2];  
                  info.imageData[(bytes + 1)] = buf[1];  
                  info.imageData[(bytes + 2)] = buf[0];  
                  bytes += info.bytesPerPixel;  
                  ++pixel;  
                  if (pixel <= total)  
                     continue;  
                  throw new IOException("Too many pixels read");  
              }  
           } else {  
              runlength -= 127;  
              if (f.read(buf, 0, info.bytesPerPixel) != info.bytesPerPixel) {  
                  throw new IOException("Failed to read TAGLoader file");  
              }  
              for (int i = 0; i < runlength; ++i) {  
                  info.imageData[bytes] = buf[2];  
                  info.imageData[(bytes + 1)] = buf[1];  
                  info.imageData[(bytes + 2)] = buf[0];  
                  bytes += info.bytesPerPixel;  
                  ++pixel;  
                  if (pixel <= total)  
                     continue;  
                  throw new IOException("Too many pixels read");  
              }  
           }  
       }  
    }  
    //翻转图像  
    private static void flipImage(TGA info) {  
       int rowbytes = info.width * info.bytesPerPixel;  
       byte[] row = new byte[rowbytes];  
       for (int y = 0; y < info.height / 2; ++y) {  
           System.arraycopy(info.imageData, y * rowbytes, row, 0, rowbytes);  
           System.arraycopy(info.imageData,  
                  (info.height - (y + 1)) * rowbytes, info.imageData, y  
                         * rowbytes, rowbytes);  
           System.arraycopy(row, 0, info.imageData, (info.height - (y + 1))  
                  * rowbytes, rowbytes);  
       }  
       info.flipped = false;  
    }

    //装载一个TGA文件，，参数流  
    public static TGA load(InputStream is) throws IOException
    {
       TGA info = new TGA();  
       BufferedInputStream file;
       try {  
           file = new BufferedInputStream(is);  
       } catch (Exception e) {  
           info.status = TGAError.TGA_ERROR_FILE_OPEN;  
           return info;  
       }  
       try {  
           loadHeader(file, info);  
       } catch (Exception e) {  
           info.status = TGAError.TGA_ERROR_READING_FILE;  
           file.close();  
           return info;  
       }  
       if (info.type == 1) {  
           info.status = TGAError.TGA_ERROR_INDEXED_COLOR;  
           file.close();  
           return info;  
       }  
       if ((info.type != 2) && (info.type != 3) && (info.type != 10)) {  
           info.status = TGAError.TGA_ERROR_COMPRESSED_FILE;  
           file.close();  
           return info;  
       }  
       int total = info.height * info.width * info.bytesPerPixel;  
       info.imageData = new byte[total];  
       try {  
           if (info.type == 10)  
              loadRLEImageData(file, info);  
           else  
              loadImageData(file, info);  
       } catch (Exception e) {  
           info.status = TGAError.TGA_ERROR_READING_FILE;  
           file.close();  
           return info;  
       }  
       file.close();  
       info.status = TGAError.TGA_OK;  
   
       if (info.flipped) {  
           flipImage(info);  
           if (info.flipped) {  
              info.status = TGAError.TGA_ERROR_MEMORY;  
           }  
       }  
       return info;  
    }  
    //rgb转换为灰度  
    public static void rgbToGreyscale(TGA info) {  
       if (info.pixelDepth == 8) {  
           return;  
       }  
       int mode = info.pixelDepth / 8;  
       byte[] newImageData = new byte[info.height * info.width];  
       int i = 0;  
       for (int j = 0; j < info.width * info.height; ++j) {  
           newImageData[j] = (byte) (int) (0.3D * info.imageData[i] + 0.59D  
                  * info.imageData[(i + 1)] + 0.11D * info.imageData[(i + 2)]);  
   
           i += mode;  
       }  
       info.imageData = null;  
       info.pixelDepth = 8;  
       info.type = 3;  
       info.imageData = newImageData;  
    }  
    //销毁  
    public static void destroy(TGA info) {  
       if ((info == null) || (info.imageData == null))  
           return;  
       info.imageData = null;  
    }  
    //TGA文件结构  
    public static class TGA {  
       TGALoader.TGAError status;  
       int type;  
       int pixelDepth;  
       public int bytesPerPixel;  
       public int width;  
       public int height;  
       public byte[] imageData;  
       boolean flipped;  
    }  
    //错误信息  
    static enum TGAError {  
       TGA_OK, TGA_ERROR_FILE_OPEN, TGA_ERROR_READING_FILE, TGA_ERROR_INDEXED_COLOR, TGA_ERROR_MEMORY, TGA_ERROR_COMPRESSED_FILE;  
    }  
}
