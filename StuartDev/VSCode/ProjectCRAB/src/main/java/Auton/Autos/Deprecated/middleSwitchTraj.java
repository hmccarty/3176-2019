package Auton.Autos.Deprecated;

import Auton.PathFollower;
import Auton.Trajectory;
import Auton.Waypoint;
import Auton.Autos.Auto;
import Subsystem.Drivetrain;
import Subsystem.Intake;
import Subsystem.Loop;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class middleSwitchTraj extends Auto {
	public static middleSwitchTraj main = new middleSwitchTraj();
	private static Trajectory t1 = new Trajectory();
	private static PathFollower p1;
	private static Trajectory t2 = new Trajectory();
	private static double scoringTime = 2.0;
	private static double startTime;
	public middleSwitchTraj() {
		super.registerLoop(new Loop()
		{
			@Override
			public void onStart() {
				startTime = Timer.getFPGATimestamp();
				t1.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				if(getGameData().substring(0,1).equals("L"))
				{
					t1.addWaypoint(new Waypoint(-4.85, -10.0, 0.0));
				}
				else
				{
					t1.addWaypoint(new Waypoint(4.45, -10.0, 0.0));
				}
				t2.addWaypoint(new Waypoint(0.0,0.0,0.0));
				t2.addWaypoint(new Waypoint(0.0,1.0,0.0));
				t2.calculateTrajectory();
				t1.calculateTrajectory();
				p1 = new PathFollower(t1);
				p1.init();
				p1.run();
			}

			@Override
			public void onloop() {
				SmartDashboard.putNumber("timer", Timer.getFPGATimestamp());
				SmartDashboard.putNumber("t2 trajectry", t2.getTimeToComplete());
				double time = Timer.getFPGATimestamp()-startTime;
				SmartDashboard.putNumber("autoTime", time);
				if(time<t1.getTimeToComplete())
				{
					p1.run();
					Intake.getInstance().setWantedState(Intake.systemStates.Neutral);
					Intake.getInstance().setPosition(-4000);
					
				}
				else if(time<t1.getTimeToComplete()+scoringTime)
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Scoring);
					Drivetrain.getInstance().swerve(0.0, 0.0, 0.0);
				}
				else
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Neutral);
					Drivetrain.getInstance().swerve(0.0, 0.0, 0.0);
				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				
			}
	
		});
	}
}
