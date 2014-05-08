package flakor.game.system.audio;


import android.media.SoundPool;


public class Sound extends BaseAudioEntity
{
	// ===========================================================
	// Fields
	// ===========================================================
	
	private int soundID;
	private int streamID;
	private int loopCount;
	
	private boolean loaded;
	private float rate = 1.0f;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Sound(SoundManager soundManager,final int soundID)
	{
		super(soundManager);
		// TODO Auto-generated constructor stub
		this.soundID = soundID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public int getSoundId()
	{
		return this.soundID;
	}
	
	public int getStreamID() {
		return this.streamID;
	}

	public boolean isLoaded() {
		return this.loaded;
	}

	public void setLoaded(final boolean loaded)
	{
		this.loaded = loaded;
	}

	public void setLoopCount(final int loopCount) throws SoundException
	{
		this.assertNotReleased();

		this.loopCount = loopCount;
		if(this.streamID != 0)
		{
			this.getSoundPool().setLoop(this.streamID, loopCount);
		}
	}

	public float getRate()
	{
		return this.rate;
	}

	public void setRate(final float rate) throws SoundException
	{
		this.assertNotReleased();

		this.rate = rate;
		if(this.streamID != 0)
		{
			this.getSoundPool().setRate(this.streamID, rate);
		}
	}

	private SoundPool getSoundPool() throws SoundException
	{
		return this.getAudioManager().getSoundPool();
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void throwOnReleased() throws SoundException
	{
		// TODO Auto-generated method stub
		throw new SoundException();
	}

	@Override
	protected SoundManager getAudioManager() throws SoundException
	{
		// TODO Auto-generated method stub
		return (SoundManager) super.getAudioManager();
	}

	@Override
	public void play() throws AudioException
	{
		// TODO Auto-generated method stub
		super.play();
		
		final float masterVolume = this.getMasterVolume();
		final float leftVolume = this.leftVolume * masterVolume;
		final float rightVolume = this.rightVolume * masterVolume;
		
		this.streamID = this.getSoundPool().play(this.soundID, leftVolume, rightVolume, 1, this.loopCount, this.rate);
	}

	@Override
	public void pause() throws AudioException
	{
		// TODO Auto-generated method stub
		super.pause();
		if(this.streamID != 0)
		{
			this.getSoundPool().pause(this.streamID);
		}
	}

	@Override
	public void resume() throws AudioException
	{
		// TODO Auto-generated method stub
		super.resume();
		if(this.streamID != 0)
		{
			this.getSoundPool().resume(this.streamID);
		}
	}

	@Override
	public void stop() throws AudioException
	{
		// TODO Auto-generated method stub
		super.stop();
		
		if(this.streamID != 0)
		{
			this.getSoundPool().stop(this.streamID);
		}
	}

	@Override
	public void release() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();

		this.getSoundPool().unload(this.soundID);
		this.soundID = 0;
		this.loaded = false;

		this.getAudioManager().remove(this);
		
		super.release();
	}
	
	@Override
	public void setVolume(float fleftVolume, float frightVolume) throws SoundException
	{
		// TODO Auto-generated method stub
		super.setVolume(fleftVolume, frightVolume);
		
		if(this.streamID != 0)
		{
			final float masterVolume = this.getMasterVolume();
			final float leftVolume = this.leftVolume * masterVolume;
			final float rightVolume = this.rightVolume * masterVolume;

			this.getSoundPool().setVolume(this.streamID, leftVolume, rightVolume);
		}
	}

	@Override
	public void onMasterVolumeChanged(float masterVolume) throws SoundException
	{
		// TODO Auto-generated method stub
		this.setVolume(this.leftVolume, this.rightVolume);
		
	}

	@Override
	public void setLooping(boolean looping) throws SoundException
	{
		// TODO Auto-generated method stub
		super.setLooping(looping);
		
		this.setLoopCount((looping)?-1:0);
	}


}
