package flakor.game.core.element;

@Deprecated
public class FKShape
{
	public class Triangle
	{
		
	}
	
	public class Rectangle
	{
		public static final int TYPE_2D=1;
		public static final int TYPE_3D=2;
		//left-bottom point position
		public float x;
		public float y;
		public float z;
		public float width;
		public float height;
		//quad type 2d/3d
		public int TYPE=TYPE_2D;
		
		public Rectangle()
		{
			
		}
		
	}
}
