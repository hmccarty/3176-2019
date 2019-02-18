package Auton.Paths;

import Auton.*;
/**
 * a container for pre-generated trajectories.
 */
public class Path {
	private Trajectory t;
	public Path()
	{
		
	}
	/**
	 * registers the trajectory and calls its {@link Trajectory#calculateTrajectory() calculateTrajectory()} method 
	 * @param t the trajectory to be registered should already have waypoints
	 */
	public void regesterTrajectory(Trajectory t)
	{
		this.t = t;
		this.t.calculateTrajectory();
	}
	/**
	 * @return trajectory already generated and ready to call the {@link Trajectory#getSpeed(double) getSpeed()} ect.
	 */
	public Trajectory get()
	{
		return t;
	}
}
