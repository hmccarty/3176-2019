package frc.auton.trajectories; 

import frc.robot.*; 
import frc.util.*;
import frc.auton.*;
import frc.subsystem.drivetrain; 

public class rightToRightRocket extends path{
	public static rightToRightRocket main = new rightToRightRocket();
	private trajectory t = new trajectory();
	private rightToRightRocket() {
		t.addWaypoint(new waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new waypoint(-6.0, 0.0, 0));
		t.addWaypoint(new waypoint(0.0, 6.2, 0.0));
		super.regesterTrajectory(t);
	}
}