package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class sideSwitchRight extends Auto{
	public static sideSwitchRight main = new sideSwitchRight();
	private AutoManager manager = new AutoManager();
	private sideSwitchRight()
	{
		if(getGameData().substring(0,1).equals("R"))
		{
			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(rightToRightSwitch.main.get()),
					new GoToElevatorHeight(Constants.SWITCHHEIGHT+10000),
					new DeployIntake()));
			manager.qeueCommand(new SlowScoring(1.0));
			manager.qeueCommand(new GoToElevatorHeight(0.0));
		}
		else
		{
			manager.qeueCommand(new DriveTrajectory(drivestraight.main.get()));
		}
		super.registerManager(manager);
	}


}
