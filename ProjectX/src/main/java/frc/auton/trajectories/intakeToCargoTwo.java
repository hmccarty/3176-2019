package frc.auton.trajectories; 

import frc.robot.*; 
import frc.util.*;
import frc.auton.*;
import frc.subsystem.drivetrain; 

public class intakeToCargoTwo extends path{
	public static intakeToCargoTwo main = new intakeToCargoTwo();
	private trajectory t = new trajectory();
	private intakeToCargoTwo() {
		t.addWaypoint(new waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new waypoint(6, 17.5,Math.PI/2));
		super.regesterTrajectory(t);
	}
}