package Subsystem;

/**
 *  Used to define every distinct robot function. 
 *  Subsystems are coded as state machines and will be given Loops to accomplish tasks.
 *  See {@link Drivetrain} as an example.
 */
public abstract class Subsystem {

	public Subsystem() {}
	
	public abstract void zeroAllSensors();
	
	/**
	 *  Checks in with the Subsystem to see if still functional
	 */
	public abstract boolean checkSystem();
	
	/**
	 *  Outputs all wanted values to the WPILIB Dashboard
	 */
	public abstract void outputToSmartDashboard();
	
	/**
	 *  Creates a new loop for each subsystem, that is then handled by {@link Loop_Manager}
	 */
	public abstract void registerLoop();
	
}
