package flakor.game.core.modifier;

import flakor.game.tool.FlakorRuntimeException;

import java.util.Comparator;

public interface ModifierInterface<T>
{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final Comparator<ModifierInterface<?>> MODIFIER_COMPARATOR_DURATION_DESCENDING = new Comparator<ModifierInterface<?>>()
	{
		@Override
		public int compare(final ModifierInterface<?> pModifierA, final ModifierInterface<?> pModifierB)
		{
			final float durationA = pModifierA.getDuration();
			final float durationB = pModifierB.getDuration();

			if (durationA < durationB)
			{
				return 1;
			}
			else if (durationA > durationB)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	};
	// ===========================================================
	// Methods
	// ===========================================================

	public void reset();

	public boolean isFinished();
	public boolean isAutoUnregisterWhenFinished();
	public void setAutoUnregisterWhenFinished(final boolean removeWhenFinished);

	public ModifierInterface<T> deepCopy() throws DeepCopyNotSupportedException;

	public float getSecondsElapsed();
	public float getDuration();

	public float onUpdate(final float secondsElapsed, final T item);

	public void addModifierListener(final ModifierListener<T> pModifierListener);
	public boolean removeModifierListener(final ModifierListener<T> pModifierListener);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ModifierListener<T>
	{

		// ===========================================================
		// Methods
		// ===========================================================

		public void onModifierStarted(final ModifierInterface<T> pModifier, final T pItem);
		public void onModifierFinished(final ModifierInterface<T> pModifier, final T pItem);
	}

	public static class DeepCopyNotSupportedException extends FlakorRuntimeException
	{

		/**
		* 
		*/
		private static final long serialVersionUID = -6123070236327818533L;
		
	}
}
