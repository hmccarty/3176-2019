package frc.auton;

import java.util.ArrayList;
import frc.auton.command;
/**
 * manages and runs commands
 */
public class autonmanager {
	private ArrayList<command> commands;
	private int commandIdx = 0;
	private boolean isFirstTime = true;
	public autonmanager() {
		commands = new ArrayList<command>();
	}
	/**
	 * Main method call to que commands for AutoManager
	 * @param c Command to be qeued and executed during {@link #run() run()}
	 */
	public void qeueCommand(command c) {
		commands.add(c);
	}
	/**
	 * Reports all commands to console as qeued <p>
	 * Runs commands {@link Command#run() run()} in sequence based on the Commands {@link Command#getIsFinished() getIsFinished()} <p>
	 *  run() should be called Iteratively 
	 */
	public void run() {
		if(isFirstTime) {
			System.out.println("------Auto Started--------");
			for(command c: commands) {
				System.out.println("queued: " + c.toString());
			}
			isFirstTime = false;
		}
		else {
			commands.get(commandIdx).run();
			if(commands.get(commandIdx).getIsFinished() && commandIdx<commands.size()-1) {
				System.out.println("------Finished " + commands.get(commandIdx).toString() + "--------");
				commandIdx++;
			}
		}
	}
	
}
