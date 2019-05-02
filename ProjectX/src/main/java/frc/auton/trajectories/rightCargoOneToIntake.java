package frc.auton.trajectories; 

import frc.robot.*; 
import frc.util.*;
import frc.auton.*;
import frc.subsystem.drivetrain; 

public class rightCargoOneToIntake extends path{
	public static rightCargoOneToIntake main = new rightCargoOneToIntake();
	private trajectory t = new trajectory();
	private rightCargoOneToIntake() {
		t.addWaypoint(new waypoint(0.0, 0.0, 0.0));
		t.addWaypoint(new waypoint(-4, 1.5,-Math.PI/2));
		t.addWaypoint(new waypoint(-16.75, 6.2, -Math.PI/2));
		super.regesterTrajectory(t);
	}
}