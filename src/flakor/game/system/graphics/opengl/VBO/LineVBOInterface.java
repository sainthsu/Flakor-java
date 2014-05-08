package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.geometry.Line;

/**
 * Created by Saint Hsu on 13-7-9.
 */
public interface LineVBOInterface  extends VBOInterface
{
    public void onUpdateColor(final Line pLine);
    public void onUpdateVertices(final Line pLine);
}
