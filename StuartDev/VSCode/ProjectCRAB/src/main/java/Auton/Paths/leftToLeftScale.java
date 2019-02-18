package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class leftToLeftScale extends Path {
	public static leftToLeftScale main = new leftToLeftScale();
	private Trajectory t = new Trajectory(8.0,4.0);
	private leftToLeftScale() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(-3.0,-4.0,Math.PI/2.0,3.0));
		t.addWaypoint(new Waypoint(-3.0,-23.0,Math.PI/2.0));
		super.regesterTrajectory(t);
	}
}
