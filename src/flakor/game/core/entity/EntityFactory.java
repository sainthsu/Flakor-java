package flakor.game.core.entity;

public interface EntityFactory<T extends EntityInterface>
{
	public T create(final float x, final float y);
}
