package flakor.game.core.entity;


import flakor.game.core.modifier.ModifierInterface;
import flakor.game.support.math.SmartList;
import flakor.game.system.graphics.UpdatableInterface;

public class ModifierList<T> extends SmartList<ModifierInterface<T>> implements UpdatableInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2810772277756710147L;
	private final T target;
	
	
	public ModifierList(final T target)
	{
		super();
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	public ModifierList(int capacity,final T target)
	{
		super(capacity);
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public T getTarget()
	{
			return this.target;
	}
	
	@Override
	public boolean add(final ModifierInterface<T> modifier)
	{
		if(modifier == null)
		{
			throw new IllegalArgumentException("Supplied " + ModifierInterface.class.getSimpleName() + " must not be null.");
		}
		else
		{
			return super.add(modifier);
		}
	}
	
	@Override
	public void onUpdate(float secondsElapsed)
	{
		// TODO Auto-generated method stub
		final int modifierCount = this.size();
		if(modifierCount > 0)
		{
			for(int i = modifierCount - 1; i >= 0; i--)
			{
				final ModifierInterface<T> modifier = this.get(i);
				modifier.onUpdate(secondsElapsed, this.target);
				if(modifier.isFinished() && modifier.isAutoUnregisterWhenFinished())
				{
					this.remove(i);
				}
			}
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		for(int i=this.size()-1;i>=0;i--)
		{
			this.get(i).reset();
		}
	}

}
