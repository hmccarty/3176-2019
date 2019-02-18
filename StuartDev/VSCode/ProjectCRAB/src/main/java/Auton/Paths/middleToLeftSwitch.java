package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class middleToLeftSwitch extends Path {
	public static middleToLeftSwitch main = new middleToLeftSwitch();
	private Trajectory t = new Trajectory(6.0,5.0);
	private middleToLeftSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(-4.0,-5.0,0.0,2.0));
		t.addWaypoint(new Waypoint(-5.0, -9.0, 0.0));
		super.regesterTrajectory(t);
	}
}
