package flakor.game.system.audio;

/**************************************************
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.8.18:00
 * @copyright Saint Hsu
 *************************************************/
public interface AudioInterface
{
	//public void prepare();
	public void play();
	public void pause();
	public void resume();
	public void stop();
	public void release();
	
	public float getVolume();
	public void setVolume(final float volume);
	public void setVolume(final float leftVolume, final float rightVolume);
	
	public float getLeftVolume() ;
	public float getRightVolume() ;
	

	public void onMasterVolumeChanged(final float masterVolume) ;

	public void setLooping(final boolean looping) ;
	
}
