package flakor.game.core.entity;

public interface EntityParameterCallable extends ParameterCallable<EntityInterface>
{
	public void call(final EntityInterface entity);
}
