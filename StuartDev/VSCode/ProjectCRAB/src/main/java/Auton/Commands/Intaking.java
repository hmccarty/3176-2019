package Auton.Commands;

import Subsystem.Intake;
import Subsystem.Intake.systemStates;
import Subsystem.Loop;

public class Intaking extends Command {
	/**
	 * CommandType: trigger <p>
	 * TimeToComplete/Trigger: when {@link Intake#isCubeIn()}  is true <p>
	 * deploys intake and starts sets wanted state as {@link systemStates Intaking} 
	 */
	public Intaking() {
		super(commandType.triggerBased);
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Intake.getInstance().setWantedState(systemStates.Intaking);
			}
			@Override
			public void onloop() {
				Intake.getInstance().setWantedState(systemStates.Neutral);
				setTrigger(Intake.getInstance().isCubeIn());
			}
			@Override
			public void stop() {

			}
		});
	}
	public void setTrigger(boolean t)
	{
		super.setTrigger(t);
	}
}
