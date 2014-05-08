package flakor.game.core.layer;

import flakor.game.core.camera.Camera;
import flakor.game.core.geometry.AreaShapeInterface;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.input.touch.TouchEvent;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public class CameraLayer extends Layer
{
    protected Camera camera;

    // ===========================================================
    // Constructors
    // ===========================================================

    public CameraLayer()
    {
        this(null);
    }
    public CameraLayer(Camera camera)
    {
        this.camera = camera;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================


    public Camera getCamera()
    {
        return camera;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onLayerTouchEvent(final TouchEvent sceneTouchEvent)
    {
        if(this.camera == null)
        {
            return false;
        }
        else
        {
            this.camera.convertSceneToCameraSceneTouchEvent(sceneTouchEvent);

            final boolean handled = super.onLayerTouchEvent(sceneTouchEvent);

            if(handled)
            {
                return true;
            }
            else
            {
                this.camera.convertCameraSceneToSceneTouchEvent(sceneTouchEvent);
                return false;
            }
        }
    }

    protected void onApplyMatrix(final GLState glState, final Camera pCamera)
    {
        this.camera.onApplyCameraSceneMatrix(glState);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void centerShapeInCamera(final AreaShapeInterface areaShape)
    {
        final Camera camera = this.camera;
        areaShape.setPosition((camera.getWidth() - areaShape.getWidth()) * 0.5f, (camera.getHeight() - areaShape.getHeight()) * 0.5f);
    }

    public void centerShapeInCameraHorizontally(final AreaShapeInterface pAreaShape)
    {
        pAreaShape.setPosition((this.camera.getWidth() - pAreaShape.getWidth()) * 0.5f, pAreaShape.getY());
    }

    public void centerShapeInCameraVertically(final AreaShapeInterface areaShape)
    {
        areaShape.setPosition(areaShape.getX(), (this.camera.getHeight() - areaShape.getHeight()) * 0.5f);
    }

}
