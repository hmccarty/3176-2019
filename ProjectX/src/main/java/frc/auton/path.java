package frc.auton;

import frc.util.trajectory;;
/**
 * a container for pre-generated trajectories.
 */
public class path {
	private trajectory t;
	public path() {}
	/**
	 * registers the trajectory and calls its {@link Trajectory#calculateTrajectory() calculateTrajectory()} method 
	 * @param t the trajectory to be registered should already have waypoints
	 */
	public void regesterTrajectory(trajectory t) {
		this.t = t;
		this.t.calculateTrajectory();
	}
	/**
	 * @return trajectory already generated and ready to call the {@link Trajectory#getSpeed(double) getSpeed()} ect.
	 */
	public trajectory get() {
		return t;
	}
}
