package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class middleSwitch2Left extends Auto{
	public static middleSwitch2Left main = new middleSwitch2Left();
	private AutoManager manager = new AutoManager();
	private middleSwitch2Left()
	{
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(middleToLeftSwitch.main.get()),new GoToElevatorHeight(Constants.SWITCHHEIGHT),new DeployIntake()));
		manager.qeueCommand(new Scoring(.07));
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(LeftSwitchToCenterCube.main.get()),new Intaking()));
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(CenterCubeToLeftSwitch.main.get()),
												new GoToElevatorHeight(Constants.SWITCHHEIGHT+10000),
												new DeployIntake()));
		manager.qeueCommand(new Scoring(.07));
		manager.qeueCommand(new GoToElevatorHeight(0.0));
		super.registerManager(manager);
	}


}
