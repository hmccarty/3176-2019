package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class middleSwitchLeft extends Auto{
	public static middleSwitchLeft main = new middleSwitchLeft();
	private AutoManager manager = new AutoManager();
	private middleSwitchLeft()
	{
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(middleToLeftSwitch.main.get()),new GoToElevatorHeight(Constants.SWITCHHEIGHT),new DeployIntake()));
		manager.qeueCommand(new SlowScoring(.5));
		manager.qeueCommand(new GoToElevatorHeight(0.0));
		super.registerManager(manager);
	}


}
