package flakor.game.core.collision;

import flakor.game.core.camera.Camera;
import flakor.game.core.entity.Constants;
import flakor.game.core.entity.Entity;
import flakor.game.core.geometry.Line;
import flakor.game.core.geometry.RectangularShape;
import flakor.game.support.math.MathUtils;
import flakor.game.system.graphics.TransformMatrix;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public class RectangularShapeCollisionChecker extends ShapeCollisionChecker
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final int RECTANGULARSHAPE_VERTEX_COUNT = 4;

    private static final float[] VERTICES_CONTAINS_TMP = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];
    private static final float[] VERTICES_COLLISION_TMP_A = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];
    private static final float[] VERTICES_COLLISION_TMP_B = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];

    // ===========================================================
    // Methods
    // ===========================================================

    public static boolean checkContains(final float pLocalX, final float pLocalY, final float pLocalWidth, final float pLocalHeight, final TransformMatrix pLocalToSceneTransformation, final float pX, final float pY) {
        RectangularShapeCollisionChecker.fillVertices(pLocalX, pLocalY, pLocalWidth, pLocalHeight, pLocalToSceneTransformation, RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP);
        return checkContains(RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, pX, pY);
    }

    public static boolean checkContains(final Entity pEntity, final float pLocalWidth, final float pLocalHeight, final float pX, final float pY)
    {
        RectangularShapeCollisionChecker.fillVertices(pEntity.getX(), pEntity.getY(), pLocalWidth, pLocalHeight, pEntity.getLocalToSceneMatrix(), RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP);
        return checkContains(RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, pX, pY);
    }

    public static boolean checkContains(final RectangularShape pRectangularShape, final float pX, final float pY) {
        RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP);
        return checkContains(RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, pX, pY);
    }

    public static boolean isVisible(final Camera pCamera, final RectangularShape pRectangularShape) 
    {
        RectangularShapeCollisionChecker.fillVertices(pCamera, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
        RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

        return checkCollision(RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT);
    }

    public static boolean isVisible(final Camera pCamera, final float pX, final float pY, final float pWidth, final float pHeight, final TransformMatrix pLocalToSceneTransformation) {
        RectangularShapeCollisionChecker.fillVertices(pCamera, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
        RectangularShapeCollisionChecker.fillVertices(pX, pY, pWidth, pHeight, pLocalToSceneTransformation, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

        return checkCollision(RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT);
    }

    public static boolean isVisible(final Camera pCamera, final Line pLine)
    {
        RectangularShapeCollisionChecker.fillVertices(pCamera, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
        LineCollisionChecker.fillVertices(pLine, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

        return checkCollision(RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B, LineCollisionChecker.LINE_VERTEX_COUNT);
    }

    public static boolean checkCollision(final RectangularShape pRectangularShapeA, final RectangularShape pRectangularShapeB) {
        RectangularShapeCollisionChecker.fillVertices(pRectangularShapeA, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
        RectangularShapeCollisionChecker.fillVertices(pRectangularShapeB, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

        return checkCollision(RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT);
    }

    public static boolean checkCollision(final RectangularShape pRectangularShape, final Line pLine) {
        RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
        LineCollisionChecker.fillVertices(pLine, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

        return checkCollision(RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B, LineCollisionChecker.LINE_VERTEX_COUNT);
    }

    public static void fillVertices(final RectangularShape pRectangularShape, final float[] pVertices) 
    {
        RectangularShapeCollisionChecker.fillVertices(0, 0, pRectangularShape.getWidth(), pRectangularShape.getHeight(), pRectangularShape.getLocalToSceneMatrix(), pVertices);
    }

    public static void fillVertices(final float pLocalX, final float pLocalY, final float pLocalWidth, final float pLocalHeight, final TransformMatrix pLocalToSceneTransformation, final float[] pVertices) {
        final float localXMin = pLocalX;
        final float localXMax = pLocalX + pLocalWidth;
        final float localYMin = pLocalY;
        final float localYMax = pLocalY + pLocalHeight;

        pVertices[0 + Constants.VERTEX_INDEX_X] = localXMin;
        pVertices[0 + Constants.VERTEX_INDEX_Y] = localYMin;

        pVertices[2 + Constants.VERTEX_INDEX_X] = localXMax;
        pVertices[2 + Constants.VERTEX_INDEX_Y] = localYMin;

        pVertices[4 + Constants.VERTEX_INDEX_X] = localXMax;
        pVertices[4 + Constants.VERTEX_INDEX_Y] = localYMax;

        pVertices[6 + Constants.VERTEX_INDEX_X] = localXMin;
        pVertices[6 + Constants.VERTEX_INDEX_Y] = localYMax;

        pLocalToSceneTransformation.transform(pVertices);
    }

    private static void fillVertices(final Camera pCamera, final float[] pVertices) 
    {
        pVertices[0 + Constants.VERTEX_INDEX_X] = pCamera.getXMin();
        pVertices[0 + Constants.VERTEX_INDEX_Y] = pCamera.getYMin();

        pVertices[2 + Constants.VERTEX_INDEX_X] = pCamera.getXMax();
        pVertices[2 + Constants.VERTEX_INDEX_Y] = pCamera.getYMin();

        pVertices[4 + Constants.VERTEX_INDEX_X] = pCamera.getXMax();
        pVertices[4 + Constants.VERTEX_INDEX_Y] = pCamera.getYMax();

        pVertices[6 + Constants.VERTEX_INDEX_X] = pCamera.getXMin();
        pVertices[6 + Constants.VERTEX_INDEX_Y] = pCamera.getYMax();

        MathUtils.rotateAroundCenter(pVertices, pCamera.getRotation(), pCamera.getCenterX(), pCamera.getCenterY());
    }

}
