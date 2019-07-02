package frc.auton;



import frc.robot.constants;
import frc.subsystem.drivetrain;
import frc.util.loop; 
import frc.util.pid; 
import frc.util.trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * follows a trajectory and commands the drivetrain
 */
public class pathfollower {
	private double currY;
	private double currY2;
	private pid spinHandler;
	private trajectory t;
	private double startTime;
	private double lastTime;
	/**
	 * Constructor initializing the trajectory
	 * @param Trajectory t the path to be followed that has already been calculated with {@link Trajectory#calculateTrajectory() calculateTrajectory()}
	 * @see {@link Trajectory}
	 * 
	 */
	public pathfollower(trajectory t) {
		this.t = t;
		spinHandler = new pid(.4,0.0,0.0);
	}
	public void init() {
		pathLoop.onStart();
	}
	/**
	 * runs the loop to follow given trajectory
	 */
	public void run() {
		pathLoop.onLoop();
	}
	loop pathLoop = new loop() {
				@Override
				public void onStart() {
					startTime = Timer.getFPGATimestamp();
					lastTime = 0.0;
					drivetrain.getInstance().resetGyro();
				}

				@Override
				public void onLoop() {
					double Time = Timer.getFPGATimestamp()-startTime;
					double dt = Time-lastTime;
					double heading = t.getHeading(Time);
					double speed = t.getSpeed(Time);
					double wheelAngle = t.getWheelAngle(Time);
					double strafeCommand = speed*Math.cos(wheelAngle);
					double forwardCommand = speed*Math.sin(wheelAngle);
					double spinCommand = spinHandler.returnOutput(drivetrain.getInstance().getAngle(), heading);
					currY += forwardCommand * dt;
					//SmartDashboard.putNumber("autoSpinCommand", spinCommand);
					SmartDashboard.putNumber("autoSpeed", forwardCommand);
					SmartDashboard.putNumber("calculatedY", currY);
                    drivetrain.getInstance().setForwardCommand(forwardCommand);
                    drivetrain.getInstance().setStrafeCommand(strafeCommand);
                    drivetrain.getInstance().setSpinCommand(spinCommand);
					double wheelSpeed = drivetrain.getInstance().getPod(0).getWheelSpeed();
					double reqWheelSpeed = drivetrain.getInstance().getPod(0).getSpeed();
					double wheelSpeedfps = wheelSpeed / 714.0;//constants.FPS_TO_UPS;
					SmartDashboard.putNumber("wheelSpeed", wheelSpeedfps);
					SmartDashboard.putNumber("requested wheel Speed", reqWheelSpeed);
					currY2 += wheelSpeedfps * dt;
					SmartDashboard.putNumber("currY2", currY2);
					lastTime = Time;
					
				}

				@Override
				public void onStop() {}		
			};
}