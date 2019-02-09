package frc.subsystem; 

import com.kauailabs.navx.frc.AHRS; //Need to install Nav-X libraries to use
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;
import frc.robot.constants;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/** 
 * Handles crab drive states and manages individual swervepods, see {@link Swervepod}
 */
public class drivetrain extends subsystem {
	private static drivetrain instance = new drivetrain();
	private loopmanager mLoopMan = loopmanager.getInstance();
	private controller mController = controller.getInstance(); 
	private PowerDistributionPanel pdp = new PowerDistributionPanel(0);
	private AHRS gyro;
	
	private ArrayList<swervepod> Pods;
	
	private swervepod upperRight;
	private swervepod upperLeft;
	private swervepod lowerLeft;
	private swervepod lowerRight;
	
	private driveCoords Coords;
	private driveType commandType;
	
	private double currTime; 
	private double lastTime = Timer.getFPGATimestamp();
	
	public TalonSRX[] driveTalon = {new TalonSRX(1), new TalonSRX(2), new TalonSRX(3), new TalonSRX(4)}; 
	public TalonSRX[] gearTalon = {new TalonSRX(11), new TalonSRX(22), new TalonSRX(33), new TalonSRX(44)};
	
	private double kLength;
	private double kWidth;
	private double kRadius;
	
	private double kMaxSpeed;
	private double kMaxRotation;
	
	private double rel_max_speed;
	private double angle;
	
	private double forwardCommand;
	private double strafeCommand;
	private double spinCommand;
	
	private Timer secretSauceTimer; 
	
	public enum systemStates{
		NEUTRAL,
		HOMING,
		DRIVE,
		PARK,
		VISION, 
		AUTON
	}
	
	public enum driveCoords{
		ROBOTCENTRIC,
		FIELDCENTRIC
	}
	
	public enum driveType{
		PERCENTPOWER,
		VELOCITY
	}
	
	private systemStates currentState;
	private systemStates requestedState;
	private systemStates lastState;
	
	private drivetrain(){
		//instantiate the pods
		upperRight = new swervepod(0, driveTalon[0], gearTalon[0]);
		upperLeft = new swervepod(1, driveTalon[1], gearTalon[1]);
		lowerLeft = new swervepod(2, driveTalon[2], gearTalon[2]);
		lowerRight = new swervepod(3, driveTalon[3], gearTalon[3]);
		
		//Instantiate array list
		Pods = new ArrayList<swervepod>();
				
		//Add instantiated Pods to the array list
		Pods.add(upperRight);
		Pods.add(upperLeft);
		Pods.add(lowerLeft);
		Pods.add(lowerRight);
		
		//Setting constants
		kLength = constants.DRIVETRAINLENGTH;
		kWidth = constants.DRIVETRAINWIDTH;
		kRadius = Math.sqrt(Math.pow(kLength,2)+Math.pow(kWidth,2));
		kMaxSpeed = constants.DRIVETRAINMAXWHEELSPEED;
		kMaxRotation = constants.DRIVETRAINMAXROTATIONSPEED;
		
		//Instantiating the gyro
		gyro = new AHRS(SPI.Port.kMXP);
		resetGyro();
		updateAngle();
		
		//Initializing the commands
		forwardCommand = Math.pow(10, -15); //Puts wheels in forward-facing direction
		strafeCommand = 0.0;
		spinCommand = 0.0;
		
		secretSauceTimer = new Timer();
	}
	
	/**
	 * Prevents more than one instance of Drivetrain
	 */
	public static drivetrain getInstance(){
		return instance;
	}
	
	/**
	 * Handles each swerve command and communicates with the swervepods
	 */
	private void crabDrive() {
		//Create arrays with the speed and angle of each pod
		double[] podDrive = new double[4];
		double[] podGear = new double[4];
		
		//Calculating components
		double a = strafeCommand + spinCommand * kLength/2; 
		double b = strafeCommand - spinCommand * kLength/2; 
		double c = forwardCommand - spinCommand * kWidth/2; 
		double d = forwardCommand + spinCommand * kWidth/2; 
		
		//Calculating the speed and angle of each pod
		podDrive[0] = Math.sqrt(Math.pow(b, 2)+ Math.pow(c, 2));
		podGear[0] = Math.atan2(b,c);
		
		podDrive[1] = Math.sqrt(Math.pow(b, 2)+ Math.pow(d, 2));
		podGear[1] = Math.atan2(b,d);
		
		podDrive[2] = Math.sqrt(Math.pow(a, 2)+ Math.pow(d, 2));
		podGear[2] = Math.atan2(a,d);
		
		podDrive[3] = Math.sqrt(Math.pow(a, 2)+ Math.pow(c, 2));
		podGear[3] = Math.atan2(a,c);
		
		//Finding the highest commanded velocity between the pods
		rel_max_speed = Math.max(Math.max(podDrive[0],podDrive[1]),Math.max(podDrive[2], podDrive[3]));
		
		//Reducing pods by the relative max speed
		if(rel_max_speed > kMaxSpeed) {
			for(int idx = 0; idx < Pods.size(); idx++) {
				podDrive[idx] /= rel_max_speed/kMaxSpeed;
			}
		}
		
		boolean allPodsStopped = true;
		for(swervepod p:Pods) {
			if(!p.isStopped())
			{
				allPodsStopped = false;
			}
		}
		
		//If enabled, sends each pod to a defensive lock when not moving 
		if(allPodsStopped && forwardCommand == 0.0 && strafeCommand == 0.0 && spinCommand == 0.0 && false) {
			// Sending each pod their respective commands
			Pods.get(0).setPod(0.0,-1.0*Math.PI/4.0);
			Pods.get(1).setPod(0.0, 1.0*Math.PI/4.0);
			Pods.get(2).setPod(0.0, 3.0*Math.PI/4.0);
			Pods.get(3).setPod(0.0, -3.0* Math.PI/4.0);
		}
		else {
			//Sending each pod their respective commands
			for(int idx = 0; idx < Pods.size(); idx++) {
				Pods.get(idx).setPod(podDrive[idx],podGear[idx]); 
			}
		}
	}
	
