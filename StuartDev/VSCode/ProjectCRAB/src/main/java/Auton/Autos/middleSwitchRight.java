package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;

import org.usfirst.frc.team6713.robot.*;

public class middleSwitchRight extends Auto{
	public static middleSwitchRight main = new middleSwitchRight();
	private AutoManager manager = new AutoManager();
	private middleSwitchRight()
	{
		manager.qeueCommand(new ParallelCommand(new DriveTrajectory(middleToRightSwitch.main.get()),new GoToElevatorHeight(Constants.SWITCHHEIGHT),new DeployIntake()));
		manager.qeueCommand(new SlowScoring(1.0));
		manager.qeueCommand(new GoToElevatorHeight(0.0));
		super.registerManager(manager);
	}


}
