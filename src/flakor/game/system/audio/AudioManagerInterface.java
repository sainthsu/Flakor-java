package flakor.game.system.audio;


public interface AudioManagerInterface<T extends AudioInterface> 
{
	public float getMasterVolume();
	public void setMasterVolume(final float masterVolume);
	
	public void add(final T audio);
	public boolean remove(final T audio);
	
	public void releaseAll();
	
}
