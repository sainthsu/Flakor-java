package flakor.game.core.entity;

import flakor.game.support.math.SmartList;
import flakor.game.system.graphics.UpdatableInterface;

public class UpdateHandlerList extends SmartList<UpdatableInterface> implements UpdatableInterface
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1025702231108807881L;

	public UpdateHandlerList()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public UpdateHandlerList(int capacity)
	{
		super(capacity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpdate(float secondsElapsed)
	{
		// TODO Auto-generated method stub
		final int handlerCount = this.size();
		for(int i = handlerCount - 1; i >= 0; i--)
		{
			this.get(i).onUpdate(secondsElapsed);
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		final int handlerCount = this.size();
		for(int i = handlerCount - 1; i >= 0; i--)
		{
			this.get(i).reset();
		}
	}

}
