package flakor.game.core.collision;

import flakor.game.core.entity.Constants;
import flakor.game.core.geometry.Line;

/**
 * Created by longjiyang on 13-7-10.
 */
public class LineCollisionChecker extends ShapeCollisionChecker
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int LINE_VERTEX_COUNT = 2;

    // ===========================================================
    // Methods
    // ===========================================================

    public static boolean checkLineCollision(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
        return ((relativeCCW(pX1, pY1, pX2, pY2, pX3, pY3) * relativeCCW(pX1, pY1, pX2, pY2, pX4, pY4) <= 0)
                && (relativeCCW(pX3, pY3, pX4, pY4, pX1, pY1) * relativeCCW(pX3, pY3, pX4, pY4, pX2, pY2) <= 0));
    }

    public static void fillVertices(final Line pLine, final float[] pVertices)
    {
        pVertices[0 + Constants.VERTEX_INDEX_X] = 0;
        pVertices[0 + Constants.VERTEX_INDEX_Y] = 0;

        pVertices[2 + Constants.VERTEX_INDEX_X] = pLine.getX2() - pLine.getX1();
        pVertices[2 + Constants.VERTEX_INDEX_Y] = pLine.getY2() - pLine.getY1();

        pLine.getLocalToSceneMatrix().transform(pVertices);
    }

}
