package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class leftToLeftSwitch extends Path {
	public static leftToLeftSwitch main = new leftToLeftSwitch();
	private Trajectory t = new Trajectory();
	private leftToLeftSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(0.0,-12.0,Math.PI/2.0));
		t.addWaypoint(new Waypoint(2.0,-12.0,Math.PI/2.0));
		super.regesterTrajectory(t);
	}
}
