package frc.auton; 

import frc.robot.*; 
import frc.util.*;
import frc.subsystem.drivetrain; 

public class leftHabT extends path{
	public static leftHabT main = new leftHabT();
	private trajectory t = new trajectory();
	private leftHabT() {
		t.addWaypoint(new waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new waypoint(0.0,-10.0,0.0));
		super.regesterTrajectory(t);
	}
}