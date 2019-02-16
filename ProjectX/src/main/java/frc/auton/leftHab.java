package frc.auton;


import frc.auton.autonmanager;
import frc.auton.drivetrajectory;
import frc.auton.path;
import frc.auton.*;
import frc.subsystem.*;

public class leftHab extends auto {
		public static leftHab main = new leftHab();
		private autonmanager manager = new autonmanager();
		public leftHab() {
			registerManager(manager);
			manager.qeueCommand(new drivetrajectory(leftHabT.main.get()));
			manager.qeueCommand(new visiontrack());
		}
		

}

