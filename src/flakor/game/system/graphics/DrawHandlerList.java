package flakor.game.system.graphics;

import flakor.game.core.camera.Camera;
import flakor.game.support.math.SmartList;
import flakor.game.system.graphics.opengl.GLState;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class DrawHandlerList  extends SmartList<DrawInterface> implements DrawInterface
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 290960052845719287L;

	public DrawHandlerList()
    {

    }

    public DrawHandlerList(final int pCapacity)
    {
        super(pCapacity);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onDraw(final GLState pGLState, final Camera pCamera)
    {
        final int handlerCount = this.size();
        for(int i = handlerCount - 1; i >= 0; i--) {
            this.get(i).onDraw(pGLState, pCamera);
        }
    }

}
