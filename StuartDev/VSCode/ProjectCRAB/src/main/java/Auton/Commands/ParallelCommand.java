package Auton.Commands;

import java.util.ArrayList;
import Subsystem.Loop;

public class ParallelCommand extends Command {
	private ArrayList<Command> commands;
	/**
	 * Creates a new command that executes both commands simultaneously. 
	 * Trigger based see {@link ParallelCommand#areAllCommandsFinished() areAllCommandsFinished()} 
	 * @param c1 Command 1
	 * @param c2 Command 2
	 */
	public ParallelCommand(Command c1,Command c2) {
		super(commandType.triggerBased);
		commands = new ArrayList<Command>();
		commands.add(c1);
		commands.add(c2);
		setLoopConstructor();
	}
	/**
	 * Creates a new command that executes both commands simultaneously. 
	 * Trigger based see {@link ParallelCommand#areAllCommandsFinished() areAllCommandsFinished()} 
	 * @param c1 Command 1
	 * @param c2 Command 2
	 * @param c3 Command 3
	 */
	public ParallelCommand(Command c1,Command c2,Command c3) {
		super(commandType.triggerBased);
		commands = new ArrayList<Command>();
		commands.add(c1);
		commands.add(c2);
		commands.add(c3);
		setLoopConstructor();
	}
	private void setLoopConstructor()
	{
		super.setLoop(new Loop() {
			@Override
			public void onStart() {

			}
			@Override
			public void onloop() {
				for(Command command:commands)
				{
					command.run();
				}
				setTrigger(areAllCommandsFinished());
			}
			@Override
			public void stop() {

			}
		});
	}
	/**
	 * checks to see if the commands are finished by polling them individually with the getIsFinished()
	 * @return True: if all commands have finished False: if not all commands are finished
	 */
	private boolean areAllCommandsFinished()
	{
		boolean isDone = true;
		for(Command command:commands)
		{
			if(!command.getIsFinished())
			{
				isDone = false;
			}
		}
		return isDone;
	}
	public void setTrigger(boolean t)
	{
		super.setTrigger(t);
	}
}
