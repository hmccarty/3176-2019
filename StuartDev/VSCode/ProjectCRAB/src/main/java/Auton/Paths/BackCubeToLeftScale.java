package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class BackCubeToLeftScale extends Path {
	public static BackCubeToLeftScale main = new BackCubeToLeftScale();
	private Trajectory t = new Trajectory();
	private BackCubeToLeftScale() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(-8.0, 0.0, 0.0));
		super.regesterTrajectory(t);
	}
}
