package flakor.game.core.camera;

import flakor.game.core.collision.RectangularShapeCollisionChecker;
import flakor.game.core.entity.Constants;
import flakor.game.core.entity.EntityInterface;
import flakor.game.core.entity.UpdateHandlerList;
import flakor.game.core.geometry.Line;
import flakor.game.core.geometry.RectangularShape;
import flakor.game.core.layer.HUD;
import flakor.game.support.math.MathUtils;
import flakor.game.system.graphics.TransformMatrix;
import flakor.game.system.graphics.UpdatableInterface;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.input.touch.TouchEvent;

/**
 * 引擎相机，只有一个，决定引擎的视口大小等因素
 */
public class Camera implements UpdatableInterface
{
	// ===========================================================
	// Constants
	// ===========================================================

	static final float[] VERTICES_TMP = new float[2];
	private static final int UPDATEHANDLERS_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	protected float XMin;
    protected float YMin;

	protected float XMax;
	protected float YMax;

	private float ZNear = -1.0f;
	private float ZFar = 1.0f;

	private HUD HUD;

	private EntityInterface chaseEntity;

	protected float rotation = 0;
	protected float cameraSceneRotation = 0;

	protected int surfaceX;
	protected int surfaceY;
	protected int surfaceWidth;
	protected int surfaceHeight;

