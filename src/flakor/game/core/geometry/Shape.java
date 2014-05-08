package flakor.game.core.geometry;

import flakor.game.core.camera.Camera;
import flakor.game.core.entity.BlendFunction;
import flakor.game.core.entity.Entity;
import flakor.game.core.element.Point;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;
import flakor.game.system.graphics.opengl.Texture.TextureParams;
import flakor.game.system.graphics.opengl.VBO.VBOInterface;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.system.input.touch.TouchEvent;

/**
 * Created by Saint Hsu on 13-7-8.
 */

public abstract class Shape extends Entity implements ShapeInterface
{
    // ===========================================================
    // Fields
    // ===========================================================

    protected boolean blendingEnabled = false;
    protected BlendFunction blendFunction;

    protected ShaderProgram shaderProgram;

    // ===========================================================
    // Constructors
    // ===========================================================

    public Shape(final Point position, final ShaderProgram shaderProgram)
    {
        super(position);

        this.shaderProgram = shaderProgram;
        blendFunction = new BlendFunction();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public boolean isBlendingEnabled()
    {
        return this.blendingEnabled;
    }

    public void setBlendingEnabled(final boolean blendingEnabled)
    {
        this.blendingEnabled = blendingEnabled;
    }

    @Override
    public void setBlendFunction(BlendFunction blendFunc)
    {
        this.blendFunction = blendFunc;
    }

    @Override
    public BlendFunction getBlendFunction()
    {
        return this.blendFunction;
    }

    @Override
    public ShaderProgram getShaderProgram()
    {
        return this.shaderProgram;
    }

    @Override
    public void setShaderProgram(final ShaderProgram shaderProgram)
    {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public VertexBufferObjectManager getVertexBufferObjectManager()
    {
        return this.getVertexBufferObject().getVertexBufferObjectManager();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract void onUpdateVertices();

    @Override
    protected void preDraw(final GLState glState, final Camera camera)
    {
        if(this.blendingEnabled)
        {
            glState.enableBlend();
            glState.blendFunction(this.blendFunction.getBlendFunctionSource(), this.blendFunction.getBlendFunctionDestination());
        }
    }

    @Override
    protected void postDraw(final GLState glState, final Camera camera)
    {
        if(this.blendingEnabled)
        {
            glState.disableBlend();
        }
    }

    @Override
    public boolean onAreaTouched(final TouchEvent sceneTouchEvent, final float touchAreaLocalX, final float touchAreaLocalY)
    {
        return false;
    }

    @Override
    public void reset()
    {
        super.reset();

        this.blendFunction.setBlendFunction(BlendFunction.BLENDFUNCTION_SOURCE_DEFAULT, BlendFunction.BLENDFUNCTION_DESTINATION_DEFAULT);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        final VBOInterface vertexBufferObject = this.getVertexBufferObject();
        if((vertexBufferObject != null) && vertexBufferObject.isAutoDispose() && !vertexBufferObject.isDisposed())
        {
            vertexBufferObject.dispose();
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void initBlendFunction(final TextureInterface texture)
    {
        this.initBlendFunction(texture.getTextureOptions());
    }

    protected void initBlendFunction(final TextureParams textureOptions)
    {
        if(textureOptions.preMultiplyAlpha)
        {
            this.blendFunction.setBlendFunction(BlendFunction.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, BlendFunction.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
        }
    }

}
