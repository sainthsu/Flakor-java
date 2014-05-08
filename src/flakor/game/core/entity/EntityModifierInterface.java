package flakor.game.core.entity;

import flakor.game.core.modifier.ModifierInterface;

public interface EntityModifierInterface extends ModifierInterface<EntityInterface>
{
	public EntityModifierInterface DeepCopy() throws DeepCopyNotSupportedException;
	
	public static interface EntityModifierListener extends ModifierListener<EntityInterface>
	{
		
	}
	
	public interface EntityModifierMatcher extends MatcherInterface<ModifierInterface<EntityInterface>>
	{
		
	}
}
