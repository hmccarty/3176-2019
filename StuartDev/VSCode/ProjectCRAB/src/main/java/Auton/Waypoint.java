package Auton;


public class Waypoint {
	private double X;
	private double Y;
	private double Angle;
	private double speed;
	/**
	 * A structure to hold and x , y, and angle
	 * @param X double in ft
	 * @param Y double in ft
	 * @param Angle (-Pi to PI) with 0 straight ahead
	 */
	public Waypoint(double X, double Y, double Angle)
	{
		this.X = X;
		this.Y = Y;
		this.Angle = Angle;
		this.speed = 0.0;
	}
	/**
	 * A structure to hold and x , y, and angle
	 * @param X double in ft
	 * @param Y double in ft
	 * @param Angle (-Pi to PI) with 0 straight ahead
	 * @param speed the ending speed for the trajectory in ft/s
	 */
	public Waypoint(double X, double Y, double Angle, double speed)
	{
		this.X = X;
		this.Y = Y;
		this.Angle = Angle;
		this.speed = speed;
	}
	/**
	 * @return X in ft
	 */
	public double getX() {
		return X;
	}
	/**
	 * @param x sets the local X
	 */
	public void setX(double x) {
		X = x;
	}
	/**
	 * @return Y in ft
	 */
	public double getY() {
		return Y;
	}
	/**
	 * @param y sets local Y
	 */
	public void setY(double y) {
		Y = y;
	}
	/**
	 * @return Angle (-Pi to PI) with 0 straight ahead
	 */
	public double getAngle() {
		return Angle;
	}
	/**
	 * @param angle sets local Angle
	 */
	public void setAngle(double angle) {
		Angle = angle;
	}
	/**
	 * @return Speed ft/s
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @param speed sets local Speed ft/s
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
}
