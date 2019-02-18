package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;

public class DeployIntake extends Command {
	/**
	 * CommandType: timeBased  <p>
	 * TimeToComplete/Trigger: .5 s <p>
	 * deploys the {@link Intake} and sets intake to {@link systemStates Neutral} 
	 */
	public DeployIntake() {
		super(commandType.timeBased,.5);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.Neutral);
				Intake.getInstance().setPosition(Intake.getInstance().downPosition_p);
			}
			@Override
			public void onloop() {
				
			}
			@Override
			public void stop() {

			}
		});
	}
}
