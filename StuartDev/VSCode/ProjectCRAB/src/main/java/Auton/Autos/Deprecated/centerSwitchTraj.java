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

public class centerSwitchTraj extends Auto {
	public static centerSwitchTraj main = new centerSwitchTraj();
	private static Trajectory t1 = new Trajectory();
	private static PathFollower p1;
	private static Trajectory t2 = new Trajectory();
	private static PathFollower p2;
	private static Trajectory t3 = new Trajectory();
	private static PathFollower p3;
	private static double scoringTime = 2.0;
	private static double startTime;
	private static boolean firstLoop1 = true;
	private static boolean firstLoop2 = true;
	public centerSwitchTraj() {
		super.registerLoop(new Loop()
		{
			@Override
			public void onStart() {
				startTime = Timer.getFPGATimestamp();
				t1.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				t1.addWaypoint(new Waypoint(-4.5, -9.0, 0.0));
				t1.calculateTrajectory();
				p1 = new PathFollower(t1);
				t2.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				t2.addWaypoint(new Waypoint(2.0, 2.0, Math.PI/2.0));
				t2.calculateTrajectory();
				p2 = new PathFollower(t2);
				t3.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
				t3.addWaypoint(new Waypoint(-2.0, -2.0, -Math.PI/2.0));
				t3.calculateTrajectory();
				p3 = new PathFollower(t3);
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
				}
				else if(time<t1.getTimeToComplete()+scoringTime+t2.getTimeToComplete()+2)
				{
					if(firstLoop1)
					{
						p2.init();
						firstLoop1 = false;
					}
					Intake.getInstance().setWantedState(Intake.systemStates.Intaking);
					p2.run();
				}
				else if(time<t1.getTimeToComplete()+scoringTime+t2.getTimeToComplete()+t3.getTimeToComplete()+2)
				{
					if(firstLoop2)
					{
						p3.init();
						firstLoop2 = false;
					}
					
					p3.run();
				}
				else if(time<t1.getTimeToComplete()+scoringTime+t2.getTimeToComplete()+t3.getTimeToComplete()+scoringTime+2)
				{
					Intake.getInstance().setWantedState(Intake.systemStates.Scoring);
					Drivetrain.getInstance().swerve(0.0, 0.0, 0.0);
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
