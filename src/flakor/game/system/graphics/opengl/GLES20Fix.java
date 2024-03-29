package flakor.game.system.graphics.opengl;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.os.Build;

import flakor.game.support.util.SystemUtils;
import flakor.game.tool.FlakorRuntimeException;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class GLES20Fix
{
    // ===========================================================
    // Constants
    // ===========================================================

    private static boolean NATIVE_LIB_LOADED;

    /** Android issue 8931. */
    private static final boolean WORKAROUND_MISSING_GLES20_METHODS;

    static {
        boolean loadLibrarySuccess;
        try
        {
            System.loadLibrary("flakor");
            loadLibrarySuccess = true;
        }
        catch (final UnsatisfiedLinkError e)
        {
            loadLibrarySuccess = false;
        }
        NATIVE_LIB_LOADED = loadLibrarySuccess;

        if(SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO))
        {
            if(loadLibrarySuccess)
            {
                WORKAROUND_MISSING_GLES20_METHODS = true;
            }
            else
            {
                throw new FlakorRuntimeException("Inherently incompatible device detected.");
            }
        } 
        else
        {
            WORKAROUND_MISSING_GLES20_METHODS = false;
        }
    }

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    private GLES20Fix() {

    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static native void glVertexAttribPointer(final int pIndex, final int pSize, final int pType, final boolean pNormalized, final int pStride, final int pOffset);
    public static native void glDrawElements(final int pMode, final int pCount, final int pType, final int pOffset);

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void glVertexAttribPointerFix(final int pIndex, final int pSize, final int pType, final boolean pNormalized, final int pStride, final int pOffset)
    {
        if(GLES20Fix.WORKAROUND_MISSING_GLES20_METHODS)
        {
            GLES20Fix.glVertexAttribPointerFix(pIndex, pSize, pType, pNormalized, pStride, pOffset);
        } else {
            GLES20.glVertexAttribPointer(pIndex, pSize, pType, pNormalized, pStride, pOffset);
        }
    }

    public static void glDrawElementsFix(final int pMode, final int pCount, final int pType, final int pOffset)
    {
        if(GLES20Fix.WORKAROUND_MISSING_GLES20_METHODS)
        {
            GLES20Fix.glDrawElements(pMode, pCount, pType, pOffset);
        } 
        else
        {
            GLES20.glDrawElements(pMode, pCount, pType, pOffset);
        }
    }

}
