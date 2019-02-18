package Auton.Autos;


	import Auton.AutoManager;
import Auton.Commands.DriveTrajectory;
import Auton.Paths.*;

public class driveStraight extends Auto {
		public static driveStraight main = new driveStraight();
		private AutoManager manager = new AutoManager();
		public driveStraight() {
			manager.qeueCommand(new DriveTrajectory(drivestraight.main.get()));
		}
		

}

