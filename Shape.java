import java.awt.Color;
public class Shape
{
	public Color color;
	public Polygon polygon;
	public Shape(Color color, Polygon polygon)
	{
		this.color = color;
		this.polygon = polygon;
	}
	public Shape(Shape shape)
	{
		this.color = shape.color;
		this.polygon = shape.polygon;
	}
	public void print()
	{
		System.out.println("("+polygon.X_POINTS[0]+","+polygon.Y_POINTS[0]+")");
	}
	public String toString()
	{
		return polygon.X_POINTS[0]+","+polygon.Y_POINTS[0];	
	}
}