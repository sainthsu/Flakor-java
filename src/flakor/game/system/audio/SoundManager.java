package flakor.game.system.audio;


import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseArray;

public class SoundManager extends BaseAudioManager<Sound> implements OnLoadCompleteListener
{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SOUND_STATUS_OK = 0;

	public static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;

	// ===========================================================
	// Fields
	// ===========================================================

	private final SoundPool soundPool;
	private final SparseArray<Sound> soundMap = new SparseArray<Sound>();
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public SoundManager()
	{
		this(MAX_SIMULTANEOUS_STREAMS_DEFAULT);
	}
	
	public SoundManager(final int maxSimultaneousStream)
	{
		soundPool = new SoundPool(maxSimultaneousStream, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	SoundPool getSoundPool()
	{
		return this.soundPool;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	
	
	@Override
	public void onLoadComplete(final SoundPool soundPool,final int soundID,final int status)
	{
		// TODO Auto-generated method stub
		if (status == SoundManager.SOUND_STATUS_OK)
		{
			final Sound sound = soundMap.get(soundID);
			if(sound == null)
			{
				throw new SoundException("Unexpected soundID: '" + soundID + "'.");
			}
			else
			{
				sound.setLoaded(true);
			}
		}
	}

	@Override
	public void add(final Sound sound)
	{
		// TODO Auto-generated method stub
		super.add(sound);
		
		this.soundMap.put(sound.getSoundId(), sound);
	}

	@Override
	public boolean remove(final Sound sound)
	{
		// TODO Auto-generated method stub
		final boolean removed = super.remove(sound);
		if(removed)
		{
			this.soundMap.delete(sound.getSoundId());
		}
		return removed;
	}

	@Override
	public void releaseAll()
	{
		// TODO Auto-generated method stub
		super.releaseAll();
		
		this.soundPool.release();
	}
}
