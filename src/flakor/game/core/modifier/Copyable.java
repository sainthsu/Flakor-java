package flakor.game.core.modifier;

/**
 * Created by saint on 8/25/13.
 */
public interface Copyable
{
    public Object deepCopy() throws ModifierInterface.DeepCopyNotSupportedException;
}
