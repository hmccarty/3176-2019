package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class rightToLeftScale extends Path {
	public static rightToLeftScale main = new rightToLeftScale();
	private Trajectory t = new Trajectory();
	private rightToLeftScale() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(-15.0,-18.0,0.0));
		t.addWaypoint(new Waypoint(-15.0,-18.0,0.0));
		t.addWaypoint(new Waypoint(-15.0,-20.0,Math.PI/4.0));
		super.regesterTrajectory(t);
	}
}
