package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class rightToRightSwitch extends Path {
	public static rightToRightSwitch main = new rightToRightSwitch();
	private Trajectory t = new Trajectory();
	private rightToRightSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(0.0,-10.0,-Math.PI/2.0));
		t.addWaypoint(new Waypoint(-2.0,-10.0,-Math.PI/2.0));
		super.regesterTrajectory(t);
	}
}
