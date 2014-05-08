package flakor.game.system.graphics;

import flakor.game.core.entity.MatcherInterface;

public interface UpdatableInterface
{
	public void onUpdate(final float secondsElapsed);
	public void reset();
	
	public interface UpdateMatcher extends MatcherInterface<UpdatableInterface>
	{
		
	}
}