	/**
	 * Determines the settings of swerve drive, and the current commands
	 * @param forwardCommand the magnitude on the Y-Axis 
	 * @param strafeCommand the magnitude on the X-Axis 
	 * @param spinCommand the magnitude on the Omega Axis 
	 * @param Coords determines whether swerve is in Robot-Centric or Field-Centric
	 * @param commandType determines whether commanding values are in percent power (-1 to 1) or their intended velocity values (in ft/s)
	 */
	public void swerve(double forwardCommand, double strafeCommand, double spinCommand, driveCoords Coords, driveType commandType) {
		this.Coords = Coords;
		this.commandType = commandType;	
		if(Coords == driveCoords.ROBOTCENTRIC) {
			this.forwardCommand = forwardCommand;
			this.strafeCommand = strafeCommand;
			this.spinCommand = -spinCommand;
		} 
		else {
			final double temp = forwardCommand * Math.sin(angle) + strafeCommand * Math.cos(angle);
		    this.strafeCommand = (-forwardCommand * Math.cos(angle) + strafeCommand * Math.sin(angle));
		    this.forwardCommand = temp;
		    this.spinCommand = -spinCommand;
		}
		if(commandType == driveType.PERCENTPOWER) {
			this.forwardCommand *= kMaxSpeed;
			this.strafeCommand *= kMaxSpeed;
			this.spinCommand *= kMaxRotation;
		}
	}
	
	public void swerve(double forwardCommand, double strafeCommand, double spinCommand) {
		if(Coords == driveCoords.ROBOTCENTRIC) {
			this.forwardCommand = forwardCommand;
			this.strafeCommand = strafeCommand;
			this.spinCommand = -spinCommand;
		}
		else {
			final double temp = forwardCommand * Math.sin(angle) + strafeCommand * Math.cos(angle);
		    this.strafeCommand = (-forwardCommand * Math.cos(angle) + strafeCommand * Math.sin(angle));
		    this.forwardCommand = temp;
		    this.spinCommand = -spinCommand;
		}
		if(commandType == driveType.PERCENTPOWER) {
			this.forwardCommand *= kMaxSpeed;
			this.strafeCommand *= kMaxSpeed;
			this.spinCommand *= kMaxRotation;
		}
	}
	
	public void setSystemState(systemStates wanted) {
		requestedState = wanted;
	}
	
	public void checkState() {
		if(requestedState!=currentState) {
			currentState = requestedState;
		}
	}
	
	public PowerDistributionPanel getPDP() {return pdp;}
	
	public swervepod getPod(int idx) {return Pods.get(idx);}
	
	public double getAvgWheelSpeed() {
		double average =0;
		for(swervepod pod: Pods) {
			average += pod.getWheelSpeed();
		}
		return average/Pods.size();
	}
	
	public double getAngle() {return ((gyro.getAngle()* Math.PI/180.0) % (2*Math.PI));} //Converts Gyro Angle (0-360) to Radians (0-2pi)
	
	private void updateAngle(){
		//-pi to pi 0 = straight ahead
		angle = ((((gyro.getAngle()+90)* Math.PI/180.0)) % (2*Math.PI));
	}
	
	public void resetGyro() {gyro.reset();}
	
	@Override public void zeroAllSensors() {
		for(int idx = 0; idx < 4; idx++)
		{
			Pods.get(idx).zeroAllSensors();
		}
	}
	
	@Override
	public void registerLoop()
	{
		mLoopMan.addLoop(new loop() {
		@Override
		public void onStart() {
			currentState = systemStates.NEUTRAL;
			requestedState = systemStates.NEUTRAL;		
		}
		@Override
		public void onLoop() {
			if(mController.getGyroReset()) {
				resetGyro();
			}
			updateAngle();
			switch(currentState) {
				case NEUTRAL:
					checkState();
					lastState = systemStates.NEUTRAL;
					break;
				case DRIVE:
					crabDrive();
					lastState = systemStates.DRIVE;
					checkState();
					break;
				default:
					break;			
				}
			outputToSmartDashboard();
		}
		@Override
		public void onStop(){}
	});
	}

	@Override
	public void outputToSmartDashboard() {}
}