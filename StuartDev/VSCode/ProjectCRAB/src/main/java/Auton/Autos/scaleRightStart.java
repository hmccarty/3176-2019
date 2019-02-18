package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class scaleRightStart extends Auto{
	public static scaleRightStart main = new scaleRightStart();
	private AutoManager manager = new AutoManager();
	private scaleRightStart()
	{
		if(super.getGameData().substring(1,2).equals("R"))
		{
			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(rightToRightScale.main.get()),
					new GoToElevatorHeight(Constants.SCALEHIGHHEIGHT)));
			manager.qeueCommand(new shootPositionIntake());
			manager.qeueCommand(new Scoring(.2));
			manager.qeueCommand(new GoToElevatorHeight(0.0));
//			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(LeftScaleToCube.main.get()),
//					new Intaking()));
//			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(BackCubeToLeftScale.main.get()), new GoToElevatorHeight(Constants.SCALEHIGHHEIGHT)));
//			manager.qeueCommand(new shootPositionIntake());
//			manager.qeueCommand(new Scoring(.2));
//			manager.qeueCommand(new GoToElevatorHeight(0.0));
		}
		else
		{
			manager.qeueCommand(new DriveTrajectory(drivestraight.main.get()));
		}
		
		super.registerManager(manager);
	}


}
