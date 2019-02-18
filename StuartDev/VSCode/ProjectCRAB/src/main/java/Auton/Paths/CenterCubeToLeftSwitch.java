package Auton.Paths;

import Auton.Trajectory;
import Auton.Waypoint;

public class CenterCubeToLeftSwitch extends Path {
	public static CenterCubeToLeftSwitch main = new CenterCubeToLeftSwitch();
	private Trajectory t = new Trajectory(7.0,4.5);
	private CenterCubeToLeftSwitch() {
		t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new Waypoint(-4.3,-1.0,0.0,3.0));
		t.addWaypoint(new Waypoint(-4.3,-5.5, 0.0));
		super.regesterTrajectory(t);
	}
}
