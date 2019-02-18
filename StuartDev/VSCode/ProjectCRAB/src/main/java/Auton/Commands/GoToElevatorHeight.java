package Auton.Commands;

import Auton.Trajectory;
import Subsystem.Elevator;
import Subsystem.Loop;

public class GoToElevatorHeight extends Command {
	private double Height;
	/**
	 * CommandType: trigger <p>
	 * TimeToComplete/Trigger: when elevator has reached the requested height <p>
	 * Commands the elevator to go to a height with the {@link Elevator#setWantedFloor(double) ElevatorSetWantedFloor()}
	 * @param Height (double) the height in encoder units for the elevator to move to
	 */
	public GoToElevatorHeight(double Height) {
		super(commandType.triggerBased);
		this.Height = Height;
		super.setLoop(new Loop() {
			@Override
			public void onStart() {
				Elevator.getInstance().setWantedState(Elevator.systemStates.POSITION_FOLLOW);
				Elevator.getInstance().setWantedFloor(Height);
			}
			@Override
			public void onloop() {
				//Elevator.getInstance().setWantedState(Elevator.systemStates.POSITION_FOLLOW);
				setTrigger(isAtHeight());
			}
			@Override
			public void stop() {

			}
		});
	}
	/**
	 * Checks to see if the elevator is at the right height
	 * @return if the Elevator is within the margin of error
	 */
	public boolean isAtHeight()
	{
		if(Math.abs(Elevator.getInstance().getHeight()-Height)<10000)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Calls the super {@link Command#setTrigger(boolean) setTrigger}
	 */
	public void setTrigger(boolean t)
	{
		super.setTrigger(t);
	}

}
