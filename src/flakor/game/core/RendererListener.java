package flakor.game.core;

import flakor.game.system.graphics.opengl.GLState;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public interface RendererListener
{
    public void onSurfaceCreated(final GLState pGlState);
    public void onSurfaceChanged(final GLState pGlState, final int pWidth, final int pHeight);
}
