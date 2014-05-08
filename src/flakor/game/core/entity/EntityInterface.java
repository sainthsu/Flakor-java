package flakor.game.core.entity;

import flakor.game.core.camera.Camera;
import flakor.game.core.element.ColorfulInterface;
import flakor.game.core.element.Point;
import flakor.game.core.entity.EntityModifierInterface.EntityModifierMatcher;
import flakor.game.system.graphics.DisposableInterface;
import flakor.game.system.graphics.DrawInterface;
import flakor.game.system.graphics.TransformMatrix;
import flakor.game.system.graphics.UpdatableInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface EntityInterface extends DrawInterface,UpdatableInterface,DisposableInterface,ColorfulInterface
{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int TAG_INVALID = Integer.MIN_VALUE;
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public boolean isVisible();
	public void setVisible(final boolean visible);

	public boolean isIgnoreUpdate();
	public void setIgnoreUpdate(boolean ignoreUpdate);

	public boolean isChildrenVisible();
	public void setChildrenVisible(final boolean childrenVisible);

	public boolean isChildrenIgnoreUpdate();
	public void setChildrenIgnoreUpdate(boolean childrenIgnoreUpdate);

	public int getTag();
	public void setTag(final int tag);

	public int getZIndex();
	public void setZIndex(final int ZIndex);

	public boolean hasParent();
	public EntityInterface getParent();
	public void setParent(final EntityInterface entity);

	public float getX();
	public float getY();
    public Point getPosition();
	public void setX(final float x);
	public void setY(final float y);
	public void setPosition(final EntityInterface otherEntity);
	public void setPosition(final float x, final float y);
    public void setPosition(final Point position);

	public boolean isRotated();
	public float getRotation();
	public void setRotation(final float rotation);

	public float getRotationCenterX();
	public float getRotationCenterY();
    public Point getRotationCenter();
	public void setRotationCenterX(final float rotationCenterX);
	public void setRotationCenterY(final float rotationCenterY);
	public void setRotationCenter(final float rotationCenterX, final float rotationCenterY);
    public void setRotationCenter(final Point rotationCenter);

	public boolean isScaled();
	public float getScaleX();
	public float getScaleY();
	public void setScaleX(final float scaleX);
	public void setScaleY(final float scaleY);
	public void setScale(final float scale);
	public void setScale(final float scaleX, final float scaleY);

	public float getScaleCenterX();
	public float getScaleCenterY();
	public void setScaleCenterX(final float scaleCenterX);
	public void setScaleCenterY(final float scaleCenterY);
	public void setScaleCenter(final float scaleCenterX, final float scaleCenterY);

	public boolean isSkewed();
	public float getSkewX();
	public float getSkewY();
	public void setSkewX(final float skewX);
	public void setSkewY(final float skewY);
	public void setSkew(final float skew);
	public void setSkew(final float skewX, final float skewY);

	public float getSkewCenterX();
	public float getSkewCenterY();
    public Point getSkewCenter();
	public void setSkewCenterX(final float skewCenterX);
	public void setSkewCenterY(final float skewCenterY);
	public void setSkewCenter(final float skewCenterX, final float skewCenterY);
    public void setSkewCenter(final Point skewCenter);

	public boolean isRotatedOrScaledOrSkewed();

	/**
	 * @return a shared(!) float[] of length 2.
	 */
	public float[] getSceneCenterCoordinates();

	/**
	 * @param reuse must be of length 2.
	 * @return <code>reuse</code> as a convenience.
	 */
	public float[] getSceneCenterCoordinates(final float[] reuse);

	/**
	 * @param x
	 * @param y
	 * @return a shared(!) float[] of length 2.
	 */
	public float[] convertLocalToSceneCoordinates(final float x, final float y);
	/**
	 * @param x
	 * @param y
	 * @param reuse must be of length 2.
	 * @return <code>reuse</code> as a convenience.
	 */
	public float[] convertLocalToSceneCoordinates(final float x, final float y, final float[] reuse);
	/**
	 * @param coordinates must be of length 2.
	 * @return a shared(!) float[] of length 2.
	 */
	public float[] convertLocalToSceneCoordinates(final float[] coordinates);
	/**
	 * @param coordinates must be of length 2.
	 * @param  reuse must be of length 2.
	 * @return <code>reuse</code> as a convenience.
	 */
	public float[] convertLocalToSceneCoordinates(final float[] coordinates, final float[] reuse);

	/**
	 * @param x
	 * @param y
	 * @return a shared(!) float[] of length 2.
	 */
	public float[] convertSceneToLocalCoordinates(final float x, final float y);
	/**
	 * @param x
	 * @param y
	 * @param reuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
	public float[] convertSceneToLocalCoordinates(final float x, final float y, final float[] reuse);
	/**
	 * @param coordinates must be of length 2.
	 * @return a shared(!) float[] of length 2.
	 */
	public float[] convertSceneToLocalCoordinates(final float[] coordinates);
	/**
	 * @param coordinates must be of length 2.
	 * @param reuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
	public float[] convertSceneToLocalCoordinates(final float[] coordinates, final float[] reuse);

	public TransformMatrix getLocalToSceneMatrix();
	public TransformMatrix getSceneToLocalMatrix();

	public TransformMatrix getLocalToParentMatrix();
	public TransformMatrix getParentToLocalMatrix();

	public int getChildCount();

	public void onAttached();
	public void onDetached();

	public void attachChild(final EntityInterface entity);
	public void attachChild(final EntityInterface entity,int ZIndex);
	//public void detachFromParent();

	public EntityInterface getChildByTag(final int tag);
	public EntityInterface getChildByMatcher(final EntityMatcher entityMatcher);
	public EntityInterface getChildByIndex(final int index);
	public EntityInterface getChildByZIndex(final int ZIndex);
	public EntityInterface getFirstChild();
	public EntityInterface getLastChild();
	
	/**
	 * @param entityMatcher
	 * @return all children (recursively!) that match the supplied {@link EntityMatcher}.
	 */
	public ArrayList<EntityInterface> query(final EntityMatcher entityMatcher);

	/**
	 * @param entityMatcher
	 * @return the first child (recursively!) that matches the supplied {@link EntityMatcher} or <code>null</code> if none matches..
	 */
	public EntityInterface queryFirst(final EntityMatcher entityMatcher);

	/**
	 * @param entityMatcher matcher to match children
	 * @param result the {@link List} to put the result into.
	 * @return all children (recursively!) that match the supplied {@link EntityMatcher}.
	 */
	public <L extends List<EntityInterface>> L query(final EntityMatcher entityMatcher, final L result);

	/**
	 * @param entityMatcher
	 * @return the first child (recursively!) that matches the supplied {@link EntityMatcher} or <code>null</code> if none matches..
	 * @throws ClassCastException when the supplied {@link EntityMatcher} matched an {@link EntityInterface} that was not of the requested subtype.
	 */
	public <S extends EntityInterface> S queryFirstForSubclass(final EntityMatcher entityMatcher);

	/**
	 * @param entityMatcher
	 * @return all children (recursively!) that match the supplied {@link EntityMatcher}.
	 * @throws ClassCastException when the supplied {@link EntityMatcher} matched an {@link EntityInterface} that was not of the requested subtype.
	 */
	public <S extends EntityInterface> ArrayList<S> queryForSubclass(final EntityMatcher entityMatcher) throws ClassCastException;

	/**
	 * @param entityMatcher
	 * @param result the {@link List} to put the result into.
	 * @return all children (recursively!) that match the supplied {@link EntityMatcher}.
	 * @throws ClassCastException when the supplied {@link EntityMatcher} matched an {@link EntityInterface} that was not of the requested subtype.
	 */
	public <L extends List<S>, S extends EntityInterface> L queryForSubclass(final EntityMatcher entityMatcher, final L result) throws ClassCastException;

	/**
	 * Immediately sorts the {@link EntityInterface}s based on their ZIndex. Sort is stable.
	 */
	public void sortChildren();
	/**
	 * Sorts the {@link EntityInterface}s based on their ZIndex. Sort is stable.
	 * In contrast to {@link EntityInterface#sortChildren()} this method is particularly useful to avoid multiple sorts per frame.
	 * @param immediate if <code>true</code>, the sorting is executed immediately.
	 * If <code>false</code> the sorting is executed before the next (visible) drawing of the children of this {@link EntityInterface}.
	 */
	public void sortChildren(final boolean immediate);
	/**
	 * Sorts the {@link EntityInterface}s based on the {@link Comparator} supplied. Sort is stable.
	 * @param pEntityComparator
	 */
	public void sortChildren(final EntityComparator pEntityComparator);

	public boolean detachSelf();


	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link flakor.game.core.layer.Layer} or the {@link flakor.game.core.Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean detachChild(final EntityInterface pEntity);

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link flakor.game.core.layer.Layer} or the {@link flakor.game.core.Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public EntityInterface detachChild(final int pTag);

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link flakor.game.core.layer.Layer} or the {@link flakor.game.core.Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public EntityInterface detachChild(final EntityMatcher pEntityMatcher);

	/**
	 * <b><i>WARNING:</i> This function should be called from within
	 * {@link RunnableHandler#postRunnable(Runnable)} which is registered
	 * to a {@link flakor.game.core.layer.Layer} or the {@link flakor.game.core.Engine} itself, because otherwise
	 * it may throw an {@link IndexOutOfBoundsException} in the
	 * Update-Thread or the GL-Thread!</b>
	 */
	public boolean detachChildren(final EntityMatcher pEntityMatcher);

	public void detachChildren();
    //public void cleanUp();

	public void callOnChildren(final EntityParameterCallable entityParameterCallable);
	public void callOnChildren(final EntityParameterCallable entityParameterCallable, final EntityMatcher entityMatcher);

	public void registerUpdateHandler(final UpdatableInterface updateHandler);
	public boolean unregisterUpdateHandler(final UpdatableInterface updateHandler);
	public boolean unregisterUpdateHandlers(final UpdateMatcher updateHandlerMatcher);
	public int getUpdateHandlerCount();
	public void clearUpdateHandlers();

	public void registerEntityModifier(final EntityModifierInterface entityModifier);
	public boolean unregisterEntityModifier(final EntityModifierInterface entityModifier);
	public boolean unregisterEntityModifierMatcher(final EntityModifierMatcher entityModifierMatcher);
	public int getEntityModifierCount();
	public void clearEntityModifiers();

	public boolean isCullingEnabled();
	public void setCullingEnabled(final boolean cullingEnabled);

	/**
	 * Will only be performed if {@link EntityInterface#isCullingEnabled()} is true.
	 *
	 * @param camera the currently active camera to perform culling checks against.
	 * @return <code>true</code> when this object is visible by the {@link Camera}, <code>false</code> otherwise.
	 */
	public boolean isCulled(final Camera camera);

	public void setUserData(final Object userData);
	public Object getUserData();

	public void toString(final StringBuilder stringBuilder);
}
