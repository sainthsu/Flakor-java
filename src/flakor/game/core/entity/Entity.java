package flakor.game.core.entity;

import flakor.game.core.camera.Camera;
import flakor.game.core.camera.EntityCamera;
import flakor.game.core.element.Color;
import flakor.game.core.element.Point;
import flakor.game.core.element.Rect;
import flakor.game.core.element.Size;
import flakor.game.core.entity.EntityModifierInterface.EntityModifierMatcher;
import flakor.game.core.modifier.ModifierManager;
import flakor.game.core.timer.Scheduler;
import flakor.game.support.math.SmartList;
import flakor.game.support.math.ZIndexSorter;
import flakor.game.system.graphics.TransformMatrix;
import flakor.game.system.graphics.UpdatableInterface;
import flakor.game.system.graphics.opengl.GLState;
import flakor.game.system.graphics.opengl.Texture.TextureInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  <p>CopyRight (c) 2013.5 Saint Hsu
 *  <p>实体元素是一个十分重要的类。任何能绘制和包含其他绘制元素的都是一个实体元素。
 *  <p>比较常用的实体有场景，层，精灵，菜单等。
 *  <p>Entity is the main element. Anything that gets drawn or contains things that get drawn is an Entity.
 *  <p>The most popular Entities are: Scene, Layer, Sprite, Menu.
 *  <p>
 *  <p>实体元素的主要特性有：
 *  <p>-他能包含其他实体元素节点（子元素，等等）
 *  <p>-他能够安排周期的回调
 *  <p>-他能够执行动画
 *  <p>有些实体元素还提供了额外的方法
 *  <p>The main features of a Entity are:
 *  <p>- They can contain other Entity nodes (attachChild, getChildByTag, detachChild, etc)
 *  <p>- They can schedule periodic callback (schedule, unschedule, etc)
 *  <p>- They can execute actions (runAction, stopAction, etc)
 *  <p>
 *  <p>Some Entity nodes provide extra functionality for them or their children.
 *  <p>
 *  <p>实体元素特征 Features of Entity:
 *  <p>- 位置 position
 *  <p>- 缩放 scale (x, y)
 *  <p>- 旋转 rotation (in degrees, clockwise)
 *  <p>- 倾斜 skew (x,y)
 *  <p>- 网格 gridBase (to do mesh transformations)
 *  <p>- 锚点 anchor point
 *  <p>- 尺寸 size
 *  <p>- 可见 visible
 *  <p>- z轴序列 zIndex
 *  <p>- opengl z轴 openGL z position
 *  <p>
 *  <p> 默然值 Default values:
 *  <p>- rotation: 0
 *  <p>- position: (x=0,y=0)
 *  <p>- scale: (x=1,y=1)
 *  <p>- contentSize: (x=0,y=0)
 *  <p>- anchorPoint: (x=0,y=0)
 *  <p>限制：
 *  <p>一个实体元素没有贴图！
 *  <p>Limitations:
 *  <p>- An Entity is a "void" object. It doesn't have a texture
 */

public class Entity implements EntityInterface
{
	
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CHILDREN_CAPACITY_DEFAULT = 4;
	private static final int ENTITYMODIFIERS_CAPACITY_DEFAULT = 4;
	private static final int UPDATEHANDLERS_CAPACITY_DEFAULT = 4;

	private static final float[] VERTICES_SCENE_TO_LOCAL_TMP = new float[2];
	private static final float[] VERTICES_LOCAL_TO_SCENE_TMP = new float[2];
	
	private static final ParameterCallable<EntityInterface> PARAMETERCALLABLE_DETACHCHILD = new ParameterCallable<EntityInterface>()
	{
		
		@Override
		public void call(final EntityInterface entity)
		{
			// TODO Auto-generated method stub
			entity.setParent(null);
			entity.onDetached();
		}
	};
	
	// ===========================================================
	// Fields
	// ===========================================================

	//锚点比例
    //protected PointF anchorPercent;
    //private BaseGrid grid;
    /**
     * 位置,左下角的点
     */
    protected Point position;
    /**
     * 尺寸大小
     */
    protected Size contentSize;
    /**
     * 锚点位置，用于变换定位等，实际的锚点是width*anchorPosition.x height*anchorPosition.y
     */
    protected Point anchorPosition;
    /**
     * 元素相机
     */
    private EntityCamera camera;
    /**
     * 锚点是否发生作用，如果起左右，那么旋转中点等点将失效，变成根据锚点进行矩阵变换
     */
    private boolean relativeAnchorPoint=false;
    /**
     * 是否选中
     */
    private boolean selected;
    /**
     * 是否运行中...
     */
    private boolean running;

    /**
     * 是否已废弃
     */
	protected boolean disposed;
    /**
     * 是否显示
     */
    private boolean enabled;
    /**
     * 是否可见
     */
	protected boolean visible = true;
	/**
	 * 是否剔除
	 */
	protected boolean cullingEnabled;
	/**
	 * 是否忽略更新
	 */
	protected boolean ignoreUpdate;
	/**
	 * 子实体元素是否可见
	 */
	protected boolean childrenVisible = true;
	/**
	 * 子实体是否忽略更新
	 */
	protected boolean childrenIgnoreUpdate;
	/**
	 * 子实体是否正等待排序
	 */
	protected boolean childrenSortPending;
	
	protected int tag = EntityInterface.TAG_INVALID;
	/**
	 * 渲染顺序，根据Zindex的顺序渲染，小于零表示在此实体后面，要先渲染，大于零则表示在此实体前面，后渲染
	 */
	protected int Zindex = 0;
	/**
	 * 颜色，默认白色
	 */
	protected Color color = new Color(1, 1, 1, 1);

	/**
	 * 旋转角度
	 */
	protected float rotation = 0;
	/**
	 * 旋转中心点
	 */
    protected Point rotationCenter = Point.makeZero();

    /**
     * X，Y轴的缩放
     */
	protected float scaleX = 1;
	protected float scaleY = 1;
	/**
	 * 缩放中心点
	 */
    protected Point scaleCenter = Point.makeZero();

    /**
     * X，Y轴的倾斜
     */
	protected float skewX = 0;
	protected float skewY = 0;
	/**
	 * 倾斜中心点
	 */
    protected Point skewCenter;

    //本身到父实体的矩阵变换标识
	private boolean localToParentTransformationDirty = true;
	private boolean parentToLocalTransformationDirty = true;

	//本身坐标变换到父实体的矩阵
	private TransformMatrix localToParentMatrix;
	private TransformMatrix parentToLocalMatrix;

	//本身坐标变换到场景的矩阵
	private TransformMatrix localToSceneMatrix;
	private TransformMatrix sceneToLocalMatrix;

	//矩阵变换和反向变换矩阵
    private TransformMatrix transformMatrix;
    private TransformMatrix inverseMatrix;

