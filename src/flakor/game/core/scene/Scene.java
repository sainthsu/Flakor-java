package flakor.game.core.scene;


import flakor.game.core.camera.Camera;
import flakor.game.core.entity.Entity;
import flakor.game.core.entity.EntityInterface;
import flakor.game.support.math.ZIndexSorter;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.input.touch.TouchEvent;

/**
 * Created by Saint Hsu on 13-7-8.
 * 场景类，可包含层，精灵等实体元素
 */

public abstract class Scene extends Entity implements SceneInterface
{	

    // ===========================================================
    // Constructors
    // ===========================================================

    public Scene()
    {
        super(0,0);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onManagedDraw(final GLState glState, final Camera camera)
    {
    	
                glState.pushProjectionGLMatrix();
                this.onApplyMatrix(glState, camera);

                if(this.childrenSortPending)
                {
                    ZIndexSorter.getInstance().sort(this.children);
                    this.childrenSortPending = false;
                }

                final int childCount = children.size();
                int i = 0;
                EntityInterface child = null;

        					 // Draw children behind this Entity. 
                for(; i < childCount; i++)
                {
                    child = children.get(i);
                    if(child.getZIndex() < 0)
                    {
                    	glState.loadModelViewGLMatrixIdentity();
                        child.onDraw(glState, camera);
                    }
                    else
                    {
                        break;
                    }
                }

                { // Draw children in front of this Entity.
                    for(; i < childCount; i++)
                    {
                        child = children.get(i);
                        //Debug.d("child=>"+child.toString());
                        glState.loadModelViewGLMatrixIdentity();
                        child.onDraw(glState, camera);
                    }
                }
                                
                glState.popProjectionGLMatrix();
	    /*
        */
    }


    protected void onApplyMatrix(final GLState glState, final Camera camera)
    {
        camera.onApplySceneMatrix(glState);
    }
    
    public boolean onSceneTouchEvent(final TouchEvent sceneTouchEvent)
    {
       boolean result = false;
       
       return result;
    }

    /**
     * 场景没有父实体
     * Scene has no parent
     * @param entity
     */
    @Override
    public void setParent(final EntityInterface entity)
    {

    }

    // ===========================================================
    // Methods
    // ===========================================================

}
