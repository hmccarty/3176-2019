package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class leftToRightScale extends Path {
	public static leftToRightScale main = new leftToRightScale();
	private Trajectory t = new Trajectory();
	private leftToRightScale() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(15.0,-18.0,0.0));
		t.addWaypoint(new Waypoint(15.0,-18.0,0.0));
		t.addWaypoint(new Waypoint(15.0,-20.0,-Math.PI/4.0));
		super.regesterTrajectory(t);
	}
}
