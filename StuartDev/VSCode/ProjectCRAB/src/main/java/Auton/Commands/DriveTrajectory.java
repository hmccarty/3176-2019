package Auton.Commands;

import Auton.PathFollower;
import Auton.Trajectory;
import Subsystem.Loop;
import Subsystem.*;

public class DriveTrajectory extends Command {
  private PathFollower p;
  	 /**
	 * CommandType: timeBased  <p>
	 * TimeToComplete/Trigger: the {@link Trajectory#getTimeToComplete()} <p>
	 * drives a Trajectory or Path with a {@link PathFollower} call as new DriveTrajectory(ExamplePath.main.get()) 
	 * @param Trajectory t the trajectory to be driven. Use the {@link Path} to calculate the trajectory. 
	 */
	public DriveTrajectory(Trajectory t) {
		super(commandType.timeBased,t.getTimeToComplete());
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				p = new PathFollower(t);
				p.init();
			}
			@Override
			public void onloop() {
				p.run();
			}
			@Override
			public void stop() {
				Drivetrain.getInstance().swerve(0.0,0.0,0.0);
			}
		});
    
	}

}
