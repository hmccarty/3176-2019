package frc.auton.trajectories; 

import frc.robot.*; 
import frc.util.*;
import frc.auton.*;
import frc.subsystem.drivetrain; 

public class leftHabT extends path{
	public static leftHabT main = new leftHabT();
	private trajectory t = new trajectory();
	private leftHabT() {
		t.addWaypoint(new waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new waypoint(0.0, -3.0, 0.0));
		t.addWaypoint(new waypoint(3.0, -16.0, -Math.PI/2));
		super.regesterTrajectory(t);
	}
}