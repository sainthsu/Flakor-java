package flakor.game.tool;

import android.util.Log;

public class Debug
{
	private static String TAG="Flakor Game Engine";
	protected static boolean debugMode = true;
	
	public static void setTag(String tag)
	{
		Debug.TAG = tag;
	}
	
	public static String getTag()
	{
		return TAG;
	}
	
	public static void d(String msg)
	{
		if(!debugMode) return;
		Log.d(TAG, msg);
	}

    public static void d(String msg,final Throwable throwable)
    {
        if(!debugMode) return;
        Log.d(TAG,msg,throwable);
    }

    public static void e(final Throwable throwable)
    {
        Log.e(Debug.TAG, TAG,throwable);
    }

	public static void e(String msg)
	{
		if(!debugMode) return;
		Log.e(TAG, msg);
	}

    public static void e(String msg,final Throwable throwable)
    {
        if(!debugMode) return;
        Log.e(TAG,msg,throwable);
    }

	public static void warn(String msg)
	{
		if(!debugMode) return;
		Log.w(TAG, msg);
	}
	
	public static void forceExit()
	{
		System.exit(0);
	}
}
