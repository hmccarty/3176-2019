package frc.subsystem; 

import com.kauailabs.navx.frc.AHRS; 
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import java.util.ArrayList;
import frc.robot.constants;
import edu.wpi.first.wpilibj.smartdashboard.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.util.*;

/** 
 * Handles crab drive states and manages individual swervemPods, see {@link Swervepod}
 */
public class drivetrain extends subsystem {
	private static drivetrain instance = new drivetrain();
	private loopmanager mLoopMan = loopmanager.getInstance();
	private vision mVision = vision.getInstance();
	private controller mController = controller.getInstance(); 
	private PowerDistributionPanel mPDP = new PowerDistributionPanel(0);
	private AHRS mGyro;
	
	private ArrayList<neopod> mNeoPods;

	private neopod mUpperRight;
	private neopod mUpperLeft;
	private neopod mLowerLeft;
	private neopod mLowerRight;
	
	private coordType mCoordType;
	private inputType mInputType;

	private boolean cAutonVision; 

	private pid mVisionForward;
	private pid mVisionTurn;  
	private pid mVisionStrafe; 

	private pid mSpinMaster; 
	
	private double lastGyroClock; 
	
	
	public CANSparkMax[] mDriveSparks = {new CANSparkMax(constants.DRIVE_ONE, MotorType.kBrushless), 
										 new CANSparkMax(constants.DRIVE_TWO, MotorType.kBrushless),
										 new CANSparkMax(constants.DRIVE_THREE, MotorType.kBrushless),
										 new CANSparkMax(constants.DRIVE_FOUR, MotorType.kBrushless)}; 
	public TalonSRX[] mGearTalons = {new TalonSRX(constants.STEER_ONE), 
									 new TalonSRX(constants.STEER_TWO),
									 new TalonSRX(constants.STEER_THREE),
									 new TalonSRX(constants.STEER_FOUR)};
	
	private double kLength;
	private double kWidth;
	
	private double kMaxSpeed;
	private double kMaxRotation;
	private double kMaxAccel;
	private double kMaxVel;
	
	private double relMaxSpeed;
	private double currentAngle;
	private double lastAngle; 
	
	private double forwardCommand;
	private double strafeCommand;
	private double spinCommand;
	
	public enum state {
		NEUTRAL,
		HOMING,
		DRIVE,
		VISION,
		AUTON
	}
	
	public enum coordType {
		ROBOTCENTRIC,
		FIELDCENTRIC,
		BACKROBOTCENTRIC
	}
	
	public enum inputType {
		PERCENTPOWER,
		VELOCITY
	}
	
	private state mCurrentState;
	private state mWantedState;
	
	private drivetrain(){
		//instantiate the mPods
		mUpperRight = new neopod(0, mDriveSparks[0], mGearTalons[0]);
		mUpperLeft = new neopod(1, mDriveSparks[1], mGearTalons[1]);
		mLowerLeft = new neopod(2, mDriveSparks[2], mGearTalons[2]);
		mLowerRight = new neopod(3, mDriveSparks[3], mGearTalons[3]);

		//set drive type
		mCoordType = coordType.FIELDCENTRIC;
		mInputType = inputType.PERCENTPOWER;

		cAutonVision = false;

		mVisionForward = new pid(0.009, 0, 0, .8); 
		mVisionTurn = new pid(0.026, 0, 0, .8); 
		mVisionStrafe = new pid(0.015,0, 0, .8); 

		mSpinMaster = new pid(0.0001, 0.0, 0, 0.5);

		
		//Instantiate array list
		mNeoPods = new ArrayList<neopod>();
			
		//Add instantiated mPods to the array list
		mNeoPods.add(mUpperRight);
		mNeoPods.add(mUpperLeft);
		mNeoPods.add(mLowerLeft);
		mNeoPods.add(mLowerRight);
		
		//Setting constants
		kLength = constants.DRIVETRAIN_LENGTH;
		kWidth = constants.DRIVETRAIN_WIDTH;

		kMaxSpeed = constants.DRIVETRAIN_MAX_WHEEL_SPEED;
		kMaxRotation = constants.DRIVETRAIN_MAX_ROTATION_SPEED;

		kMaxAccel = constants.NEO_MAX_ACCEL;
		kMaxVel = constants.NEO_MAX_VEL;
		
		//Instantiating the Gyro
		mGyro = new AHRS(SPI.Port.kMXP);
		resetGyro();
		updateAngle();
		
		//Start wheels in a forward facing direction
		forwardCommand = Math.pow(10, -15); 
		strafeCommand = 0.0;
		spinCommand = 0.0;
	}
	
	/**
	 * Prevents more than one instance of Drivetrain
	 */
	public static drivetrain getInstance(){
		return instance;
	}
	
