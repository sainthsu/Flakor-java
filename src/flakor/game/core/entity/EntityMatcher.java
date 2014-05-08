package flakor.game.core.entity;

public interface EntityMatcher extends MatcherInterface<EntityInterface>
{
	public boolean matches(final EntityInterface entity);
}