	protected boolean resizeOnSurfaceSizeChanged;
	protected UpdateHandlerList updateHandlers;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Camera(final float X, final float Y, final float Width, final float Height)
	{
			this.set(X, Y, X + Width, Y + Height);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float getXMin()
	{
		return XMin;
	}

	public void setXMin(float xMin)
	{
		XMin = xMin;
	}

	public float getXMax() {
		return XMax;
	}

	public void setXMax(float xMax)
	{
		XMax = xMax;
	}

	public float getYMin() 
	{
		return YMin;
	}

	public void setYMin(float yMin)
	{
		YMin = yMin;
	}

	public float getYMax()
	{
		return YMax;
	}

	public void setYMax(float yMax)
	{
		YMax = yMax;
	}

	public void set(final float XMin, final float YMin, final float XMax, final float YMax)
	{
		this.XMin = XMin;
		this.XMax = XMax;
		this.YMin = YMin;
		this.YMax = YMax;
	}
	
	public float getZNear()
	{
		return ZNear;
	}

	public void setZNear(float zNear) 
	{
		ZNear = zNear;
	}
	
	public float getZFar()
	{
		return ZFar;
	}

	public void setZFar(float zFar)
	{
		ZFar = zFar;
	}

	public void setZClippingPlanes(final float nearZClippingPlane, final float farZClippingPlane)
	{
		this.ZNear = nearZClippingPlane;
		this.ZFar = farZClippingPlane;
	}

	public float getWidth() 
	{
		return this.XMax - this.XMin;
	}

	public float getHeight() 
	{
		return this.YMax - this.YMin;
	}

	public float getWidthRaw()
	{
		return this.XMax - this.XMin;
	}

	public float getHeightRaw() 
	{
		return this.YMax - this.YMin;
	}

	public float getCenterX()
	{
		return (this.XMin + this.XMax) * 0.5f;
	}

	public float getCenterY()
	{
		return (this.YMin + this.YMax) * 0.5f;
	}

	public void setCenter(final float centerX, final float centerY)
	{
		final float dX = centerX - this.getCenterX();
		final float dY = centerY - this.getCenterY();

		this.XMin += dX;
		this.XMax += dX;
		this.YMin += dY;
		this.YMax += dY;
	}

	public void offsetCenter(final float X, final float Y)
	{
		this.setCenter(this.getCenterX() + X, this.getCenterY() + Y);
	}

	public HUD getHUD()
	{
		return this.HUD;
	}

	public void setHUD(final HUD hUD)
	{
		this.HUD = hUD;
		if(hUD != null) 
		{
			hUD.setCamera(this);
		}
	}

	public boolean hasHUD() 
	{
		return this.HUD != null;
	}
	
	public EntityInterface getChaseEntity()
	{
		return chaseEntity;
	}

	public void setChaseEntity(EntityInterface chaseEntity)
    {
		this.chaseEntity = chaseEntity;
	}

	public boolean isRotated()
	{
		return this.rotation != 0;
	}
	
	public float getRotation()
	{
		return rotation;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	public float getCameraSceneRotation()
	{
		return cameraSceneRotation;
	}

	public void setCameraSceneRotation(float cameraSceneRotation)
	{
		this.cameraSceneRotation = cameraSceneRotation;
	}

	public int getSurfaceX()
	{
		return surfaceX;
	}

	public int getSurfaceY() 
	{
		return surfaceY;
	}

	public int getSurfaceWidth()
	{
		return surfaceWidth;
	}

	public int getSurfaceHeight()
	{
		return surfaceHeight;
	}

	public void setSurfaceSize(final int surfaceX, final int surfaceY, final int surfaceWidth, final int surfaceHeight) 
	{
		if(this.surfaceHeight == 0 && this.surfaceWidth == 0) 
		{
			this.onSurfaceSizeInitialized(surfaceX, surfaceY, surfaceWidth, surfaceHeight);
		}
		else if(this.surfaceWidth != surfaceWidth || this.surfaceHeight != surfaceHeight)
		{
			this.onSurfaceSizeChanged(this.surfaceX, this.surfaceY, this.surfaceWidth, this.surfaceHeight, surfaceX, surfaceY, surfaceWidth, surfaceHeight);
		}
	}
	
	public boolean isResizeOnSurfaceSizeChanged()
	{
		return resizeOnSurfaceSizeChanged;
	}

	public void setResizeOnSurfaceSizeChanged(boolean resizeOnSurfaceSizeChanged)
	{
		this.resizeOnSurfaceSizeChanged = resizeOnSurfaceSizeChanged;
	}

	public UpdateHandlerList getUpdateHandlers() 
	{
		return updateHandlers;
	}

	public void setUpdateHandlers(UpdateHandlerList updateHandlers)
	{
		this.updateHandlers = updateHandlers;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	public void onUpdate(final float secondsElapsed)
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers != null)
		{
			this.updateHandlers.onUpdate(secondsElapsed);
		}
		if(this.HUD != null)
		{
			this.HUD.onUpdate(secondsElapsed);
		}
		this.updateChaseEntity();
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public void onDrawHUD(final GLState glState)
	{
		if(this.HUD != null) 
		{
			this.HUD.onDraw(glState, this);
		}
	}

	public void updateChaseEntity()
	{
		if(this.chaseEntity != null)
		{
			final float[] centerCoordinates = this.chaseEntity.getSceneCenterCoordinates();
			this.setCenter(centerCoordinates[Constants.VERTEX_INDEX_X], centerCoordinates[Constants.VERTEX_INDEX_Y]);
		}
	}

	public boolean isLineVisible(final Line line)
	{
		return RectangularShapeCollisionChecker.isVisible(this, line);
	}

	public boolean isRectangularShapeVisible(final RectangularShape rectangularShape)
	{
		return RectangularShapeCollisionChecker.isVisible(this, rectangularShape);
	}

	public boolean isRectangularShapeVisible(final float X, final float Y, final float width, final float height, final TransformMatrix localToSceneTransformation) {
		return RectangularShapeCollisionChecker.isVisible(this, X, Y, width, height, localToSceneTransformation);
	}

	public void onApplySceneMatrix(final GLState pGLState)
	{
		pGLState.orthoProjectionGLMatrixf(this.getXMin(), this.getXMax(), this.getYMax(), this.getYMin(), this.ZNear, this.ZFar);

		final float rotation = this.rotation;
		if(rotation != 0)
        {
			Camera.applyRotation(pGLState, this.getCenterX(), this.getCenterY(), rotation);
		}
	}

	public void onApplyLayerBackgroundMatrix(final GLState glState)
	{
		final float widthRaw = this.getWidthRaw();
		final float heightRaw = this.getHeightRaw();

		glState.orthoProjectionGLMatrixf(0, widthRaw, heightRaw, 0, this.ZNear, this.ZFar);

		final float rotation = this.rotation;
		if(rotation != 0)
		{
			Camera.applyRotation(glState, widthRaw * 0.5f, heightRaw * 0.5f, rotation);
		}
	}

	public void onApplyCameraSceneMatrix(final GLState pGLState)
	{
		final float widthRaw = this.getWidthRaw();
		final float heightRaw = this.getHeightRaw();
		pGLState.orthoProjectionGLMatrixf(0, widthRaw, heightRaw, 0, this.ZNear, this.ZFar);

		final float cameraSceneRotation = this.cameraSceneRotation;
		if(cameraSceneRotation != 0) {
			Camera.applyRotation(pGLState, widthRaw * 0.5f, heightRaw * 0.5f, cameraSceneRotation);
		}
	}

	private static void applyRotation(final GLState pGLState, final float pRotationCenterX, final float pRotationCenterY, final float pAngle) 
	{
		pGLState.translateProjectionGLMatrixf(pRotationCenterX, pRotationCenterY, 0);
		pGLState.rotateProjectionGLMatrixf(pAngle, 0, 0, 1);
		pGLState.translateProjectionGLMatrixf(-pRotationCenterX, -pRotationCenterY, 0);
	}

	public void convertSceneToCameraSceneTouchEvent(final TouchEvent pSceneTouchEvent)
    {
		this.unapplySceneRotation(pSceneTouchEvent);

		this.applySceneToCameraSceneOffset(pSceneTouchEvent);

		this.applyCameraSceneRotation(pSceneTouchEvent);
	}

	public float[] getCameraSceneCoordinatesFromSceneCoordinates(final float pSceneX, final float pSceneY)
    {
		Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pSceneX;
		Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pSceneY;

		return this.getCameraSceneCoordinatesFromSceneCoordinates(Camera.VERTICES_TMP);
	}

	public float[] getCameraSceneCoordinatesFromSceneCoordinates(final float[] pSceneCoordinates)
    {
		this.unapplySceneRotation(pSceneCoordinates);

		this.applySceneToCameraSceneOffset(pSceneCoordinates);

		this.applyCameraSceneRotation(pSceneCoordinates);

		return pSceneCoordinates;
	}

	public void convertCameraSceneToSceneTouchEvent(final TouchEvent pCameraSceneTouchEvent)
    {
		this.unapplyCameraSceneRotation(pCameraSceneTouchEvent);

		this.unapplySceneToCameraSceneOffset(pCameraSceneTouchEvent);

		this.applySceneRotation(pCameraSceneTouchEvent);
	}

	public float[] getSceneCoordinatesFromCameraSceneCoordinates(final float pCameraSceneX, final float pCameraSceneY)
    {
		Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pCameraSceneX;
		Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pCameraSceneY;

		return this.getSceneCoordinatesFromCameraSceneCoordinates(Camera.VERTICES_TMP);
	}

	public float[] getSceneCoordinatesFromCameraSceneCoordinates(final float[] pCameraSceneCoordinates)
    {
		this.unapplyCameraSceneRotation(pCameraSceneCoordinates);

		this.unapplySceneToCameraSceneOffset(pCameraSceneCoordinates);

		this.applySceneRotation(pCameraSceneCoordinates);

		return pCameraSceneCoordinates;
	}

	protected void applySceneToCameraSceneOffset(final TouchEvent pSceneTouchEvent)
    {
		pSceneTouchEvent.offset(-this.XMin, -this.YMin);
	}

	protected void applySceneToCameraSceneOffset(final float[] pSceneCoordinates)
    {
		pSceneCoordinates[Constants.VERTEX_INDEX_X] -= this.XMin;
		pSceneCoordinates[Constants.VERTEX_INDEX_Y] -= this.YMin;
	}

	protected void unapplySceneToCameraSceneOffset(final TouchEvent pCameraSceneTouchEvent)
    {
		pCameraSceneTouchEvent.offset(this.XMin, this.YMin);
	}

	protected void unapplySceneToCameraSceneOffset(final float[] pCameraSceneCoordinates)
    {
		pCameraSceneCoordinates[Constants.VERTEX_INDEX_X] += this.XMin;
		pCameraSceneCoordinates[Constants.VERTEX_INDEX_Y] += this.YMin;
	}

	private void applySceneRotation(final float[] pCameraSceneCoordinates)
	{
		final float rotation = this.rotation;
		if(rotation != 0)
        {
			MathUtils.rotateAroundCenter(pCameraSceneCoordinates, -rotation, this.getCenterX(), this.getCenterY());
		}
	}

	private void applySceneRotation(final TouchEvent pCameraSceneTouchEvent)
    {
		final float rotation = this.rotation;
		if(rotation != 0)
        {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.rotateAroundCenter(Camera.VERTICES_TMP, -rotation, this.getCenterX(), this.getCenterY());

			pCameraSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	private void unapplySceneRotation(final float[] pSceneCoordinates)
    {
		final float rotation = this.rotation;

		if(rotation != 0) {
			MathUtils.revertRotateAroundCenter(pSceneCoordinates, rotation, this.getCenterX(), this.getCenterY());
		}
	}

	private void unapplySceneRotation(final TouchEvent pSceneTouchEvent)
    {
		final float rotation = this.rotation;

		if(rotation != 0) {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.revertRotateAroundCenter(Camera.VERTICES_TMP, rotation, this.getCenterX(), this.getCenterY());

			pSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	private void applyCameraSceneRotation(final float[] pSceneCoordinates)
    {
		final float cameraSceneRotation = -this.cameraSceneRotation;

		if(cameraSceneRotation != 0) {
			MathUtils.rotateAroundCenter(pSceneCoordinates, cameraSceneRotation, (this.XMax - this.XMin) * 0.5f, (this.YMax - this.YMin) * 0.5f);
		}
	}

	private void applyCameraSceneRotation(final TouchEvent pSceneTouchEvent)
    {
		final float cameraSceneRotation = -this.cameraSceneRotation;

		if(cameraSceneRotation != 0) {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.rotateAroundCenter(Camera.VERTICES_TMP, cameraSceneRotation, (this.XMax - this.XMin) * 0.5f, (this.YMax - this.YMin) * 0.5f);

			pSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	private void unapplyCameraSceneRotation(final float[] pCameraSceneCoordinates)
    {
		final float cameraSceneRotation = -this.cameraSceneRotation;

		if(cameraSceneRotation != 0) {
			MathUtils.revertRotateAroundCenter(pCameraSceneCoordinates, cameraSceneRotation, (this.XMax - this.XMin) * 0.5f, (this.YMax - this.YMin) * 0.5f);
		}
	}

	private void unapplyCameraSceneRotation(final TouchEvent pCameraSceneTouchEvent)
    {
		final float cameraSceneRotation = -this.cameraSceneRotation;

		if(cameraSceneRotation != 0) {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pCameraSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pCameraSceneTouchEvent.getY();

			MathUtils.revertRotateAroundCenter(Camera.VERTICES_TMP, cameraSceneRotation, (this.XMax - this.XMin) * 0.5f, (this.YMax - this.YMin) * 0.5f);

			pCameraSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	// TODO Camera already knows about its surfaceWidth, is there a need to pass it in here again?
	public void convertSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final int pSurfaceWidth, final int pSurfaceHeight)
    {
		final float relativeX;
		final float relativeY;

		final float surfaceTouchEventX = pSurfaceTouchEvent.getX();
		final float surfaceTouchEventY = pSurfaceTouchEvent.getY();

		final float rotation = this.rotation;
		if(rotation == 0) {
			relativeX = surfaceTouchEventX / pSurfaceWidth;
			relativeY = surfaceTouchEventY / pSurfaceHeight;
		} else if(rotation == 180) {
			relativeX = 1 - (surfaceTouchEventX / pSurfaceWidth);
			relativeY = 1 - (surfaceTouchEventY / pSurfaceHeight);
		} else {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = surfaceTouchEventX;
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = surfaceTouchEventY;

			MathUtils.rotateAroundCenter(Camera.VERTICES_TMP, rotation, pSurfaceWidth >> 1, pSurfaceHeight >> 1);

			relativeX = Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] / pSurfaceWidth;
			relativeY = Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] / pSurfaceHeight;
		}

		this.convertAxisAlignedSurfaceToSceneTouchEvent(pSurfaceTouchEvent, relativeX, relativeY);
	}

	private void convertAxisAlignedSurfaceToSceneTouchEvent(final TouchEvent pSurfaceTouchEvent, final float pRelativeX, final float pRelativeY)
    {
		final float xMin = this.getXMin();
		final float xMax = this.getXMax();
		final float yMin = this.getYMin();
		final float yMax = this.getYMax();

		final float x = xMin + pRelativeX * (xMax - xMin);
		final float y = yMin + pRelativeY * (yMax - yMin);

		pSurfaceTouchEvent.set(x, y);
	}

	public void convertSceneToSurfaceTouchEvent(final TouchEvent pSceneTouchEvent, final int pSurfaceWidth, final int pSurfaceHeight)
    {
		this.convertAxisAlignedSceneToSurfaceTouchEvent(pSceneTouchEvent, pSurfaceWidth, pSurfaceHeight);

		final float rotation = this.rotation;
		if(rotation == 0) {
			/* Nothing to do. */
		} else if(rotation == 180) {
			pSceneTouchEvent.set(pSurfaceWidth - pSceneTouchEvent.getX(), pSurfaceHeight - pSceneTouchEvent.getY());
		} else {
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X] = pSceneTouchEvent.getX();
			Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y] = pSceneTouchEvent.getY();

			MathUtils.revertRotateAroundCenter(Camera.VERTICES_TMP, rotation, pSurfaceWidth >> 1, pSurfaceHeight >> 1);

			pSceneTouchEvent.set(Camera.VERTICES_TMP[Constants.VERTEX_INDEX_X], Camera.VERTICES_TMP[Constants.VERTEX_INDEX_Y]);
		}
	}

	private void convertAxisAlignedSceneToSurfaceTouchEvent(final TouchEvent pSceneTouchEvent, final int pSurfaceWidth, final int pSurfaceHeight)
    {
		final float xMin = this.getXMin();
		final float xMax = this.getXMax();
		final float yMin = this.getYMin();
		final float yMax = this.getYMax();

		final float relativeX = (pSceneTouchEvent.getX() - xMin) / (xMax - xMin);
		final float relativeY = (pSceneTouchEvent.getY() - yMin) / (yMax - yMin);

		pSceneTouchEvent.set(relativeX * pSurfaceWidth, relativeY * pSurfaceHeight);
	}

	public void registerUpdateHandler(final UpdatableInterface updateHandler)
    {
		if(this.updateHandlers == null) {
			this.allocateUpdateHandlers();
		}
		this.updateHandlers.add(updateHandler);
	}

	public boolean unregisterUpdateHandler(final UpdatableInterface updateHandler)
    {
		if(this.updateHandlers == null) {
			return false;
		}
		return this.updateHandlers.remove(updateHandler);
	}

	public boolean unregisterUpdateHandlers(final UpdateMatcher updateHandlerMatcher)
	{
		if(this.updateHandlers == null)
        {
			return false;
		}
		return this.updateHandlers.removeAll(updateHandlerMatcher);
	}

	public void clearUpdateHandlers()
	{
		if(this.updateHandlers == null) {
			return;
		}
		this.updateHandlers.clear();
	}

	private void allocateUpdateHandlers() 
	{
		this.updateHandlers = new UpdateHandlerList(Camera.UPDATEHANDLERS_CAPACITY_DEFAULT);
	}

	protected void onSurfaceSizeInitialized(final int pSurfaceX, final int pSurfaceY, final int pSurfaceWidth, final int pSurfaceHeight) {
		this.surfaceX = pSurfaceX;
		this.surfaceY = pSurfaceY;
		this.surfaceWidth = pSurfaceWidth;
		this.surfaceHeight = pSurfaceHeight;
	}

	protected void onSurfaceSizeChanged(final int pOldSurfaceX, final int pOldSurfaceY, final int pOldSurfaceWidth, final int pOldSurfaceHeight, final int pNewSurfaceX, final int pNewSurfaceY, final int pNewSurfaceWidth, final int pNewSurfaceHeight)
	{
		if(this.resizeOnSurfaceSizeChanged)
		{
			final float surfaceWidthRatio = (float)pNewSurfaceWidth / pOldSurfaceWidth;
			final float surfaceHeightRatio = (float)pNewSurfaceHeight / pOldSurfaceHeight;

			final float centerX = this.getCenterX();
			final float centerY = this.getCenterY();

			final float newWidthRaw = this.getWidthRaw() * surfaceWidthRatio;
			final float newHeightRaw = this.getHeightRaw() * surfaceHeightRatio;

			final float newWidthRawHalf = newWidthRaw * 0.5f;
			final float newHeightRawHalf = newHeightRaw * 0.5f;

			final float xMin = centerX - newWidthRawHalf;
			final float yMin = centerY - newHeightRawHalf;
			final float xMax = centerX + newWidthRawHalf;
			final float yMax = centerY + newHeightRawHalf;

			this.set(xMin, yMin, xMax, yMax);
		}

		this.surfaceX = pNewSurfaceX;
		this.surfaceY = pNewSurfaceY;
		this.surfaceWidth = pNewSurfaceWidth;
		this.surfaceHeight = pNewSurfaceHeight;
	}


}
