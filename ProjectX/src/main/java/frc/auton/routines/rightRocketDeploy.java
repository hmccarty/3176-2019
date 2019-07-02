package frc.auton.routines;

import frc.auton.autonmanager;
import frc.auton.drivetrajectory;
import frc.auton.trajectories.*;
import frc.auton.path;
import frc.auton.*;
import frc.subsystem.*;

public class rightRocketDeploy extends auto {
		public static rightRocketDeploy main = new rightRocketDeploy();
		private autonmanager manager = new autonmanager();
		public rightRocketDeploy() {
			registerManager(manager);
			manager.qeueCommand(new drivetrajectory(rightToRightRocket.main.get()));
		}
		

}