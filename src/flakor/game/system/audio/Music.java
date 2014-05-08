package flakor.game.system.audio;


import android.media.MediaPlayer;

/**************************************************
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.8.18:00
 * @copyright Saint Hsu
 *************************************************/

public class Music extends BaseAudioEntity
{

	private MediaPlayer mediaPlayer;
	
	/*************************************************************
	* Constructors
	*************************************************************/
	
	public Music(final MusicManager musicManager,final MediaPlayer mediaPlayer)
	{
		super(musicManager);
		// TODO Auto-generated constructor stub
		this.mediaPlayer = mediaPlayer;
	}

	/*************************************************************
	* Getter & Setter
	* @throws MusicException 
	*************************************************************/
	
	public boolean isPlaying() throws MusicException
	{
		this.assertNotReleased();
		return this.mediaPlayer.isPlaying();
	}
	
	public MediaPlayer getMediaPlayer() throws MusicException
	{
		this.assertNotReleased();
		return this.mediaPlayer;
	}
	
	/*************************************************************
	* Methods for/from SuperClass/Interfaces
	* @throws MusicException 
	*************************************************************/
	
	
	@Override
	protected void throwOnReleased() throws MusicException
	{
		// TODO Auto-generated method stub
		throw new MusicException();
	}

	@Override
	protected  MusicManager getAudioManager() throws MusicException
	{
		// TODO Auto-generated method stub
		return (MusicManager) super.getAudioManager();
	}

	@Override
	public void setVolume(float leftVolume, float rightVolume) throws MusicException
	{
		// TODO Auto-generated method stub
		super.setVolume(leftVolume, rightVolume);
		
		final float masterVolume = this.getAudioManager().getMasterVolume();
		final float actualLeftVolume = leftVolume * masterVolume;
		final float actualRightVolume = rightVolume * masterVolume;

		this.mediaPlayer.setVolume(actualLeftVolume, actualRightVolume);
	}

	@Override
	public void onMasterVolumeChanged(float masterVolume) throws MusicException
	{
		// TODO Auto-generated method stub
		this.setVolume(this.leftVolume, this.rightVolume);
	}

	@Override
	public void setLooping(boolean looping) throws MusicException
	{
		// TODO Auto-generated method stub
		super.setLooping(looping);
		this.mediaPlayer.setLooping(looping);
	}

	@Override
	public void play() throws MusicException
	{
		// TODO Auto-generated method stub
		super.play();
	}

	@Override
	public void pause() throws MusicException
	{
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void resume() throws MusicException {
		// TODO Auto-generated method stub
		super.resume();
	}

	@Override
	public void stop() throws MusicException
	{
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void release() throws MusicException
	{
		// TODO Auto-generated method stub
		super.release();
	}

}
