package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;


public class StowIntake extends Command {
	/**
	 * CommandType: timeBased  <p>
	 * TimeToComplete/Trigger: .5 s <p>
	 * deploys the {@link Intake} and sets intake to {@link systemStates Neutral} 
	 */
	public StowIntake() {
		super(commandType.timeBased,.5);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.Neutral);
				Intake.getInstance().setPosition(0.0);
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
