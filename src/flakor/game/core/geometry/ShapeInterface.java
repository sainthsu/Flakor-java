package flakor.game.core.geometry;

import flakor.game.core.entity.Entity;
import flakor.game.core.entity.EntityInterface;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;
import flakor.game.system.graphics.opengl.VBO.VBOInterface;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;
import flakor.game.system.input.touch.TouchAreaInterface;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public interface ShapeInterface extends EntityInterface,TouchAreaInterface,Entity.BlendableInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public boolean collidesWith(final ShapeInterface otherShape);

    public boolean isBlendingEnabled();
    public void setBlendingEnabled(final boolean blendingEnabled);

    public VertexBufferObjectManager getVertexBufferObjectManager();
    public VBOInterface getVertexBufferObject();

    public ShaderProgram getShaderProgram();
    public void setShaderProgram(final ShaderProgram pShaderProgram);

}
