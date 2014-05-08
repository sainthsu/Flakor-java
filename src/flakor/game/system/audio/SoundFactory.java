package flakor.game.system.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SoundFactory
{
	// ===========================================================
	// Fields
	// ===========================================================

	private static String assetBasePath = "";


	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param assetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	*/
	public static void setAssetBasePath(final String assetBasePath)
	{
		if(assetBasePath.endsWith("/") || assetBasePath.length() == 0)
		{
			SoundFactory.assetBasePath = assetBasePath;
		}
		else
		{
			throw new IllegalStateException("AssetBasePath must end with '/' or be lenght zero.");
		}
	}

	public static String getAssetBasePath() 
	{
		return SoundFactory.assetBasePath;
	}

	public static void onCreate()
	{
		SoundFactory.setAssetBasePath("");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static Sound createSoundFromPath(final SoundManager soundManager, final String path)
	{
			synchronized(soundManager)
			{
				final int soundID = soundManager.getSoundPool().load(path, 1);
				final Sound sound = new Sound(soundManager, soundID);
				soundManager.add(sound);
				return sound;
			}
	}

	public static Sound createSoundFromAsset(final SoundManager soundManager, final Context context, final String assetPath) throws IOException
	{
			synchronized(soundManager)
			{
				final int soundID = soundManager.getSoundPool().load(context.getAssets().openFd(SoundFactory.assetBasePath + assetPath), 1);
				final Sound sound = new Sound(soundManager, soundID);
				soundManager.add(sound);
				return sound;
			}
	}

	public static Sound createSoundFromResource(final SoundManager pSoundManager, final Context pContext, final int pSoundResID) {
			synchronized(pSoundManager) {
				final int soundID = pSoundManager.getSoundPool().load(pContext, pSoundResID, 1);
				final Sound sound = new Sound(pSoundManager, soundID);
				pSoundManager.add(sound);
				return sound;
			}
	}

	public static Sound createSoundFromFile(final SoundManager pSoundManager, final File pFile)
	{
		return SoundFactory.createSoundFromPath(pSoundManager, pFile.getAbsolutePath());
	}

	public static Sound createSoundFromAssetFileDescriptor(final SoundManager pSoundManager, final AssetFileDescriptor pAssetFileDescriptor)
	{
			synchronized(pSoundManager)
			{
				final int soundID = pSoundManager.getSoundPool().load(pAssetFileDescriptor, 1);
				final Sound sound = new Sound(pSoundManager, soundID);
				pSoundManager.add(sound);
				return sound;
			}
	}

	public static Sound createSoundFromFileDescriptor(final SoundManager pSoundManager, final FileDescriptor pFileDescriptor, final long pOffset, final long pLength)
	{
		synchronized(pSoundManager)
		{
				final int soundID = pSoundManager.getSoundPool().load(pFileDescriptor, pOffset, pLength, 1);
				final Sound sound = new Sound(pSoundManager, soundID);
				pSoundManager.add(sound);
				return sound;
		}
	}
}
