package frc.auton; 

import frc.auton.pathfollower;
import frc.util.trajectory;
import frc.util.loop;
import frc.subsystem.*;

public class visiontrack extends command {
  private visiontrack v; 
  	 /**
	 * CommandType: timeBased  <p>
	 * TimeToComplete/Trigger: the {@link Trajectory#getTimeToComplete()} <p>
	 * drives a Trajectory or Path with a {@link PathFollower} call as new DriveTrajectory(ExamplePath.main.get()) 
	 * @param Trajectory t the trajectory to be driven. Use the {@link Path} to calculate the trajectory. 
	 */
	public visiontrack() {
		super(commandType.triggerBased);
		super.setLoop(new loop() {
			@Override
			public void onStart() {
                drivetrain.getInstance().cAutonVision(true);
			}
			@Override
			public void onLoop() {
				setTrigger(drivetrain.getInstance().isAtTarget());
			}
			@Override
			public void onStop() {
                drivetrain.getInstance().cAutonVision(false);
			}
		});
    
	}

}