	/**
	 * Handles each swerve command and communicates with the Swerve Pods
	 * 
	 * For a better understanding of the equations, see Ether's Swerve whitepaper: 
	 * https://drive.google.com/file/d/1Ny6jm400zbYKn7ZM9AllUX-0SE9cbts2/view?usp=sharing 
	 */
	private void crabDrive() {
		if(mCoordType == coordType.FIELDCENTRIC){
			final double temp = forwardCommand * Math.sin(currentAngle) + strafeCommand * Math.cos(currentAngle);
		    strafeCommand = (-forwardCommand * Math.cos(currentAngle) + strafeCommand * Math.sin(currentAngle));
		    forwardCommand = temp;
		}

		if(mCoordType == coordType.BACKROBOTCENTRIC){
			strafeCommand *= -1;
			forwardCommand *= -1;
		}

		if(mCoordType == coordType.ROBOTCENTRIC) {
			strafeCommand *= .75;
			forwardCommand *= .75;
			spinCommand *= .75;
		}

		if(mInputType == inputType.PERCENTPOWER){
			forwardCommand *= kMaxSpeed;
			strafeCommand *= kMaxSpeed;
			spinCommand *= kMaxRotation;
		}

		//Create arrays with the speed and angle of each pod
		double[] podDrive = new double[4];
		double[] podGear = new double[4];
		
		//Calculating components
		double a = strafeCommand + spinCommand * getRadius("A"); 
		double b = strafeCommand - spinCommand * getRadius("B"); 
		double c = forwardCommand - spinCommand * getRadius("C"); 
		double d = forwardCommand + spinCommand * getRadius("D"); 
		
		//Calculating the speed and angle of each pod
		podDrive[0] = Math.sqrt(Math.pow(b, 2)+ Math.pow(c, 2));
		podGear[0] = Math.atan2(b,c);
		
		podDrive[1] = Math.sqrt(Math.pow(b, 2)+ Math.pow(d, 2));
		podGear[1] = Math.atan2(b,d);
		
		podDrive[2] = Math.sqrt(Math.pow(a, 2)+ Math.pow(d, 2));
		podGear[2] = Math.atan2(a,d);
		
		podDrive[3] = Math.sqrt(Math.pow(a, 2)+ Math.pow(c, 2));
		podGear[3] = Math.atan2(a,c);
		
		//Finding the highest commanded velocity between the mPods
		relMaxSpeed = Math.max(Math.max(podDrive[0],podDrive[1]),Math.max(podDrive[2], podDrive[3]));
		
		//Reducing mPods by the relative max speed
		if(relMaxSpeed > kMaxSpeed) {
			for(int idx = 0; idx < mNeoPods.size(); idx++) {
				podDrive[idx] /= relMaxSpeed/kMaxSpeed;
			}
		}
		
		//If enabled, sends each pod to a defensive lock when not moving 
		if(mController.defenseEnabled()) {
			// Sending each pod their respective commands
			mNeoPods.get(0).setPod(0.0,-1.0*Math.PI/4.0);
			mNeoPods.get(1).setPod(0.0, 1.0*Math.PI/4.0);
			mNeoPods.get(2).setPod(0.0, 3.0*Math.PI/4.0);
			mNeoPods.get(3).setPod(0.0, -3.0* Math.PI/4.0);
		} else { 			//Sending each pod their respective commands
			for(int idx = 0; idx < mNeoPods.size(); idx++) {
				mNeoPods.get(idx).setPod(podDrive[idx],podGear[idx]); 
			}
		}
	}

	public void cAutonVision(boolean state){ 
		cAutonVision = state; 
	}

	public void setForwardCommand(double wantedForwardCommand){
		forwardCommand = wantedForwardCommand; 
	}

	public void setStrafeCommand(double wantedStrafeCommand){
		strafeCommand = wantedStrafeCommand; 
	}

	public void setSpinCommand(double wantedSpinCommand){
		spinCommand = wantedSpinCommand; 
	}

	private void setCoordType(coordType mCoordType){
		this.mCoordType = mCoordType; 
	}

	private void setInputType(inputType mInputType){
		this.mInputType = mInputType; 
	}
	
	public void setWantedState(state wanted) {
		mWantedState = wanted;
	}
	
	/**
	 * Checks to ensure drivetrain is in the correct state
	 */
	private void checkState() {
		if(mWantedState!=mCurrentState) {
			mCurrentState = mWantedState;
		}
	}
	
	public PowerDistributionPanel getPDP() {return mPDP;}
	
	public neopod getPod(int idx) {return mNeoPods.get(idx);}
	
	public double getAvgWheelSpeed() {
		double average =0;
		for(neopod pod: mNeoPods) {
			average += pod.getWheelSpeed();
		}
		return average/mNeoPods.size();
	}
	