    /**
     * 变换和反向标识
     */
    protected boolean transformDirty;
    protected boolean inverseDirty;
    /**
     * 节点在OpenGL中的z order值
     */
    private float vertexZ = 0f;
    
    /**
     * 父实体元素
     */
    private EntityInterface parent;
    /**
     * 子实体队列
     */
    protected SmartList<EntityInterface> children;
    /**
     * 元素调节器队列
     */
    private EntityModifierList entityModifiers;
    /**
     * 更新队列
     */
    private UpdateHandlerList updateHandlers;
    /**
     * 定时器的hash图
     */
    private HashMap<String, Scheduler.Timer> scheduledSelectors;
    
    /**
     * 用户自定义数据
     */
	private Object userData;

    // ===========================================================
	// Constructors
	// ===========================================================
	
    
	public Entity()
	{
		this(0,0);
	}

    public Entity(Point position)
    {
        this.position = position;
        this.anchorPosition = Point.make(0.0f,0.0f);
    }

	public Entity(float x, float y)
	{
		this.position = Point.make(x,y);
        this.anchorPosition = Point.make(0.0f,0.0f);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================


    public Size getContentSize()
    {
        return this.contentSize;
    }

    public void setContentSize(Size contentSize)
    {
    	if(!contentSize.equals(this.contentSize))
    		this.contentSize = contentSize;
    }

    public Point getAnchorPosition()
    {
        return anchorPosition;
    }

    public void setAnchorPosition(Point anchorPosition)
    {
        this.anchorPosition = anchorPosition;
    }

    public boolean isRelativeAnchorPoint()
    {
        return this.relativeAnchorPoint;
    }

    public void setRelativeAnchorPoint(boolean relativeAnchorPoint)
    {
        this.relativeAnchorPoint = relativeAnchorPoint;
    }

    public boolean isSelected()
    {
        return this.selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isRunning()
    {
        return this.running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public boolean isTransformDirty()
    {
        return this.transformDirty;
    }

    public void setTransformDirty(boolean transformDirty)
    {
        this.transformDirty = transformDirty;
    }

    public boolean isInverseDirty()
    {
        return inverseDirty;
    }

    public void setInverseDirty(boolean inverseDirty)
    {
        this.inverseDirty = inverseDirty;
    }

    public float getVertexZ()
    {
        return vertexZ;
    }

    public void setVertexZ(float vertexZ)
    {
        this.vertexZ = vertexZ;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
	public boolean isVisible()
	{
		// TODO Auto-generated method stub
		return this.visible;
	}

	@Override
	public void setVisible(final boolean visible)
	{
		// TODO Auto-generated method stub
		this.visible = visible;
	}

	@Override
	public boolean isIgnoreUpdate()
	{
		// TODO Auto-generated method stub
		return this.ignoreUpdate;
	}

	@Override
	public void setIgnoreUpdate(final boolean ignoreUpdate)
	{
		// TODO Auto-generated method stub
		this.ignoreUpdate = ignoreUpdate;
	}

	@Override
	public boolean isChildrenVisible()
	{
		// TODO Auto-generated method stub
		return this.childrenVisible;
	}

	@Override
	public void setChildrenVisible(final boolean childrenVisible)
	{
		// TODO Auto-generated method stub
		this.childrenVisible = childrenVisible;
	}

	@Override
	public boolean isChildrenIgnoreUpdate()
	{
		// TODO Auto-generated method stub
		return this.childrenIgnoreUpdate;
	}

	@Override
	public void setChildrenIgnoreUpdate(final boolean childrenIgnoreUpdate)
	{
		// TODO Auto-generated method stub
		this.childrenIgnoreUpdate = childrenIgnoreUpdate;
	}

	@Override
	public int getTag()
	{
		// TODO Auto-generated method stub
		return this.tag;
	}

	@Override
	public void setTag(int tag)
	{
		// TODO Auto-generated method stub
		this.tag = tag;
	}

	@Override
	public int getZIndex()
	{
		// TODO Auto-generated method stub
		return this.Zindex;
	}

	@Override
	public void setZIndex(int ZIndex)
	{
		// TODO Auto-generated method stub
		this.Zindex = ZIndex;
		if(this.hasParent())
		{
			this.parent.sortChildren();
		}
	}

	@Override
	public boolean hasParent()
	{
		// TODO Auto-generated method stub
		return this.parent!=null;
	}

	@Override
	public EntityInterface getParent()
	{
		// TODO Auto-generated method stub
		return this.parent;
	}

	@Override
	public void setParent(final EntityInterface entity)
	{
		// TODO Auto-generated method stub
		this.parent = entity;
	}

	@Override
	public float getX()
	{
		// TODO Auto-generated method stub
		return this.position.x;
	}

	@Override
	public float getY() 
	{
		// TODO Auto-generated method stub
		return this.position.y;
	}

    @Override
    public Point getPosition()
    {
        return this.position;
    }

	@Override
	public void setX(float x) 
	{
		// TODO Auto-generated method stub
		this.position.x = x;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setY(float y) 
	{
		// TODO Auto-generated method stub
		this.position.y = y ;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setPosition(EntityInterface otherEntity)
	{
		// TODO Auto-generated method stub
		this.setPosition(otherEntity.getPosition());
	}

	@Override
	public void setPosition(float x, float y)
	{
		// TODO Auto-generated method stub
		this.position.x = x;
		this.position.y = y;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

    @Override
    public void setPosition(Point position)
    {
        this.position = position;

        this.localToParentTransformationDirty = true;
        this.parentToLocalTransformationDirty = true;
    }

    @Override
	public boolean isRotated()
	{
		// TODO Auto-generated method stub
		return this.rotation != 0;
	}

	@Override
	public float getRotation()
    {
		// TODO Auto-generated method stub
		return this.rotation;
	}

	@Override
	public void setRotation(float rotation)
	{
		// TODO Auto-generated method stub
		this.rotation = rotation;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public float getRotationCenterX()
	{
		// TODO Auto-generated method stub
		return this.rotationCenter.x;
	}

	@Override
	public float getRotationCenterY()
	{
		// TODO Auto-generated method stub
		return this.rotationCenter.y;
	}

    @Override
    public Point getRotationCenter()
    {
        return this.rotationCenter;
    }

    @Override
	public void setRotationCenterX(float rotationCenterX)
	{
		// TODO Auto-generated method stub
		this.rotationCenter.x = rotationCenterX;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenterY(float rotationCenterY)
	{
		// TODO Auto-generated method stub
		this.rotationCenter.y = rotationCenterY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenter(float rotationCenterX, float rotationCenterY)
	{
		// TODO Auto-generated method stub
		this.rotationCenter.x = rotationCenterX;
		this.rotationCenter.y = rotationCenterY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

    @Override
    public void setRotationCenter(Point rotationCenter)
    {
        this.rotationCenter = rotationCenter;

        this.localToParentTransformationDirty = true;
        this.parentToLocalTransformationDirty = true;
    }

    @Override
	public boolean isScaled()
	{
		return (this.scaleX != 1)||(this.scaleY != 1);
	}

	@Override
	public float getScaleX()
	{
		return this.scaleX;
	}

	@Override
	public float getScaleY()
	{
		// TODO Auto-generated method stub
		return this.scaleY;
	}

	@Override
	public void setScaleX(float scaleX)
	{
		// TODO Auto-generated method stub
		this.scaleX = scaleX;
	}

	@Override
	public void setScaleY(float scaleY)
	{
		// TODO Auto-generated method stub
		this.scaleY = scaleY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setScale(float scale)
	{
		// TODO Auto-generated method stub
		this.scaleX = scale;
		this.scaleY = scale;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setScale(float scaleX, float scaleY)
	{
		// TODO Auto-generated method stub
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public float getScaleCenterX()
	{
		// TODO Auto-generated method stub
		return this.scaleCenter.x;
	}

	@Override
	public float getScaleCenterY()
	{
		// TODO Auto-generated method stub
		return this.scaleCenter.y;
	}

	@Override
	public void setScaleCenterX(float scaleCenterX)
	{
		// TODO Auto-generated method stub
		this.scaleCenter.x = scaleCenterX;

        this.transformDirty = true;
        this.inverseDirty = true;
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenterY(float scaleCenterY)
	{
		// TODO Auto-generated method stub
		this.scaleCenter.y = scaleCenterY;

        this.transformDirty = true;
        this.inverseDirty = true;
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenter(float scaleCenterX, float scaleCenterY)
	{
		// TODO Auto-generated method stub
		this.scaleCenter.x = scaleCenterX;
		this.scaleCenter.y = scaleCenterY;

		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public boolean isSkewed()
	{
		// TODO Auto-generated method stub
		return (this.skewX != 0) || (this.skewY != 0);
	}

	@Override
	public float getSkewX()
	{
		// TODO Auto-generated method stub
		return this.skewX;
	}

	@Override
	public float getSkewY() 
	{
		// TODO Auto-generated method stub
		return this.scaleY;
	}

	@Override
	public void setSkewX(float skewX)
	{
		// TODO Auto-generated method stub
		this.skewX = skewX;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewY(float skewY)
	{
		// TODO Auto-generated method stub
		this.skewY = skewY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkew(float skew)
	{
		// TODO Auto-generated method stub
		this.skewX = skew;
		this.skewY = skew;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkew(float skewX, float skewY)
	{
		// TODO Auto-generated method stub
		this.skewX = skewX;
		this.skewY = skewY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

	@Override
	public float getSkewCenterX()
	{
		// TODO Auto-generated method stub
		return this.skewCenter.x;
	}

	@Override
	public float getSkewCenterY()
	{
		// TODO Auto-generated method stub
		return this.skewCenter.y;
	}

    @Override
    public Point getSkewCenter() {
        return this.skewCenter;
    }

    @Override
	public void setSkewCenterX(float skewCenterX)
	{
		// TODO Auto-generated method stub
		this.skewCenter.x = skewCenterX;

        this.localToParentTransformationDirty = true;
        this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenterY(float skewCenterY)
	{
		// TODO Auto-generated method stub
		this.skewCenter.y = skewCenterY;

        this.localToParentTransformationDirty = true;
        this.parentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenter(float skewCenterX, float skewCenterY)
	{
		// TODO Auto-generated method stub
		this.skewCenter.x = skewCenterX;
		this.skewCenter.y = skewCenterY;
		
		this.localToParentTransformationDirty = true;
		this.parentToLocalTransformationDirty = true;
	}

    @Override
    public void setSkewCenter(final Point skewCenter)
    {
        if(this.skewCenter!=null)
        {
            this.skewCenter.x = skewCenter.x;
            this.skewCenter.y = skewCenter.y;
        }
        else
        {
            this.rotationCenter = Point.make(skewCenter.x,skewCenter.y);
        }
        this.localToParentTransformationDirty = true;
        this.parentToLocalTransformationDirty = true;
    }

    @Override
	public boolean isRotatedOrScaledOrSkewed()
	{
		// TODO Auto-generated method stub
		return (this.rotation != 0) || (this.scaleX != 1) || (this.scaleY != 1) || (this.skewX != 0) || (this.skewY != 0);
	}

	@Override
	public float getRed()
	{
		// TODO Auto-generated method stub
		return this.color.getRed();
	}

	@Override
	public float getGreen()
	{
		// TODO Auto-generated method stub
		return this.color.getGreen();
	}

	@Override
	public float getBlue()
	{
		// TODO Auto-generated method stub
		return this.color.getBlue();
	}

	@Override
	public float getAlpha()
	{
		// TODO Auto-generated method stub
		return this.color.getAlpha();
	}

	@Override
	public Color getColor()
	{
		// TODO Auto-generated method stub
		return this.color;
	}

	@Override
	public void setRed(final float red)
	{
		// TODO Auto-generated method stub
		if(this.color.setRedChecking(red))
			this.onUpdateColor();
	}

	@Override
	public void setGreen(float green)
	{
		// TODO Auto-generated method stub
		if(this.color.setGreenChecking(green))
			this.onUpdateColor();
	}

	@Override
	public void setBlue(float blue)
	{
		// TODO Auto-generated method stub
		if(this.color.setBlueChecking(blue))
			this.onUpdateColor();
	}

	@Override
	public void setAlpha(float alpha)
	{
		// TODO Auto-generated method stub
		if(this.color.setAlphaChecking(alpha))
			this.onUpdateColor();
	}

	@Override
	public void setColor(final Color color) 
	{
		// TODO Auto-generated method stub
		if(this.color.setChecking(color));
		    this.onUpdateColor();
	}

	@Override
	public void setColor(float red, float green, float blue)
	{
		// TODO Auto-generated method stub
		if(this.color.setChecking(red, green, blue))
			this.onUpdateColor();
	}

	@Override
	public void setColor(float red, float green, float blue, float alpha)
	{
		// TODO Auto-generated method stub
		if(this.color.setChecking(red, green, blue, alpha))
			this.onUpdateColor();
	}

	@Override
	public boolean isCullingEnabled()
	{
		// TODO Auto-generated method stub
		return this.cullingEnabled;
	}

	@Override
	public void setCullingEnabled(boolean cullingEnabled)
	{
		// TODO Auto-generated method stub
		this.cullingEnabled = cullingEnabled;
	}

	@Override
	public boolean isCulled(Camera camera)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUserData(Object userData)
	{
		// TODO Auto-generated method stub
		this.userData = userData;
	}

	@Override
	public Object getUserData()
	{
		// TODO Auto-generated method stub
		return this.userData;
	}

	@Override
	public float[] getSceneCenterCoordinates()
	{
		// TODO Auto-generated method stub
		return this.convertLocalToSceneCoordinates(0, 0);
	}

	@Override
	public float[] getSceneCenterCoordinates(float[] reuse)
	{
		// TODO Auto-generated method stub
		return this.convertLocalToSceneCoordinates(0, 0, reuse);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(float x, float y)
	{
		// TODO Auto-generated method stub
		return this.convertLocalToSceneCoordinates(x, y, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(float x, float y,
			float[] reuse)
	{
		// TODO Auto-generated method stub
		final TransformMatrix localToSceneMatrix = this.getLocalToSceneMatrix();

		reuse[Constants.VERTEX_INDEX_X] = x;
		reuse[Constants.VERTEX_INDEX_Y] = y;

		localToSceneMatrix.transform(reuse);

		return reuse;
	}

	@Override
	public float[] convertLocalToSceneCoordinates(float[] coordinates)
	{
		// TODO Auto-generated method stub
		return this.convertLocalToSceneCoordinates(coordinates, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	@Override
	public float[] convertLocalToSceneCoordinates(float[] coordinates,
			float[] reuse)
	{
		// TODO Auto-generated method stub
		final TransformMatrix localToSceneMatrix = this.getLocalToSceneMatrix();

		reuse[Constants.VERTEX_INDEX_X] = coordinates[Constants.VERTEX_INDEX_X];
		reuse[Constants.VERTEX_INDEX_Y] = coordinates[Constants.VERTEX_INDEX_Y];

		localToSceneMatrix.transform(reuse);

		return reuse;
	}

	@Override
	public float[] convertSceneToLocalCoordinates(float x, float y)
	{
		// TODO Auto-generated method stub
		return this.convertSceneToLocalCoordinates(x, y, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(float x, float y,float[] reuse)
	{
		// TODO Auto-generated method stub
		reuse[Constants.VERTEX_INDEX_X] = x;
		reuse[Constants.VERTEX_INDEX_Y] = y;
		
		this.getSceneToLocalMatrix().transform(reuse);
		return reuse;
	}

	@Override
	public float[] convertSceneToLocalCoordinates(float[] coordinates)
	{
		// TODO Auto-generated method stub
		return this.convertSceneToLocalCoordinates(coordinates, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertSceneToLocalCoordinates(float[] coordinates,float[] reuse)
	{
		// TODO Auto-generated method stub
		reuse[Constants.VERTEX_INDEX_X] = coordinates[Constants.VERTEX_INDEX_X];
		reuse[Constants.VERTEX_INDEX_Y] = coordinates[Constants.VERTEX_INDEX_Y];
		
		this.getSceneToLocalMatrix().transform(reuse);
		return reuse;
	}

	/**
	 * 递归获取当前坐标转化为场景坐标的矩阵。
	 */
	@Override
	public TransformMatrix getLocalToSceneMatrix()
	{
		// TODO Auto-generated method stub
		if(this.localToSceneMatrix == null)
		{
			this.localToSceneMatrix = new TransformMatrix();
		}
		
		final TransformMatrix localToSceneMatrix = this.localToSceneMatrix;
		localToSceneMatrix.set(this.getLocalToParentMatrix());
		
		final EntityInterface parent = this.parent;
		if(parent != null)
		{
			localToSceneMatrix.concat(parent.getLocalToSceneMatrix());
		}
		
		return localToSceneMatrix;
	}

	@Override
	public TransformMatrix getSceneToLocalMatrix()
	{
		// TODO Auto-generated method stub
		if(this.sceneToLocalMatrix == null)
		{
			this.sceneToLocalMatrix = new TransformMatrix();
		}
		
		final TransformMatrix sceneToLocalMatrix = this.sceneToLocalMatrix;
		sceneToLocalMatrix.set(this.getParentToLocalMatrix());
		
		final EntityInterface parent = this.parent;
		if(parent != null)
		{
			sceneToLocalMatrix.preConcat(parent.getLocalToSceneMatrix());
		}
		
		return sceneToLocalMatrix;
	}

	@Override
	public TransformMatrix getLocalToParentMatrix()
	{
		// TODO Auto-generated method stub
		if(this.localToParentMatrix == null)
		{
			this.localToParentMatrix = new TransformMatrix();
		}
		
		final TransformMatrix localToParentMatrix = this.localToParentMatrix;
		if(this.localToParentTransformationDirty)
		{
			localToParentMatrix.identity();
			
			/* Scale. */
			final float scaleX = this.scaleX;
			final float scaleY = this.scaleY;
			if((scaleX != 1) || (scaleY != 1)) 
			{
				final float scaleCenterX = this.scaleCenter.x;
				final float scaleCenterY = this.scaleCenter.y;

				/* TODO Check if it is worth to check for scaleCenterX == 0 && scaleCenterY == 0 
				 * as the two postTranslate can be saved.The same obviously applies for all
				 *  similar occurrences of this pattern in this class.
				 */

				localToParentMatrix.translate(-scaleCenterX, -scaleCenterY);
				localToParentMatrix.scale(scaleX, scaleY);
				localToParentMatrix.translate(scaleCenterX, scaleCenterY);
			}

			/* Skew. */
			final float skewX = this.skewX;
			final float skewY = this.skewY;
			if((skewX != 0) || (skewY != 0))
			{
				final float skewCenterX = this.skewCenter.x;
				final float skewCenterY = this.skewCenter.y;

				localToParentMatrix.translate(-skewCenterX, -skewCenterY);
				localToParentMatrix.skew(skewX, skewY);
				localToParentMatrix.translate(skewCenterX, skewCenterY);
			}

			/* Rotation. */
			final float rotation = this.rotation;
			if(rotation != 0)
			{
				final float rotationCenterX = this.rotationCenter.x;
				final float rotationCenterY = this.rotationCenter.y;

				localToParentMatrix.translate(-rotationCenterX, -rotationCenterY);
				localToParentMatrix.rotate(rotation);
				localToParentMatrix.translate(rotationCenterX, rotationCenterY);
			}

			/* Translation. */
			localToParentMatrix.translate(this.position.x, this.position.y);

			this.localToParentTransformationDirty = false;
		}
		
		return localToParentMatrix;
	}

	@Override
	public TransformMatrix getParentToLocalMatrix()
	{
		// TODO Auto-generated method stub
		if(this.parentToLocalMatrix == null)
		{
			this.parentToLocalMatrix = new TransformMatrix();
		}

		final TransformMatrix parentToLocalMatrix = this.parentToLocalMatrix;
		if(this.parentToLocalTransformationDirty) 
		{
			parentToLocalMatrix.identity();

			/* Translation. */
			parentToLocalMatrix.translate(-this.position.x, -this.position.y);

			/* Rotation. */
			final float rotation = this.rotation;
			if(rotation != 0)
			{
				final float rotationCenterX = this.rotationCenter.x;
				final float rotationCenterY = this.rotationCenter.y;

				parentToLocalMatrix.translate(-rotationCenterX, -rotationCenterY);
				parentToLocalMatrix.rotate(-rotation);
				parentToLocalMatrix.translate(rotationCenterX, rotationCenterY);
			}

			/* Skew. */
			final float skewX = this.skewX;
			final float skewY = this.skewY;
			if((skewX != 0) || (skewY != 0))
			{
				final float skewCenterX = this.skewCenter.x;
				final float skewCenterY = this.skewCenter.y;

				parentToLocalMatrix.translate(-skewCenterX, -skewCenterY);
				parentToLocalMatrix.skew(-skewX, -skewY);
				parentToLocalMatrix.translate(skewCenterX, skewCenterY);
			}

			/* Scale. */
			final float scaleX = this.scaleX;
			final float scaleY = this.scaleY;
			if((scaleX != 1) || (scaleY != 1))
			{
				final float scaleCenterX = this.scaleCenter.x;
				final float scaleCenterY = this.scaleCenter.y;

				parentToLocalMatrix.translate(-scaleCenterX, -scaleCenterY);
				parentToLocalMatrix.scale(1 / scaleX, 1 / scaleY);
				// TODO Division could be replaced by a multiplication of 'scale(X/Y)Inverse'...
				parentToLocalMatrix.translate(scaleCenterX, scaleCenterY);
			}

			this.parentToLocalTransformationDirty = false;
		}
		return parentToLocalMatrix;
	}

	@Override
	public int getChildCount()
	{
		if(this.children == null)
		{
			return 0;
		}
		
		return this.children.size();
	}

	@Override
	public EntityInterface getChildByTag(int tag)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		
		for(int i=this.children.size()-1;i>=0;i--)
		{
			final EntityInterface child = this.children.get(i);
			if(child.getTag() == tag)
				return child;
				
		}
		return null;
	}

	@Override
	public EntityInterface getChildByMatcher(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		return this.children.get(entityMatcher);
	}

	@Override
	public EntityInterface getChildByIndex(int index)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		return this.children.get(index);
	}
	
	@Override
	public EntityInterface getChildByZIndex(int index)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		for(int i=this.children.size()-1;i>=0;i--)
		{
			final EntityInterface child = this.children.get(i);
			if(child.getZIndex() == index)
				return child;
				
		}
		return null;
	}

	@Override
	public EntityInterface getFirstChild()
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		
		return this.children.get(0);
	}

	@Override
	public EntityInterface getLastChild()
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		return this.children.get(this.children.size()-1);
	}
	
	
	@Override
	public boolean isDisposed()
	{
		// TODO Auto-generated method stub
		return this.disposed;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		if(!this.disposed)
		{
			this.disposed = true;
		} 
		else 
		{
			throw new AlreadyDisposedException();
		}
	}

	@Override
	public ArrayList<EntityInterface> query(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		return this.query(entityMatcher, new ArrayList<EntityInterface>());
	}

	@Override
	public EntityInterface queryFirst(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		return this.queryFirstForSubclass(entityMatcher);
	}

	@Override
	public <L extends List<EntityInterface>> L query(EntityMatcher entityMatcher, L result)
	{
		// TODO Auto-generated method stub
		final int childCount = this.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			final EntityInterface child = this.children.get(i);
			if(entityMatcher.matches(child))
			{
				result.add(child);
			}

			child.query(entityMatcher, result);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends EntityInterface> S queryFirstForSubclass(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		final int childCount = this.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			final EntityInterface child = this.children.get(i);
			if(entityMatcher.matches(child))
			{
				return (S)child;
			}

			final S childQueryFirst = child.queryFirstForSubclass(entityMatcher);
			if(childQueryFirst != null)
			{
				return childQueryFirst;
			}
		}

		return null;
	}

	@Override
	public <S extends EntityInterface> ArrayList<S> queryForSubclass(EntityMatcher entityMatcher) throws ClassCastException
	{
		// TODO Auto-generated method stub
		return this.queryForSubclass(entityMatcher,new ArrayList<S>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <L extends List<S>, S extends EntityInterface> L queryForSubclass(EntityMatcher entityMatcher, L result)throws ClassCastException
	{
		// TODO Auto-generated method stub
		final int childCount = this.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			final EntityInterface child = this.children.get(i);
			if(entityMatcher.matches(child))
			{
				result.add((S)child);
			}

			child.queryForSubclass(entityMatcher, result);
		}

		return result;
	}

	@Override
	public void sortChildren()
	{
		// TODO Auto-generated method stub
		this.sortChildren(true);
	}

	@Override
	public void sortChildren(boolean immediate)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return;
		}
		if(immediate == true)
		{
			ZIndexSorter.getInstance().sort(this.children);
		}
		else
		{
			this.childrenSortPending = true ;
		}
	}

	@Override
	public void sortChildren(EntityComparator entityComparator)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return;
		}
		
		ZIndexSorter.getInstance().sort(this.children,entityComparator);
	}
	
	@Override
	public boolean detachSelf()
	{
		// TODO Auto-generated method stub
		final EntityInterface parent = this.parent;
		if(parent != null)
		{
			return parent.detachChild(this);
		}
		else
		{
			return false;
		}
	}

    @Override
    public void attachChild(EntityInterface entity) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        int z = entity.getZIndex();
        this.attachChild(entity, z);
    }

    @Override
    public void attachChild(EntityInterface entity,int ZIndex) throws IllegalStateException
    {
        // TODO Auto-generated method stub
        this.assertEntityHasNoParent(entity);

        if(this.children == null)
        {
            this.allocateChildren();
        }

        boolean added=false;
        for (int i = 0; i < this.children.size(); ++i)
        {
            final EntityInterface child = this.children.get(i);
            if (child.getZIndex() > ZIndex)
            {
                added = true;
                this.children.add(i, entity);
                break;
            }
        }
        if (!added)
        {
            this.children.add(entity);
        }
        entity.setParent(this);
        if(isRunning())
        {
            entity.onAttached();
        }
    }
    
    @Override
	public boolean detachChild(EntityInterface entity)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return false;
		}
		return this.children.remove(entity, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public EntityInterface detachChild(int tag)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		for(int i = this.children.size() - 1; i >= 0; i--)
		{
			if(this.children.get(i).getTag() == tag)
			{
				final EntityInterface removed = this.children.remove(i);
				Entity.PARAMETERCALLABLE_DETACHCHILD.call(removed);
				return removed;
			}
		}
		return null;
	}

	@Override
	public EntityInterface detachChild(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return null;
		}
		return this.children.remove(entityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}
	
	@Override
	public boolean detachChildren(EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return false;
		}
		return this.children.removeAll(entityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public void detachChildren()
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return;
		}
		this.children.clear(Entity.PARAMETERCALLABLE_DETACHCHILD);
	}
	
    @Override
	public void callOnChildren(EntityParameterCallable entityParameterCallable)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return;
		}
		this.children.call(entityParameterCallable);
	}

	@Override
	public void callOnChildren(
			EntityParameterCallable entityParameterCallable,
			EntityMatcher entityMatcher)
	{
		// TODO Auto-generated method stub
		if(this.children == null)
		{
			return;
		}
		this.children.call(entityMatcher, entityParameterCallable);
	}

	@Override
	public void registerUpdateHandler(UpdatableInterface updateHandler)
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers == null)
		{
			this.allocateUpdateHandlers();
		}
		this.updateHandlers.add(updateHandler);
	}

	@Override
	public boolean unregisterUpdateHandler(UpdatableInterface updateHandler)
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers == null) 
		{
			return false;
		}
		return this.updateHandlers.remove(updateHandler);
	}

	@Override
	public boolean unregisterUpdateHandlers(UpdateMatcher updateHandlerMatcher)
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers == null) 
		{
			return false;
		}
		return this.updateHandlers.removeAll(updateHandlerMatcher);
	}

	@Override
	public int getUpdateHandlerCount()
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers == null) 
		{
			return 0;
		}
		return this.updateHandlers.size();
	}

	@Override
	public void clearUpdateHandlers()
	{
		// TODO Auto-generated method stub
		if(this.updateHandlers == null) 
		{
			return;
		}
		this.updateHandlers.clear();
	}

	@Override
	public void registerEntityModifier(EntityModifierInterface entityModifier)
	{
		// TODO Auto-generated method stub
		if(this.entityModifiers == null)
		{
			this.allocateEntityModifiers();
		}
		this.entityModifiers.add(entityModifier);
	}

	@Override
	public boolean unregisterEntityModifier(EntityModifierInterface entityModifier)
	{
		// TODO Auto-generated method stub
		if(this.entityModifiers == null)
		{
			return false;
		}
		return this.entityModifiers.remove(entityModifier);
	}

	@Override
	public boolean unregisterEntityModifierMatcher(EntityModifierMatcher entityModifierMatcher)
	{
		// TODO Auto-generated method stub
		if(this.entityModifiers == null)
		{
			return false;
		}
		return this.entityModifiers.removeAll(entityModifierMatcher);
	}

	@Override
	public int getEntityModifierCount() 
	{
		// TODO Auto-generated method stub
		if(this.entityModifiers == null)
		{
			return 0;
		}
		return this.entityModifiers.size();
	}

	@Override
	public void clearEntityModifiers()
	{
		// TODO Auto-generated method stub
		if(this.entityModifiers == null)
		{
			return;
		}
		this.entityModifiers.clear();
	}


	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = new StringBuilder();
		this.toString(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void toString(final StringBuilder stringBuilder)
	{
		stringBuilder.append(this.getClass().getSimpleName());

		if((this.children != null) && (this.children.size() > 0))
		{
			stringBuilder.append(" [");
			final SmartList<EntityInterface> entities = this.children;
			for(int i = 0; i < entities.size(); i++)
			{
				entities.get(i).toString(stringBuilder);
				if(i < (entities.size() - 1))
				{
					stringBuilder.append(", ");
				}
			}
			stringBuilder.append("]");
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();

		if(!this.disposed)
		{
			this.dispose();
		}
	}
	
	@Override
	public void onDraw(final GLState GLState, final Camera camera)
	{
		// TODO Auto-generated method stub
		if(this.visible && !(this.cullingEnabled && this.isCulled(camera)))
		{
			this.onManagedDraw(GLState, camera);
		}
	}

	protected void onUpdateColor()
	{

	}
	
	@Override
	public void onUpdate(float secondsElapsed)
	{
		// TODO Auto-generated method stub
		if(!this.ignoreUpdate)
		{
			this.onManagedUpdate(secondsElapsed);
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		this.visible = true;
		this.cullingEnabled = false;
		this.ignoreUpdate = false;
		this.childrenVisible = true;
		this.childrenIgnoreUpdate = false;

		this.rotation = 0;
		this.scaleX = 1;
		this.scaleY = 1;
		this.skewX = 0;
		this.skewY = 0;

		this.color.reset();

		if(this.entityModifiers != null) 
		{
			this.entityModifiers.reset();
		}

		if(this.children != null) 
		{
			final SmartList<EntityInterface> entities = this.children;
			for(int i = entities.size() - 1; i >= 0; i--) 
			{
				entities.get(i).reset();
			}
		}
	}

	
	@Override
	public void onAttached()
	{
		// TODO Auto-generated method stub
        if (this. children != null)
        {
            for (int i = 0; i < this.children.size(); ++i)
            {
                final EntityInterface child = this.children.get(i);
                child.onAttached();
            }
        }
        this.running = true;
    }

	@Override
	public void onDetached()
	{
		// TODO Auto-generated method stub
        // actions
        //this.stopAllActions();
        //this.unscheduleAllSelectors();
        this.running=false;
        if (this. children != null)
        {
            for (int i = 0; i < this.children.size(); ++i)
            {
                final EntityInterface child = this.children.get(i);
                child.onDetached();
            }
        }
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	

	/**
	* @param glState the currently active {@link GLState} i.e. to apply transformations to.
	* @param scene camera the currently active {@link Camera} i.e. to be used for culling.
	*/
	protected void preDraw(final GLState glState, final Camera camera)
	{

	}

	/**
	* @param glState the currently active {@link GLState} i.e. to apply transformations to.
	* @param scene camera the currently active {@link Camera} i.e. to be used for culling.
	*/
	protected void draw(final GLState glState, final Camera camera)
	{

	}

	/**
	* @param glState the currently active {@link GLState} i.e. to apply transformations to.
	* @param camera the currently active {@link Camera} i.e. to be used for culling.
	*/
	protected void postDraw(final GLState glState, final Camera camera)
	{

	}

	private void allocateEntityModifiers()
	{
			this.entityModifiers = new EntityModifierList(Entity.ENTITYMODIFIERS_CAPACITY_DEFAULT,this);
	}

	private void allocateChildren()
	{
			this.children = new SmartList<EntityInterface>(Entity.CHILDREN_CAPACITY_DEFAULT);
	}

	private void allocateUpdateHandlers()
	{
			this.updateHandlers = new UpdateHandlerList(Entity.UPDATEHANDLERS_CAPACITY_DEFAULT);
	}

	protected void onApplyTransformations(final GLState glState)
	{
        if (this.relativeAnchorPoint)
        {
            glState.translateModelViewGLMatrixf(-this.anchorPosition.x*(this.contentSize.width), 
            		-this.anchorPosition.y*(this.contentSize.height),
                    this.vertexZ);
        }
			/* Translation. */
			this.applyTranslation(glState);

			/* Rotation. */
			this.applyRotation(glState);

			/* Skew. */
			this.applySkew(glState);

			/* Scale. */
			this.applyScale(glState);

	}

	/**
	 * 平移
	 * @param glState
	 */
	protected void applyTranslation(final GLState glState)
	{

			glState.translateModelViewGLMatrixf(this.position.x, this.position.y, this.vertexZ);
	}

	/**
	 * 旋转
	 * @param pGLState
	 */
	protected void applyRotation(final GLState pGLState)
	{
			final float rotation = this.rotation;

			if(rotation != 0)
            {
				final float rotationCenterX = this.rotationCenter.x;
				final float rotationCenterY = this.rotationCenter.y;

				pGLState.translateModelViewGLMatrixf(rotationCenterX, rotationCenterY, this.vertexZ);
				pGLState.rotateModelViewGLMatrixf(rotation, 0, 0, 1);
				pGLState.translateModelViewGLMatrixf(-rotationCenterX, -rotationCenterY, this.vertexZ);

				/* TODO There is a special, but very likely case when mRotationCenter and mScaleCenter are the same.
				 * In that case the last glTranslatef of the rotation and the first glTranslatef of the scale is superfluous.
				 * The problem is that applyRotation and applyScale would need to be "merged" in order to efficiently check for that condition.  */
			}
	}

	protected void applySkew(final GLState pGLState)
	{
			final float skewX = this.skewX;
			final float skewY = this.skewY;

			if((skewX != 0) || (skewY != 0))
			{
				final float skewCenterX = this.skewCenter.x;
				final float skewCenterY = this.skewCenter.y;

				pGLState.translateModelViewGLMatrixf(skewCenterX, skewCenterY, this.vertexZ);
				pGLState.skewModelViewGLMatrixf(skewX, skewY);
				pGLState.translateModelViewGLMatrixf(-skewCenterX, -skewCenterY, this.vertexZ);
			}
	}

	protected void applyScale(final GLState pGLState)
	{
			final float scaleX = this.scaleX;
			final float scaleY = this.scaleY;

			if((scaleX != 1) || (scaleY != 1))
            {
				final float scaleCenterX = this.scaleCenter.x;
				final float scaleCenterY = this.scaleCenter.y;

				pGLState.translateModelViewGLMatrixf(scaleCenterX, scaleCenterY, this.vertexZ);
				pGLState.scaleModelViewGLMatrixf(scaleX, scaleY, 1);
				pGLState.translateModelViewGLMatrixf(-scaleCenterX, -scaleCenterY, this.vertexZ);
			}
	}

    protected void applyTransform(final  GLState glState)
    {
    	
    }

	protected void onManagedDraw(final GLState glState, final Camera camera)
	{
			glState.pushModelViewGLMatrix();
			{
				this.onApplyTransformations(glState);

				final SmartList<EntityInterface> children = this.children;
				if((children == null) || !this.childrenVisible) 
				{
					/* Draw only self. */
					this.preDraw(glState, camera);
					this.draw(glState, camera);
					this.postDraw(glState, camera);
				} 
				else
				{
					if(this.childrenSortPending)
					{
						ZIndexSorter.getInstance().sort(this.children);
						this.childrenSortPending = false;
					}

					final int childCount = children.size();
					int i = 0;
                    EntityInterface child = null;

					 /* Draw children behind this Entity. */
					for(; i < childCount; i++)
					{
						child = children.get(i);
						if(child.getZIndex() < 0)
						{
								child.onDraw(glState, camera);
						}
                        else
                        {
							break;
						}
					}


					/* Draw self. */
					this.preDraw(glState, camera);
					this.draw(glState, camera);
					this.postDraw(glState, camera);

					{ /* Draw children in front of this Entity. */
						for(; i < childCount; i++)
						{
                            child = children.get(i);
                            //Debug.d("child=>"+child.toString());
							child.onDraw(glState, camera);
						}
					}
				}
			}
			glState.popModelViewGLMatrix();
	}

	protected void onManagedUpdate(final float pSecondsElapsed)
	{
			if(this.entityModifiers != null)
			{
				this.entityModifiers.onUpdate(pSecondsElapsed);
			}
			if(this.updateHandlers != null)
			{
				this.updateHandlers.onUpdate(pSecondsElapsed);
			}

			if((this.children != null) && !this.childrenIgnoreUpdate)
            {
				final SmartList<EntityInterface> entities = this.children;
				final int entityCount = entities.size();
				for(int i = 0; i < entityCount; i++)
                {
					entities.get(i).onUpdate(pSecondsElapsed);
				}
			}
	}

	private void assertEntityHasNoParent(final EntityInterface entity) throws IllegalStateException
	{
			if(entity.hasParent())
			{
				final String entityClassName = entity.getClass().getSimpleName();
				final String currentParentClassName = entity.getParent().getClass().getSimpleName();
				final String newParentClassName = this.getClass().getSimpleName();
				throw new IllegalStateException("Entity '" + entityClassName +"' already has a parent: '" + currentParentClassName + "'. New parent: '" + newParentClassName + "'!");
			}
	}

    
    /**
     * 得到包围盒，相对于元素自身坐标系而言
     * @return
     */
    public Rect getBoundingBox()
    {
        return Rect.make(0.0F, 0.0F, this.contentSize.width,
                  this.contentSize.height);  
    }  
    /**
     * 得到节点的边框矩形，相对于父节点坐标系而言  
     * @return
     */
    public Rect getBoundingBoxRelativeToParent()
    {
        Rect rect = Rect.make(0.0F, 0.0F, this.contentSize.width,
                  this.contentSize.height);  
        return convertRectUsingMatrix(rect, this.getLocalToParentMatrix());  
    }  
    //得到节点的边框矩形，相对于全局坐标系而言，得到的矩形对象中，origin是左下角坐标  
    public Rect getBoundingBoxReletiveToWorld()
     {
        Rect rect = Rect.make(0.0F, 0.0F, this.contentSize.width,
                  this.contentSize.height);  
        return convertRectUsingMatrix(rect, this.getLocalToSceneMatrix());  
    }
    
  //将矩阵转换成矩形
    private Rect convertRectUsingMatrix(Rect rect,TransformMatrix matrix)
    {
    	Rect r = Rect.make(0.0F, 0.0F, 0.0F, 0.0F);
        Point[] p = new Point[4];
        for (int i = 0; i < 4; ++i) {
           p[i] = Point.make(rect.origin.x, rect.origin.y);
        }
        p[1].x += rect.size.width;
        p[2].y += rect.size.height;
        p[3].x += rect.size.width;
        p[3].y += rect.size.height;
        for (int i = 0; i < 4; ++i) {
           p[i] = p[i].applyTransform(matrix);
        }
        Point min = Point.make(p[0].x, p[0].y);
        Point max = Point.make(p[0].x, p[0].y);
        for (int i = 1; i < 4; ++i) {
           min.x = Math.min(min.x, p[i].x);
           min.y = Math.min(min.y, p[i].y);
           max.x = Math.max(max.x, p[i].x);
           max.y = Math.max(max.y, p[i].y);
        }
        r.origin.x = min.x;
        r.origin.y = min.y;
        r.size.width = (max.x - min.x);
        r.size.height = (max.y - min.y);
        return r;
    }
/*	

    实现了这样的接口之后，我们就可以在节点被添加（addnode）时调用onEnter函数，
    在将节点被移除时调用onExit函数，因此前面所实现的节点操作函数中就可以加上这样两个函数，使其更加完善，添加方法很简单，就是判断当前节点是否运行（mRunning），
    如果运行则调用该节点的上面这两个函数，所以大家可以自己下去实现即可。
    
    //把一个全局坐标转换成节点内坐标
    public Point convertToNodeSpace(float x, float y) {
        YFSPoint worldPoint = YFSPoint.make(x, y);
        return worldPoint.applyTransform(worldToNodeTransform());
    }
    //把一个全局坐标转换成节点内坐标，以节点的锚点为原点
    public Point convertToNodeSpaceAR(float x, float y) {
        YFSPoint nodePoint = convertToNodeSpace(x, y);
        return YFSPoint.sub(nodePoint, this.mAnchorPosition);
    }
    //把Android触摸事件中坐标转换成节点内坐标。
    public Point convertTouchToNodeSpace(MotionEvent event) {
        YFSPoint point = Director.getInstance().convertToGL(event.getX(),
                  event.getY());
        return convertToNodeSpace(point.x, point.y);
    }
    //把Android触摸事件中坐标转换成节点内坐标。
    public Point convertTouchToNodeSpaceAR(MotionEvent event) {
        YFSPoint point = Director.getInstance().convertToGL(event.getX(),
                  event.getY());
        return convertToNodeSpaceAR(point.x, point.y);
    }
    //把节点内坐标转换为OpenGL全局坐标
    public Point convertToWorldSpace(float x, float y) {
        YFSPoint nodePoint = YFSPoint.make(x, y);
        return nodePoint.applyTransform(nodeToWorldTransform());
    }
    //把节点内坐标转换为OpenGL全局坐标。
    public Point convertToWorldSpaceAR(float x, float y) {
        YFSPoint nodePoint = YFSPoint.make(x, y);
        nodePoint = YFSPoint.add(nodePoint, this.mAnchorPosition);
        return convertToWorldSpace(nodePoint.x, nodePoint.y);
    }


    //由于我们的节点可以嵌套，所以节点的位置也就会受到其父节点的影响，因此我们提供了得到相对位置的函数，
        public Point getAbsolutePosition() {
        YFSPoint ret = YFSPoint.make(this.mPosition.x, this.mPosition.y);
        Node cn = this;
        while (cn.mParent != null) {
           cn = cn.mParent;
           ret.x += cn.mPosition.x;
           ret.y += cn.mPosition.y;
        }
        return ret;
    }

    void CCNode::onEnter()
{
    arrayMakeObjectsPerformSelector(m_pChildren, onEnter, CCNode*);

    this->resumeSchedulerAndActions();

    m_bRunning = true;

    if (m_eScriptType != kScriptTypeNone)
    {
        CCScriptEngineManager::sharedManager()->getScriptEngine()->executeNodeEvent(this, kCCNodeOnEnter);
    }
}

void CCNode::onEnterTransitionDidFinish()
{
    arrayMakeObjectsPerformSelector(m_pChildren, onEnterTransitionDidFinish, CCNode*);

    if (m_eScriptType == kScriptTypeJavascript)
    {
        CCScriptEngineManager::sharedManager()->getScriptEngine()->executeNodeEvent(this, kCCNodeOnEnterTransitionDidFinish);
    }
}

void CCNode::onExitTransitionDidStart()
{
    arrayMakeObjectsPerformSelector(m_pChildren, onExitTransitionDidStart, CCNode*);

    if (m_eScriptType == kScriptTypeJavascript)
    {
        CCScriptEngineManager::sharedManager()->getScriptEngine()->executeNodeEvent(this, kCCNodeOnExitTransitionDidStart);
    }
}

void CCNode::onExit()
{
    this->pauseSchedulerAndActions();

    m_bRunning = false;

    if ( m_eScriptType != kScriptTypeNone)
    {
        CCScriptEngineManager::sharedManager()->getScriptEngine()->executeNodeEvent(this, kCCNodeOnExit);
    }

    arrayMakeObjectsPerformSelector(m_pChildren, onExit, CCNode*);
}
    */
    private void timerAlloc()
    {
        this.scheduledSelectors = new HashMap<String, Scheduler.Timer>(4);
    }

    public void schedule(String selector, float interval)
    {
        assert (selector != null) : "Argument selector must be non-null";
        assert (interval >= 0.0F) : "Argument interval must be positive";
        if (this.scheduledSelectors == null)
        {
            timerAlloc();
        }

        String key = super.getClass().getName() + "." + selector + "(float)";
        if (this.scheduledSelectors.containsKey(key))
        {
            return;
        }

        Scheduler.Timer timer = new Scheduler.Timer(this, selector, interval);
        if (this.running)
        {
            Scheduler.getInstance().schedule(timer);
        }
        this.scheduledSelectors.put(key, timer);
    }

    public void unschedule(String selector)
    {
        if (selector == null)
        {
            return;
        }
        if (this.scheduledSelectors == null)
        {
            return;
        }
        String key = super.getClass().getName() + "." + selector + "(float)";
        Scheduler.Timer timer;
        if ((timer = (Scheduler.Timer) this.scheduledSelectors.get(key)) == null)
        {
            return;
        }
        this.scheduledSelectors.remove(key);
        if (this.running)
            Scheduler.getInstance().unschedule(timer);
    }

    //激活计时器
    private void activateTimers()
    {
        if (this.scheduledSelectors != null)
        {
            for (String key : this.scheduledSelectors.keySet())
                Scheduler.getInstance().schedule(
                        (Scheduler.Timer) this.scheduledSelectors.get(key));
        }
        ModifierManager.getInstance().resumeAllActions(this);
    }

    //销毁计时器
    private void deactivateTimers()
    {
        if (this.scheduledSelectors != null)
        {
            for (String key : this.scheduledSelectors.keySet())
                Scheduler.getInstance().unschedule(
                        (Scheduler.Timer) this.scheduledSelectors.get(key));
        }
        ModifierManager.getInstance().pauseAllActions(this);
    }
    
  //动画帧的基类
    public static class Frame
    {
        public float duration;
        public Frame(float duration)
        {
            this.duration = duration;
        }
    }

    //实现此接口表示节点可以包含一个贴图
    public static abstract interface TextureOwnerInterface
    {
        public abstract TextureInterface getTexture();
        public abstract void setTexture(TextureInterface texture);
    }

    //实现此接口表示节点可以设置渲染模式
    public static abstract interface BlendableInterface
    {
        //设置混合
        public abstract void setBlendFunction(BlendFunction blendFunc);
        //得到混合
        public abstract BlendFunction getBlendFunction();
    }

    //实现该接口表示节点可以支持设置贴图且设置渲染方式
    public static abstract interface IBlendableTextureOwner extends
            BlendableInterface, TextureOwnerInterface
    {

    }
}
