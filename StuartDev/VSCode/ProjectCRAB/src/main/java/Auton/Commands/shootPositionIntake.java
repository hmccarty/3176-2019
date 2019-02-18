package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;


public class shootPositionIntake extends Command {
	/**
	 * CommandType: time based <p>
	 * TimeToComplete: .5 s <p>
	 * sets intake position to -8000 and intake state to {@link systemStates Neutral}
	 */
	public shootPositionIntake() {
		super(commandType.timeBased,.5);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.Neutral);
				Intake.getInstance().setPosition(-8000);
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
