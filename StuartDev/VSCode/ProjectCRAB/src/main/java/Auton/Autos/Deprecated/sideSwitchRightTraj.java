package Auton.Autos.Deprecated;

import Auton.PathFollower;
import Auton.Trajectory;
import Auton.Waypoint;
import Auton.Autos.Auto;
import Subsystem.*;
import edu.wpi.first.wpilibj.Timer;

public class sideSwitchRightTraj extends Auto {
	public static sideSwitchRightTraj main = new sideSwitchRightTraj();
	private static Trajectory t;
	private static PathFollower p;
	private static double startTime;
	private static double scoreTime = 2.0;
	public sideSwitchRightTraj() {
		super.registerLoop(new Loop()
		{

			@Override
			public void onStart() {
				startTime = Timer.getFPGATimestamp();
				t = new Trajectory();
				t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				if(getGameData().substring(0, 1).equals("R"))
				{
					t.addWaypoint(new Waypoint(0.0, -8.0, -Math.PI/2.0));
				}
				else
				{
					t.addWaypoint(new Waypoint(0.0, -8.0, 0.0));
				}
				t.calculateTrajectory();
				p = new PathFollower(t);
				p.init();
				p.run();
			}

			@Override
			public void onloop() {
				double time = Timer.getFPGATimestamp()-startTime;
				if(time<t.getTimeToComplete())
				{
					p.run();
				}
				else if(time<t.getTimeToComplete()+scoreTime && getGameData().substring(0, 1).equals("R"))
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Scoring);
				}
				else
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Neutral);
				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				
			}
	
		});
	}
}
