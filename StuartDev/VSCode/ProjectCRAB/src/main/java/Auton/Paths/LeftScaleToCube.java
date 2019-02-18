package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class LeftScaleToCube extends Path {
	public static LeftScaleToCube main = new LeftScaleToCube();
	private Trajectory t = new Trajectory();
	private LeftScaleToCube() {
		
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(5.0,0.0,Math.PI/4));
		t.addWaypoint(new Waypoint(8.0,-3.5,Math.PI/4));
		t.addWaypoint(new Waypoint(8.0,-3.5,Math.PI/4));
		t.addWaypoint(new Waypoint(8.0,0.0,0.0));
		super.regesterTrajectory(t);
	}
}
