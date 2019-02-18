package Auton.Autos;

import Auton.AutoManager;
import Auton.Commands.*;
import Auton.Paths.*;


import org.usfirst.frc.team6713.robot.*;

public class scaleLeftStart extends Auto{
	public static scaleLeftStart main = new scaleLeftStart();
	private AutoManager manager = new AutoManager();
	private scaleLeftStart()
	{
		if(super.getGameData().substring(1,2).equals("L"))
		{
			manager.qeueCommand(new ParallelCommand(new DriveTrajectory(leftToLeftScale.main.get()),
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
