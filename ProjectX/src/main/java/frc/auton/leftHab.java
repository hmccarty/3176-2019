package frc.auton;


import frc.auton.autonmanager;
import frc.auton.drivetrajectory;
import frc.auton.path;
import frc.auton.*;

public class leftHab extends auto {
		public static leftHab main = new leftHab();
		private autonmanager manager = new autonmanager();
		public leftHab() {
			manager.qeueCommand(new drivetrajectory(leftHabT.main.get()));
		}
		

}

