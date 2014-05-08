package flakor.game.system.audio;



/**************************************************
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.12.18:00
 * @copyright Saint Hsu
 *************************************************/

public abstract class BaseAudioEntity implements AudioInterface
{
	private final AudioManagerInterface<? extends AudioInterface> audioManager;
	
	protected float leftVolume = 1.0f;
	protected float rightVolume = 1.0f;
	
	private boolean released;
	private boolean mute;
	
	/*************************************************************
	 * Constructors
	 *************************************************************/

	public BaseAudioEntity(final AudioManagerInterface<? extends AudioInterface> fAudioManager)
	{
			this.audioManager = fAudioManager;
	}

	/*************************************************************
	 * Getter & Setter
	 *************************************************************/
	
	protected AudioManagerInterface<? extends AudioInterface> getAudioManager() throws AudioException
	{
		return this.audioManager;
	}
	
	public float getActualLeftVolume() throws AudioException
	{
		this.assertNotReleased();

		return this.leftVolume * this.getMasterVolume();
	}

	public float getActualRightVolume() throws AudioException
	{
		this.assertNotReleased();

		return this.rightVolume * this.getMasterVolume();
	}

	protected float getMasterVolume() throws AudioException
	{
		this.assertNotReleased();

		return this.audioManager.getMasterVolume();
	}
	
	public boolean isReleased()
	{
		return released;
	}
	
	public boolean isMute()
	{
		return mute;
	}
	
	public void setMute() throws Exception
	{
		this.leftVolume = 0;
		this.rightVolume = 0;
		
		this.audioManager.setMasterVolume(0);
	}
	
	/*************************************************************
	 * Methods for/from SuperClass/Interfaces
	 * 
	 *************************************************************/
	
	@Override
	public float getVolume() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
		
		return (this.leftVolume+this.rightVolume)*0.5f;
	}

	@Override
	public void setVolume(final float volume) throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
		this.setVolume(volume, volume);
	}
	
	@Override
	public void setVolume(final float leftVolume, final float rightVolume) throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
		this.leftVolume = leftVolume;
		this.rightVolume = rightVolume;
	}

	@Override
	public float getLeftVolume() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
		return this.leftVolume;
	}

	@Override
	public float getRightVolume() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
		return this.rightVolume;
	}

	@Override
	public void onMasterVolumeChanged(final float masterVolume) throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	@Override
	public void setLooping(final boolean looping) throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}
	
	/*
	@Override
	public void prepare() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}*/

	@Override
	public void play() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	@Override
	public void pause() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	@Override
	public void resume() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	@Override
	public void stop() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	@Override
	public void release() throws AudioException
	{
		// TODO Auto-generated method stub
		this.assertNotReleased();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected abstract void throwOnReleased() throws AudioException ;
	
	protected void assertNotReleased() throws AudioException
	{
			if(this.released)
			{
				this.throwOnReleased();
			}
	}
}
