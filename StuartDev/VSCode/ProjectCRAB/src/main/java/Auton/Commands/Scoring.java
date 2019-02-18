package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;


public class Scoring extends Command {
	/**
	 * CommandType: Time <p>
	 * sets intakes wanted state to {@link systemStates Scoring}
	 * @param scoreTime the time to score in s
	 */
	public Scoring(double scoreTime) {
		super(commandType.timeBased,scoreTime);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.Scoring);
			}
			@Override
			public void onloop() {

			}
			@Override
			public void stop() {
				Intake.getInstance().setWantedState(systemStates.Neutral);
			}
		});
    
	}
}
