package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class CenterCubeToRightSwitch extends Path {
	public static CenterCubeToRightSwitch main = new CenterCubeToRightSwitch();
	private Trajectory t = new Trajectory(7.0,4.5);
	private CenterCubeToRightSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(4.0,-1.0,0.0,3.0));
		t.addWaypoint(new Waypoint(4.0,-5.5, 0.0));
		super.regesterTrajectory(t);
	}
}
