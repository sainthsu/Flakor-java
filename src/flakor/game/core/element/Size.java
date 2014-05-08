package flakor.game.core.element;

/**
 *
 */
public class Size
{
	public float width;  
	public float height;
	
	public Size(float width,float height)
	{
		this.width = width;
		this.height = height;
	}

    public static  Size make(float width,float height)
    {
        return new Size(width,height);
    }

    public static Size makeZero()
	{  
	     return make(0.0F, 0.0F);
	}
    
    public Size swap()
    {
        return new Size(this.height,this.width);
    }

    public boolean equals(final Size other)
    {
        return (this.width == other.width) && (this.height == other.height);
    }

 	//...
	//由于篇幅关系,中间省略了部分重载函数  
	public String toString()
	{  
	    return "Size:<width:" + this.width + ",height: " + this.height + ">";
	}  
}
