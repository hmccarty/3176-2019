package Subsystem;

import com.kauailabs.navx.frc.AHRS; //Need to install Nav-X libraries to use
import Auton.Kinematics;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;
import org.usfirst.frc.team6713.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import Util.PIDLoop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/** 
 * Handles crab drive states and manages individual swervepods, see {@link Swervepod}
 */
public class Drivetrain extends Subsystem {
	private static Drivetrain instance = new Drivetrain();
	private Loop_Manager loopMan = Loop_Manager.getInstance();
	private Controller controller = Controller.getInstance(); 
	private PixyCam cam = PixyCam.getInstance();
	private PowerDistributionPanel pdp = new PowerDistributionPanel(0);
	private AHRS gyro;
	private Kinematics kinematics;
	
	private ArrayList<Swervepod> Pods;
	
	private Swervepod upperRight;
	private Swervepod upperLeft;
	private Swervepod lowerLeft;
	private Swervepod lowerRight;
	
	private driveCoords Coords;
	private driveType commandType;
	
	private double currTime; 
	private double lastTime = Timer.getFPGATimestamp();
	
	public TalonSRX[] driveTalon = {new TalonSRX(1), new TalonSRX(2), new TalonSRX(3), new TalonSRX(4)}; 
	public TalonSRX[] gearTalon = {new TalonSRX(11), new TalonSRX(22), new TalonSRX(33), new TalonSRX(44)};
	
	private double kLength;
	private double kWidth;
	private double kRadius;

	private PIDLoop pidRotate;
	private PIDLoop pidForward;
	private PIDLoop pidStrafe;
	
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
		AUTON, 
		SECRET_SAUCE
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
	
	private Drivetrain(){
		//instantiate the pods
		upperRight = new Swervepod(0, driveTalon[0], gearTalon[0]);
		upperLeft = new Swervepod(1, driveTalon[1], gearTalon[1]);
		lowerLeft = new Swervepod(2, driveTalon[2], gearTalon[2]);
		lowerRight = new Swervepod(3, driveTalon[3], gearTalon[3]);
		
		//Instantiate array list
		Pods = new ArrayList<Swervepod>();
		
		//PID Loops for Macro-tasks, ex. Secret Sauce
		pidRotate = new PIDLoop(0.0015,0,0, .5);
		pidForward = new PIDLoop(.06,0,0, 3.0);
				
		//Add instantiated Pods to the array list
		Pods.add(upperRight);
		Pods.add(upperLeft);
		Pods.add(lowerLeft);
		Pods.add(lowerRight);
		
		//Setting constants
		kLength = Constants.DRIVETRAINLENGTH;
		kWidth = Constants.DRIVETRAINWIDTH;
		kRadius = Math.sqrt(Math.pow(kLength,2)+Math.pow(kWidth,2));
		kMaxSpeed = Constants.DRIVETRAINMAXWHEELSPEED;
		kMaxRotation = Constants.DRIVETRAINMAXROTATIONSPEED;
		
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
	public static Drivetrain getInstance(){
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
		for(Swervepod p:Pods) {
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
	
	public Swervepod getPod(int idx) {return Pods.get(idx);}
	
	public double getAvgWheelSpeed() {
		double average =0;
		for(Swervepod pod: Pods) {
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

	@Override public boolean checkSystem() {return false;}
	
	@Override
	public void registerLoop()
	{
		loopMan.addLoop(new Loop() {
		@Override
		public void onStart() {
			currentState = systemStates.NEUTRAL;
			requestedState = systemStates.NEUTRAL;		
		}
		@Override
		public void onloop() {
			if(controller.getGyroResetButton()) {
				resetGyro();
			}
			
			updateAngle();
			switch(currentState) {
				case NEUTRAL:
					checkState();
					lastState = systemStates.NEUTRAL;
				case DRIVE:
					crabDrive();
					lastState = systemStates.DRIVE;
					checkState();
					break;
				case SECRET_SAUCE:
					if(lastState != systemStates.SECRET_SAUCE) {
						secretSauceTimer.start();
						secretSauceTimer.reset();
						Coords = driveCoords.ROBOTCENTRIC;
						Intake.getInstance().setWantedState(Intake.systemStates.Intaking);
					}
					if(secretSauceTimer.get() < .3) {
						forwardCommand = -1.0;
					}
					else if(secretSauceTimer.get() < 1) {
						forwardCommand = 3.0;
					}
					else if(!Intake.getInstance().isCubeIn()){
						forwardCommand = -2.25;
					}
					else {
						forwardCommand = 0.0;
					}
					crabDrive();
					lastState = systemStates.SECRET_SAUCE;
					checkState();
					break;
				case VISION:
					Coords = driveCoords.ROBOTCENTRIC;
					commandType = driveType.PERCENTPOWER;
					if(!Intake.getInstance().isCubeIn()) {
						spinCommand = pidRotate.returnOutput(cam.getAvgX(), 175);
						forwardCommand = -pidForward.returnOutput(175-cam.getY()) * (1/((Math.abs(cam.getAvgX()-175)/90))); /* - (-2.5 * pidForward.returnOutput(60, Math.abs(175-cam.getAvgX())))*/;
						if(forwardCommand > 1.0)
						{
							forwardCommand = 1.0;
						}
						else if(forwardCommand < -3.0)
						{
							forwardCommand = -3.0;
						}
					}
					else {
						spinCommand = 0.0;
						forwardCommand = 0.0;
						strafeCommand = 0.0;
					}
					SmartDashboard.putNumber("Spin", spinCommand);
					SmartDashboard.putNumber("Forward", forwardCommand);
					crabDrive();
					lastState = systemStates.VISION;
					checkState();
					break;
				default:
					break;			
				}
			outputToSmartDashboard();
			kinematics.update();
		}	
		@Override
		public void stop() {				
		}
	});
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Vision X", cam.getAvgX());
		SmartDashboard.putNumber("Vision Area", cam.getAvgArea());
		SmartDashboard.putNumber("kinematics Position", kinematics.getY());
		SmartDashboard.putNumber("angle", getAngle());
	}
}
	

