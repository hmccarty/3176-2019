package Auton;



import org.usfirst.frc.team6713.robot.Constants;
import Subsystem.Drivetrain;
import Subsystem.Loop;
import Util.PIDLoop;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * follows a trajectory and commands the drivetrain
 */
public class PathFollower {
	private double currY;
	private double currY2;
	private PIDLoop spinHandler;
	private Trajectory t;
	private double startTime;
	private double lastTime;
	/**
	 * Constructor initializing the trajectory
	 * @param Trajectory t the path to be followed that has already been calculated with {@link Trajectory#calculateTrajectory() calculateTrajectory()}
	 * @see {@link Trajectory}
	 * 
	 */
	public PathFollower(Trajectory t) {
		this.t = t;
		spinHandler = new PIDLoop(.7,0.0,0.0);
	}
	public void init()
	{
		pathLoop.onStart();
	}
	/**
	 * runs the loop to follow given trajectory
	 */
	public void run()
	{
		pathLoop.onloop();
	}
	Loop pathLoop = new Loop()
			{
				@Override
				public void onStart() {
					startTime = Timer.getFPGATimestamp();
					lastTime = 0.0;
					Drivetrain.getInstance().resetGyro();
				}

				@Override
				public void onloop() {
					double Time = Timer.getFPGATimestamp()-startTime;
					double dt = Time-lastTime;
					double heading = t.getHeading(Time);
					double speed = t.getSpeed(Time);
					double wheelAngle = t.getWheelAngle(Time);
					double strafeCommand = speed*Math.cos(wheelAngle);
					double forwardCommand = speed*Math.sin(wheelAngle);
					double spinCommand = spinHandler.returnOutput(Drivetrain.getInstance().getAngle(), heading);
					currY += forwardCommand * dt;
					SmartDashboard.putNumber("autoSpinCommand", spinCommand);
					SmartDashboard.putNumber("autoSpeed", forwardCommand);
					SmartDashboard.putNumber("calculatedY", currY);
					Drivetrain.getInstance().swerve(forwardCommand, strafeCommand, spinCommand,Drivetrain.driveCoords.FIELDCENTRIC, 
							Drivetrain.driveType.VELOCITY);
					double wheelSpeed = Drivetrain.getInstance().getPod(0).getWheelSpeed();
					double reqWheelSpeed = Drivetrain.getInstance().getPod(0).getSpeed();
					double wheelSpeedfps = wheelSpeed / Constants.fps2ups;
					SmartDashboard.putNumber("wheelSpeed", wheelSpeedfps);
					SmartDashboard.putNumber("requested wheel Speed", reqWheelSpeed);
					currY2 += wheelSpeedfps * dt;
					SmartDashboard.putNumber("currY2", currY2);
					lastTime = Time;
					
				}

				@Override
				public void stop() {
					//  N/A
					
				}
		
			};
	

}