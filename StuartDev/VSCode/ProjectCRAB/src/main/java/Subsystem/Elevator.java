package Subsystem;


import org.usfirst.frc.team6713.robot.Constants;
import Auton.Waypoint;
import Util.PIDLoop;
import Util.Trajectory1D;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem {
	private static Elevator instance = new Elevator();
	private Controller joystick = Controller.getInstance(); 
	private Victor driveMotor;
	private Victor driveMotor2;
	private PIDLoop elevatorControlLoop; 
	private Encoder encoder;
	private double wantedFloor;
	private Trajectory1D motionProfileTrajectory;
	private double liftSpeed;
	private double motionProfileStartTime;
	/**
	 *  NEUTRAL : Do Nothing <p>
	 *	POSITION_FOLLOW : follow wanted height (calls MOTION_PROFILE if needed) <p>
	 *	MOTION_PROFILE : executing a motion profile (Internal Use only) <p>
	 *	OPEN_LOOP : direct current to wheels <p>
	 */
	public enum systemStates {
		NEUTRAL,
		POSITION_FOLLOW,
		MOTION_PROFILE,
		OPEN_LOOP
	}
	
	private systemStates currentState;
	private systemStates wantedState;
	private systemStates lastState;
	private Elevator() {
		encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		driveMotor = new Victor(Constants.ELEVATORMOTOR);
		driveMotor2 = new Victor(6);
		elevatorControlLoop = new PIDLoop(Constants.ELEVATOR_KP, //Proportional Gain
											Constants.ELEVATOR_KI, //Integral Gain
											Constants.ELEVATOR_KD,
											1); //Max Speed
	}
	/**
	 * Prevents more than one instance from being created
	 * @return instance of the Elevator
	 */
	public static Elevator getInstance() {
		return instance; 
	}
	
	/**
	 * sets the setpoint of the control loop for the Elevator
	 * @param wantedHeight the wanted height of the elevator in encoder ticks
	 */
	private void setFloor(double wantedHeight) {
		liftSpeed = elevatorControlLoop.returnOutput(-encoder.getRaw(),wantedHeight);
		driveMotor.set(liftSpeed);
		driveMotor2.set(liftSpeed);
	}
	
	/**
	 * sets the wanted position of the elevato. Must be in POSITION_FOLLOW to respond
	 * @param wF wanted height of the elevator in encoder ticks
	 * @see {@link #setWantedState(systemStates wantedState) setWantedState()}
	 */
	public void setWantedFloor(double wF) {this.wantedFloor = wF;}
	
	/**
	 * gets the raw value from the encoder
	 * @return the encoder ticks from the elevator
	 */
	public double getHeight() {return -encoder.getRaw();}
	
	/**
	 * Requests the desired state for the Elevator
	 * @param wantedState the systemState for the elevator to transition to
	 * @see {@link systemStates}
	 */
 	public void setWantedState(systemStates wantedState) {this.wantedState = wantedState;}
 	
 	/**
 	 * Gives State of the Elevator
 	 * @return current SystemState 
 	 * @see {@link systemStates}
 	 */
 	public systemStates getState() {return currentState;}
 	
 	/**
 	 * resets the winch encoder
 	 */
	@Override public void zeroAllSensors() { encoder.reset();}
	
	@ Override public boolean checkSystem() {return true;}
 	
	/**
	 * checks the wanted state vs the current state to transition states
	 */
 	private void checkState() {
 		if(currentState!=wantedState) {
			currentState=wantedState;
		}
 	}
 	
 	/**
 	 * adds Elevator Loop to the static {@link Loop_Manager} to be run
 	 */
	@Override
	public void registerLoop() {
		Loop_Manager.getInstance().addLoop(new Loop()
		{
			@Override
			public void onStart() {
				currentState = systemStates.NEUTRAL;
				wantedState = systemStates.NEUTRAL;
			}
			@Override
			public void onloop() {
				//outputToSmartDashboard();
				switch(currentState){
					case NEUTRAL:
						driveMotor.set(0.0);
						driveMotor2.set(0.0);
						checkState();
						lastState = systemStates.NEUTRAL;
						break;		
					case OPEN_LOOP:
						driveMotor.set(joystick.elevatorPositionJoystick());
						driveMotor2.set(joystick.elevatorPositionJoystick());
						checkState();
						lastState = systemStates.OPEN_LOOP;
						break;
					case POSITION_FOLLOW:			
						checkState();
						if(Math.abs(wantedFloor-getHeight())>10000)
						{
							currentState = systemStates.MOTION_PROFILE;
						}
						else
						{
							setFloor(wantedFloor);
						}
						lastState = systemStates.POSITION_FOLLOW;
						break;
					case MOTION_PROFILE:
						if(lastState != systemStates.MOTION_PROFILE)
						{
							if(getHeight()<wantedFloor)
							{
								motionProfileTrajectory = new Trajectory1D(100000.0,400000.0);
							}
							else
							{
								motionProfileTrajectory = new Trajectory1D(50000.0,100000.0);
							}
							motionProfileTrajectory.addWaypoint(new Waypoint(getHeight(),0.0,0.0));
							motionProfileTrajectory.addWaypoint(new Waypoint(wantedFloor,0.0,0.0));
							motionProfileTrajectory.calculateTrajectory();
							motionProfileStartTime = Timer.getFPGATimestamp();
						}
						else if(Timer.getFPGATimestamp()-motionProfileStartTime<motionProfileTrajectory.getTimeToComplete())
						{
							setFloor(motionProfileTrajectory.getPosition(Timer.getFPGATimestamp()-motionProfileStartTime));
						}
						else
						{
							currentState = systemStates.POSITION_FOLLOW;
						}
						lastState = systemStates.MOTION_PROFILE;
						break;
				}
			}
			@Override
			public void stop() {
				driveMotor.set(0);
			}
			@Override
			public void test() {
				//Smartdashboard.putNumber("Encoder Units", encoder.getRaw());
			}
		});
	}

	@Override
	public void outputToSmartDashboard() {
		//SmartDashboard.putNumber("Lift Speed", liftSpeed);	driveMotor.set(liftSpeed);
		//SmartDashboard.putNumber("Lift Speed", liftSpeed);	driveMotor.set(liftSpeed);
		SmartDashboard.putString("elevator state", currentState.toString());
		SmartDashboard.putString("elevator wantedState", wantedState.toString());
		SmartDashboard.putNumber("elevator wanted floor", wantedFloor);
		SmartDashboard.putNumber("position", getHeight());
		SmartDashboard.putNumber("Rate of spin", encoder.getRate());
	}
}
