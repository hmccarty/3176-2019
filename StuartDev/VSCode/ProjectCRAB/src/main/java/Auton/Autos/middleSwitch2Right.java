package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class middleSwitch2Right extends Auto{
	public static middleSwitch2Right main = new middleSwitch2Right();
	private AutoManager manager = new AutoManager();
	private middleSwitch2Right()
	{
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(middleToRightSwitch.main.get()),new GoToElevatorHeight(Constants.SWITCHHEIGHT),new DeployIntake()));
		manager.qeueCommand(new SlowScoring(.1));
		manager.qeueCommand(new GoToElevatorHeight(0.0));
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(RightSwitchToCenterCube.main.get()),new Intaking()));
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(CenterCubeToRightSwitch.main.get()),
												new GoToElevatorHeight(Constants.SWITCHHEIGHT),
												new DeployIntake()));
		manager.qeueCommand(new SlowScoring(.1));
		manager.qeueCommand(new GoToElevatorHeight(0.0));
		super.registerManager(manager);
	}


}
