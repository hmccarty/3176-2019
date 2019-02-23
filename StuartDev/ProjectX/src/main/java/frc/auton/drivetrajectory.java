package frc.auton; 

import frc.auton.pathfollower;
import frc.util.trajectory;
import frc.util.loop;
import frc.subsystem.*;

public class drivetrajectory extends command {
  private pathfollower p;
  	 /**
	 * CommandType: timeBased  <p>
	 * TimeToComplete/Trigger: the {@link Trajectory#getTimeToComplete()} <p>
	 * drives a Trajectory or Path with a {@link PathFollower} call as new DriveTrajectory(ExamplePath.main.get()) 
	 * @param Trajectory t the trajectory to be driven. Use the {@link Path} to calculate the trajectory. 
	 */
	public drivetrajectory(trajectory t) {
		super(commandType.timeBased,t.getTimeToComplete());
		super.setLoop(new loop() {
			@Override
			public void onStart() {
				p = new pathfollower(t);
				p.init();
			}
			@Override
			public void onLoop() {
				p.run();
			}
			@Override
			public void onStop() {
                drivetrain.getInstance().setForwardCommand(0);
                drivetrain.getInstance().setStrafeCommand(0);
                drivetrain.getInstance().setSpinCommand(0);
			}
		});
    
	}

}
