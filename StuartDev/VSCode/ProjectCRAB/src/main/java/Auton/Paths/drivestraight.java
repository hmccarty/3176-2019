package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class drivestraight extends Path {
	public static drivestraight main = new drivestraight();
	private Trajectory t = new Trajectory();
	private drivestraight() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(0.0,-10.0,0.0));
		super.regesterTrajectory(t);
	}
}
