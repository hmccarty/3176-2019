package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class sideSwitchLeft extends Auto{
	public static sideSwitchLeft main = new sideSwitchLeft();
	private AutoManager manager = new AutoManager();
	private sideSwitchLeft()
	{
		if(getGameData().substring(0,1).equals("L"))
		{
			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(leftToLeftSwitch.main.get()),
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
