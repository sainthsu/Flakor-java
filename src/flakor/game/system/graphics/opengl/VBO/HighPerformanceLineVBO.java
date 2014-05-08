package flakor.game.system.graphics.opengl.VBO;

import flakor.game.core.geometry.Line;
import flakor.game.system.graphics.opengl.DrawType;

/**
 * Created by longjiyang on 13-7-10.
 */
public class HighPerformanceLineVBO extends HighPerformanceVBO implements LineVBOInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public HighPerformanceLineVBO(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes)
    {
        super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdateColor(final Line pLine)
    {
        final float[] bufferData = this.bufferData;

        final float packedColor = pLine.getColor().getABGRPackedFloat();//?

        bufferData[0 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;
        bufferData[1 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;

        this.setDirtyOnHardware();
    }

    @Override
    public void onUpdateVertices(final Line pLine)
    {
        final float[] bufferData = this.bufferData;

        bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = 0;
        bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_Y] = 0;

        bufferData[1 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = pLine.getX2() - pLine.getX1();
        bufferData[1 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_Y] = pLine.getY2() - pLine.getY1();

        this.setDirtyOnHardware();
    }
}
