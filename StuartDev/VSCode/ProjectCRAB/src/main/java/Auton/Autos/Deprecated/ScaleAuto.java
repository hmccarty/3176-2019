package Auton.Autos.Deprecated;

import Auton.PathFollower;
import Auton.Trajectory;
import Auton.Waypoint;
import Auton.Autos.Auto;
import Subsystem.Loop;
import edu.wpi.first.wpilibj.Timer;
import Subsystem.*;
import org.usfirst.frc.team6713.robot.*;
public class ScaleAuto extends Auto {
	public static ScaleAuto main = new ScaleAuto();
	private static Trajectory t;
	private static PathFollower p;
	private static double startTime;
	public ScaleAuto() {
		super.registerLoop(new Loop()
		{

			@Override
			public void onStart() {
				t = new Trajectory();
				t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				if(Auto.getGameData().substring(1,2).equals("L"))
				{
					t.addWaypoint(new Waypoint(0.0, -24.0, 1.0));
				}
				else
				{
					t.addWaypoint(new Waypoint(0.0, -24.0, -1.0));
				}
				
				t.calculateTrajectory();
				p = new PathFollower(t);
				p.init();
				p.run();
				startTime = Timer.getFPGATimestamp();
			}

			@Override
			public void onloop() {
				double time = Timer.getFPGATimestamp()-startTime;
				if(time<t.getTimeToComplete())
				{
					p.run();
					Elevator.getInstance().setWantedState(Elevator.systemStates.POSITION_FOLLOW);
					Elevator.getInstance().setWantedFloor(Constants.SCALEHIGHHEIGHT);
					Intake.getInstance().setPosition(-1000);
				}
				else if(time<t.getTimeToComplete()+2.0)
				{
					Intake.getInstance().setPosition(-1000);
					Intake.getInstance().setWantedState(Intake.systemStates.Scoring);
				}
				else
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Neutral);
					Elevator.getInstance().setWantedFloor(0.0);
				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				
			}
	
		});
	}
}
