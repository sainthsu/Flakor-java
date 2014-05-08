package flakor.game.system.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**************************************************
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.8.18:00
 * @copyright Saint Hsu
 *************************************************/

public class MusicFactory
{
	private static String assetBasePath = "";
	public static MusicManager musicManager = new MusicManager();
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public static void onCreate()
	{
		MusicFactory.setAssetBasePath("");
	}
	
	public static void setAssetBasePath(final String fAssetBasePath)
	{
		if(fAssetBasePath.endsWith("/") || fAssetBasePath.length() == 0)
		{
			MusicFactory.assetBasePath = fAssetBasePath;
		}
		else
		{
			throw new IllegalStateException("assetBasePath must end with '/' or be lenght zero.");
		}
	}

	public static String getAssetBasePath()
	{
		return MusicFactory.assetBasePath;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public static Music createMusic(final File musicfile) throws FileNotFoundException, IOException
	{
		final MediaPlayer mediaPlayer = new MediaPlayer();
		FileInputStream fileInput = new FileInputStream(musicfile);
		FileDescriptor fd = fileInput.getFD();
		mediaPlayer.setDataSource(fd);
		mediaPlayer.prepare();
		
		Music music = new Music(musicManager, mediaPlayer);
		musicManager.add(music);
		fileInput.close();
		return music;
	}
	
	public static Music createMusic(final Context context, final String assetPath) throws IOException
	{
		final MediaPlayer mediaPlayer = new MediaPlayer();
		
		final AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(MusicFactory.assetBasePath+assetPath); 
		mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
		mediaPlayer.prepare();
		
		Music music = new Music(musicManager, mediaPlayer);
		musicManager.add(music);
		
		return music;
	}
	
	public static Music createMusic(final Context context, final int musicResouceId) throws IOException
	{
		final MediaPlayer mediaPlayer = MediaPlayer.create(context, musicResouceId);
		mediaPlayer.prepare();
		
		Music music = new Music(musicManager, mediaPlayer);
		musicManager.add(music);
		
		return music;
	}
	
	public static Music createMusic(final Context context, final AssetFileDescriptor assetFileDescriptor) throws IOException
	{
		final MediaPlayer mediaPlayer = new MediaPlayer();
		
		mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
		mediaPlayer.prepare();
		
		Music music = new Music(musicManager, mediaPlayer);
		musicManager.add(music);
		
		return music;
	}
}
