package flakor.game.system.audio;

import java.util.ArrayList;

/**************************************************
 * @author Saint Hsu(saint.hsu@outlook.com)
 * @since 2013.5.8.18:00
 * @copyright Saint Hsu
 *************************************************/

public abstract class BaseAudioManager<T extends AudioInterface> implements AudioManagerInterface<T>
{
	protected final ArrayList<T> audioEntities = new ArrayList<T>();

	protected float masterVolume = 1.0f;

	@Override
	public float getMasterVolume()
	{
		// TODO Auto-generated method stub
		return this.masterVolume;
	}

	@Override
	public void setMasterVolume(final float masterVolume)
	{
		// TODO Auto-generated method stub
		this.masterVolume = masterVolume;
		
		final ArrayList<T> audioEntities = this.audioEntities;
		for(int i = audioEntities.size() - 1; i >= 0; i--)
		{
			final T audioEntity = audioEntities.get(i);

			audioEntity.onMasterVolumeChanged(masterVolume);
		}
	}

	@Override
	public void add(final T audio)
	{
		// TODO Auto-generated method stub
		this.audioEntities.add(audio);
	}

	@Override
	public boolean remove(final T audio)
	{
		// TODO Auto-generated method stub
		return this.audioEntities.remove(audio);
	}

	@Override
	public void releaseAll()
	{
		// TODO Auto-generated method stub
		final ArrayList<T> audioEntities = this.audioEntities;
		for(int i = audioEntities.size() - 1; i >= 0; i--)
		{
			final T audioEntity = audioEntities.get(i);

			audioEntity.stop();
			audioEntity.release();
		}
	}
	
	
}
