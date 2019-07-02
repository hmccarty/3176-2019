package frc.auton.routines;

import frc.auton.autonmanager;
import frc.auton.drivetrajectory;
import frc.auton.trajectories.*;
import frc.auton.path;
import frc.auton.*;
import frc.subsystem.*;

public class leftHab extends auto {
		public static leftHab main = new leftHab();
		private autonmanager manager = new autonmanager();
		public leftHab() {
			registerManager(manager);
			manager.qeueCommand(new drivetrajectory(habToRightCargoHatch.main.get()));
			manager.qeueCommand(new visiontrack());
			manager.qeueCommand(new drivetrajectory(rightCargoOneToIntake.main.get()));
			manager.qeueCommand(new visiontrack());
			manager.qeueCommand(new drivetrajectory(intakeToCargoTwo.main.get()));
			manager.qeueCommand(new visiontrack());
		}
}

