package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class middleToRightSwitch extends Path {
	public static middleToRightSwitch main = new middleToRightSwitch();
	private Trajectory t = new Trajectory(6.0,5.0);
	private middleToRightSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(3.65, -4.0,0.0 ,2.0));
		t.addWaypoint(new Waypoint(3.65, -9.0, 0.0));
		super.regesterTrajectory(t);
	}
}
