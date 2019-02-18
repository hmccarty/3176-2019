package Auton.Commands;

import Subsystem.Loop;
import edu.wpi.first.wpilibj.Timer;

public class Command {
	private double timeToComplete = 0.0;
	private boolean isTriggered = false;
	private boolean isFinished =false;
	private Loop commandLoop;
	private boolean firstTime = true;
	private double currTime = 0.0;
	public double startTimeOfCommand;
	/**
	 * timeBased : command runs for x seconds then stops <p>
	 * triggerBased : command runs until a trigger
	 */
	public enum commandType {
		timeBased,
		triggerBased
	}
	private commandType type;
	/**Constructor for the command to initialize the command 
	 * @param type the type of the commands {@link commandType}
	 * @param timeToComplete the time in second the command lasts
	 */
	public Command(commandType type, double timeToComplete)
	{
		this.type = type;
		this.timeToComplete = timeToComplete;
	}
	/**
	 * Constructor for the command to initialize the command 
	 * @param type the type of the commands {@link commandType}
	 */
	public Command(commandType type)
	{
		this.type = type;
	}
	/**
	 * sets the command loop to be executed. 
	 * Methods that are called iteratively should be put in run. 
	 * Methods that are called only once should be put in onStart
	 * @param l
	 */
	public void setLoop(Loop l)
	{
		commandLoop = l;
	}
	/**
	 * checks to see if the command is done based on either time or trigger based
	 * @return boolean is the command finished
	 */
	private boolean checkEndCondition()
	{
		if(type == commandType.timeBased)
		{
			if(currTime <= timeToComplete) {return false;} else {return true;}
		}
		else
		{
			if(!isTriggered) {return false;} else {return true;}
		}
	}
	/**
	 * called to set a trigger for if it is finished
	 * @param isTriggered boolean true for if finished false if still executing
	 */
	public void setTrigger(boolean isTriggered)
	{
		this.isTriggered = isTriggered;
	}
	/**
	 * @return if the command is finished
	 */
	public boolean getIsFinished()
	{
		return isFinished;
	}
	/**
	 * records start time from FPGA
	 * runs the onStart() of the commandLoop
	 */
	public void runOnStart()
	{
		startTimeOfCommand = Timer.getFPGATimestamp();
		commandLoop.onStart();
	}
	/**
	 * sets current time based off start time from FPGA
	 * runs the onloop() of the commandLoop
	 */
	public void runOnloop()
	{
		currTime = Timer.getFPGATimestamp()-startTimeOfCommand;
		commandLoop.onloop();
	}
	/**
	 * sets isFinished to true
	 * runs the stop() of the commandLoop
	 */
	public void runStop()
	{
		isFinished = true;
		commandLoop.stop();
	}
	/**
	 * runs onStart() then runs onLoop() until {@link #checkEndCondition()} is true then runs stop()
	 */
	public void run()
	{
		if(firstTime)
		{
			startTimeOfCommand = Timer.getFPGATimestamp();
			//System.out.println("initLoop with: " + checkEndCondition() + " as end condition");
			commandLoop.onStart();
			firstTime = false;
		}
		else if(!checkEndCondition())
		{
			currTime = Timer.getFPGATimestamp()-startTimeOfCommand;
			//System.out.println("doing loop");
			commandLoop.onloop();
		}
		else
		{
			commandLoop.stop();
			isFinished = true;
		}
	}

}
