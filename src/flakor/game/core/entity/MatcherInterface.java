package flakor.game.core.entity;

public interface MatcherInterface<T>
{
	public boolean matches(final T object);
}
