package flakor.game.system.input.touch;


import android.view.MotionEvent;

import flakor.game.support.math.GenericPool;

public class TouchEvent
{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int INVALID_POINTER_ID = -1;

	public static final int ACTION_CANCEL = MotionEvent.ACTION_CANCEL;
	public static final int ACTION_DOWN = MotionEvent.ACTION_DOWN;
	public static final int ACTION_MOVE = MotionEvent.ACTION_MOVE;
	public static final int ACTION_OUTSIDE = MotionEvent.ACTION_OUTSIDE;
	public static final int ACTION_UP = MotionEvent.ACTION_UP;

	private static final TouchEventPool TOUCHEVENT_POOL = new TouchEventPool();

	// ===========================================================
	// Fields
	// ===========================================================

	protected int pointerID;

	protected float x;
	protected float y;

	protected int action;

	protected MotionEvent motionEvent;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static TouchEvent obtain(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pMotionEvent) 
	{
		final TouchEvent touchEvent = TOUCHEVENT_POOL.obtainPoolItem();
		touchEvent.set(pX, pY, pAction, pPointerID, pMotionEvent);
		return touchEvent;
	}

	private void set(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pMotionEvent)
	{
		this.x = pX;
		this.y = pY;
		this.action = pAction;
		this.pointerID = pPointerID;
		this.motionEvent = pMotionEvent;
	}

	public void recycle()
    {
		TOUCHEVENT_POOL.recyclePoolItem(this);
	}

	public static void recycle(final TouchEvent pTouchEvent)
    {
		TOUCHEVENT_POOL.recyclePoolItem(pTouchEvent);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getX()
	{
		return this.x;
	}

	public float getY()
    {
		return this.y;
	}

	public void set(final float pX, final float pY) 
	{
		this.x = pX;
		this.y = pY;
	}

	public void offset(final float pDeltaX, final float pDeltaY)
    {
		this.x += pDeltaX;
		this.y += pDeltaY;
	}

	public int getPointerID()
	{
		return this.pointerID;
	}

	public int getAction()
	{
		return this.action;
	}

	public boolean isActionDown() 
	{
		return this.action == TouchEvent.ACTION_DOWN;
	}

	public boolean isActionUp() {
		return this.action == TouchEvent.ACTION_UP;
	}

	public boolean isActionMove() 
	{
		return this.action == TouchEvent.ACTION_MOVE;
	}

	public boolean isActionCancel()
	{
		return this.action == TouchEvent.ACTION_CANCEL;
	}

	public boolean isActionOutside()
	{
		return this.action == TouchEvent.ACTION_OUTSIDE;
	}

	/**
	 * Provides the raw {@link android.view.MotionEvent} that originally caused this {@link TouchEvent}.
	 * The coordinates of this {@link android.view.MotionEvent} are in surface-coordinates!
	 * @return {@link android.view.MotionEvent}
	 */
	public MotionEvent getMotionEvent()
	{
		return this.motionEvent;
	}


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static final class TouchEventPool extends GenericPool<TouchEvent>
	{
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected TouchEvent onAllocatePoolItem()
		{
			return new TouchEvent();
		}
	}
}
