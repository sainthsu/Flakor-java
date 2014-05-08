/*
 * AtlasSprite.java
 * Created on 9/8/13 8:19 PM
 *
 * ver0.0.1beta 9/8/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.sprite;

import android.opengl.GLES20;

import flakor.game.core.camera.Camera;
import flakor.game.core.element.Point;
import flakor.game.core.element.Size;
import flakor.game.system.graphics.opengl.DrawType;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Shader.PositionColorTextureCoordinatesShaderProgram;
import flakor.game.system.graphics.opengl.Shader.ShaderProgram;
import flakor.game.system.graphics.opengl.Texture.TextureRegionInterface;
import flakor.game.system.graphics.opengl.Texture.TiledTextureInterface;
import flakor.game.system.graphics.opengl.Texture.TiledTextureRegionInterface;
import flakor.game.system.graphics.opengl.VBO.HighPerformanceTiledSpriteVBO;
import flakor.game.system.graphics.opengl.VBO.TiledSpriteVBOInterface;
import flakor.game.system.graphics.opengl.VBO.VertexBufferObjectManager;

/**
 * Created by saint on 9/8/13.
 */
public class TiledSprite extends Sprite
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int VERTEX_SIZE = Sprite.VERTEX_SIZE;
    public static final int VERTICES_PER_TILEDSPRITE = 6;
    public static final int TILEDSPRITE_SIZE = TiledSprite.VERTEX_SIZE * TiledSprite.VERTICES_PER_TILEDSPRITE;

    // ===========================================================
    // Fields
    // ===========================================================

    private int mCurrentTileIndex;
    private final TiledSpriteVBOInterface mTiledSpriteVertexBufferObject;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TiledSprite(final Point position, final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
    {
        this(position, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public TiledSprite(final Point position,  final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram)
    {
        this(position, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public TiledSprite(final Point position, final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType)
    {
        this(position, new Size( pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight()), pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
    }

    public TiledSprite(final Point position, final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram)
    {
        this(position,new Size(pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight()), pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public TiledSprite(final Point position,  final TiledTextureInterface pTiledTextureRegion, final TiledSpriteVBOInterface pTiledSpriteVertexBufferObject)
    {
        this(position, new Size( pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight()), pTiledTextureRegion, pTiledSpriteVertexBufferObject);
    }

    public TiledSprite(final Point position, final TiledTextureInterface pTiledTextureRegion, final TiledSpriteVBOInterface pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram)
    {
        this(position,new Size( pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight()), pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
    }

    public TiledSprite(final Point position, final Size size, final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        this(position,size, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
    }

    public TiledSprite(final Point position, final Size size,final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
        this(position,size,pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
    }

    public TiledSprite(final Point position, final Size size,final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
        this(position,size, pTiledTextureRegion, new HighPerformanceTiledSpriteVBO(pVertexBufferObjectManager, TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTiledTextureRegion().getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
    }

    public TiledSprite(final Point position, final Size size,final TiledTextureInterface pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram)
    {
        this(position,size, pTiledTextureRegion, new HighPerformanceTiledSpriteVBO(pVertexBufferObjectManager, TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTiledTextureRegion().getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
    }

    public TiledSprite(final Point position, final Size size,final TiledTextureInterface pTiledTextureRegion, final TiledSpriteVBOInterface pTiledSpriteVertexBufferObject)
    {
        this(position,size,pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
    }

    public TiledSprite(final Point position, final Size size, final TiledTextureInterface pTiledTexture, final TiledSpriteVBOInterface pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram)
    {
        super(position,size,pTiledTexture , pTiledSpriteVertexBufferObject, pShaderProgram);

        this.mTiledSpriteVertexBufferObject = pTiledSpriteVertexBufferObject;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public TextureRegionInterface getTextureRegion()
    {
        return this.getTiledTexture().getTiledTextureRegion().getTextureRegion(this.mCurrentTileIndex);
    }

    public TiledTextureRegionInterface getTiledTextureRegion()
    {
        return this.getTiledTexture().getTiledTextureRegion();
    }

    public TiledTextureInterface getTiledTexture()
    {
        return (TiledTextureInterface)this.texture;
    }

    @Override
    public TiledSpriteVBOInterface getVertexBufferObject()
    {
        return (TiledSpriteVBOInterface)super.getVertexBufferObject();
    }

    @Override
    protected void draw(final GLState pGLState, final Camera pCamera)
    {
        this.mTiledSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mCurrentTileIndex * TiledSprite.VERTICES_PER_TILEDSPRITE, TiledSprite.VERTICES_PER_TILEDSPRITE);
    }

    @Override
    protected void onUpdateColor()
    {
        this.getVertexBufferObject().onUpdateColor(this);
    }

    @Override
    protected void onUpdateVertices()
    {
        this.getVertexBufferObject().onUpdateVertices(this);
    }

    @Override
    protected void onUpdateTextureCoordinates()
    {
        this.getVertexBufferObject().onUpdateTextureCoordinates(this);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public int getCurrentTileIndex()
    {
        return this.mCurrentTileIndex;
    }

    public void setCurrentTileIndex(final int pCurrentTileIndex)
    {
        this.mCurrentTileIndex = pCurrentTileIndex;
    }

    public int getTileCount()
    {
        return this.getTiledTexture().getTiledTextureRegion().getTileCount();
    }

}
