package flakor.game.support.util;

/**
 * Created by longjiyang on 13-7-10.
 */
public class VertexUtils
{
    /**
     * @param pVertices
     * @param pVertexOffset
     * @param pVertexStride
     * @param pVertexIndex
     * @return the value of the <code>pVertexOffset</code>-th attribute of the <code>pVertexIndex</code>-th vertex.
     */
    public static float getVertex(final float[] pVertices, final int pVertexOffset, final int pVertexStride, final int pVertexIndex) {
        return pVertices[(pVertexIndex * pVertexStride) + pVertexOffset];
    }

}
