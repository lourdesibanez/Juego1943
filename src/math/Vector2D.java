package math;

public class Vector2D {
	private double x,y;
	//contener variables doble para poder efectuar operaciones, angulos
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2D()
	{
		x = 0;
		y = 0;
	}	
	
	//se mueva
	public Vector2D add(Vector2D v)
	{
		return new Vector2D(x + v.getX(), y + v.getY());
	}
	
	public Vector2D subtract(Vector2D v)
	{
		return new Vector2D(x - v.getX(), y - v.getY());
	}
	
	public Vector2D scale(double value)//multiplicar por un escalar, pide un doble
	{
		return new Vector2D(x*value, y*value);//sirve para modificar la magnitud para la direccion
	}
	
	public Vector2D limit(double value)//limita la velocidad maxima del avion
	{
		if(getMagnitude() > value)
		{
			return this.normalize().scale(value);
		}
		return this;
	}
	
	public Vector2D normalize()
	{
		double magnitude = getMagnitude();
		
		return new Vector2D(x / magnitude, y / magnitude);//la magnitud sea 1
	}
	
	public double getMagnitude()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	public Vector2D setDirection(double angle)
	{
		double magnitude = getMagnitude();
		
		return new Vector2D(Math.cos(angle)*magnitude, Math.sin(angle)*magnitude);//en cada operacion creo un nuevo vector porque 
	}
	
	public double getAngle() {
		return Math.asin(y/getMagnitude());
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