	public double getAngle() {return ((mGyro.getAngle()* Math.PI/180.0) % (2*Math.PI));} //Converts mGyro Angle (0-360) to Radians (0-2pi)
	
	private void updateAngle(){
		//-pi to pi 0 = straight ahead
		currentAngle = ((((mGyro.getAngle()+90)* Math.PI/180.0)) % (2*Math.PI));
	}

	/**
	 * Adjust radius to pivot around different positions
	 */
	private double getRadius(String component){
		if(mController.frontRightRotation() || mController.frontLeftRotation()){
			if(component.equals("A")) {return(kLength);}
			if(component.equals("B")) {return(0.0);}
			if(mController.frontRightRotation()){
				if(component.equals("C")) {return(0.0);}
				if(component.equals("D")) {return(kWidth);}
			} else {
				if(component.equals("C")) {return(kWidth);}
				if(component.equals("D")) {return(0.0);}
			}
		} else {
			if(component.equals("A") || component.equals("B")) {return(kLength/2.0);}
			else {return(kWidth/2.0);}
		}
		return 0; 
	}

	/**
	 * Uses PID to home in on vision targets
	 */
	private void trackToTarget(){
		double wantedGyroPosition = mController.gyroClockPosition();

		if(wantedGyroPosition != -1){
			lastGyroClock = wantedGyroPosition; 
		} else {
			wantedGyroPosition = lastGyroClock; 
		}
		spinCommand = mVisionTurn.returnOutput(getAngle(), wantedGyroPosition);

		if(Math.abs(spinCommand) <= 0.06){
			if(mVision.getDistance() != -1){
				forwardCommand = -mVisionForward.returnOutput(mVision.getDistance(), 18);
			} else {
				forwardCommand = 0;
			}
			if(mVision.getAngle() != -1){
				strafeCommand = mVisionStrafe.returnOutput(mVision.getAngle(), 5);
			} else {
				strafeCommand = 0;
			}
		}
	}

	/**
	 * Determines when robot has reached the position it was tracking to
	 */
	public boolean isAtTarget(){
		if(Math.abs((mVisionTurn.returnOutput(mVision.getDistance(), 0))) < .5){
			return true;
		} else {
			return false;
		}
	}
	
	public void resetGyro() {mGyro.reset();}
	
	@Override public void zeroAllSensors() {
		for(int idx = 0; idx < 4; idx++)
		{
			mNeoPods.get(idx).zeroAllSensors();
		}
	}
	
	@Override
	public void registerLoop()
	{
		mLoopMan.addLoop(new loop() {
		@Override
		public void onStart() {
			mCurrentState = state.NEUTRAL;
			mWantedState = state.NEUTRAL;		
		}
		@Override
		public void onLoop() {
			if(mController.gyroReset()) {
				resetGyro();
			}
			updateAngle();
			switch(mCurrentState) {
				case NEUTRAL:
					checkState();
					break;
				case DRIVE:
					forwardCommand = mController.getForward();
					strafeCommand = mController.getStrafe();
					spinCommand = mController.getSpin();

					if (!mController.sickoMode()) {
						forwardCommand *= constants.MAX_SLOW_PERCENT_SPEED;
						strafeCommand *= constants.MAX_SLOW_PERCENT_SPEED;
						spinCommand *= constants.MAX_SLOW_PERCENT_SPEED;
					}

					if(mController.robotCentric()) {
						setCoordType(coordType.ROBOTCENTRIC);
					} else if (mController.backRobotCentric()){
						setCoordType(coordType.BACKROBOTCENTRIC);
					} else {
						setCoordType(coordType.FIELDCENTRIC);
					}
					setInputType(inputType.PERCENTPOWER);

					if(spinCommand == 0){
						//spinCommand = -mSpinMaster.returnOutput(getAngle(), lastAngle);
					} else {
						lastAngle = getAngle();
					}
					
					crabDrive();
					checkState();
					break;
				case VISION:
					trackToTarget();
					setCoordType(coordType.ROBOTCENTRIC); 
					setInputType(inputType.PERCENTPOWER);

					crabDrive();
					checkState();
					break;
				case AUTON:
					System.out.println(isAtTarget());
					if(cAutonVision){
						trackToTarget(); 
						setCoordType(coordType.ROBOTCENTRIC); 
						setInputType(inputType.PERCENTPOWER);
					} else {
						setCoordType(coordType.FIELDCENTRIC); 
						setInputType(inputType.VELOCITY);
					}
					crabDrive();
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
	public void outputToSmartDashboard() {
		//SmartDashboard.putNumber("Vision Gyro", mController.gyroClockPosition()); 
	}
}