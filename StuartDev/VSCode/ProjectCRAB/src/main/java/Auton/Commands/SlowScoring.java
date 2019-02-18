package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;

public class SlowScoring extends Command {
	/**
	 * CommandType: timeBased <p>
	 * sets intake to {@link systemStates SlowScoring} for scoreTime time
	 * @param scoreTime the time to score in s
	 */
	public SlowScoring(double scoreTime) {
		super(commandType.timeBased,scoreTime);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.SlowScoring);
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
