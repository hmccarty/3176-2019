package frc.subsystem; 

import com.kauailabs.navx.frc.AHRS; 
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import java.util.ArrayList;
import frc.robot.constants;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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
	
	private ArrayList<swervepod> mPods;
	
	private swervepod mUpperRight;
	private swervepod mUpperLeft;
	private swervepod mLowerLeft;
	private swervepod mLowerRight;
	
	private coordType mCoordType;
	private inputType mInputType;

	private boolean autonVision; 

	private pid visionForward;
	private pid visionTurn;  
	private pid visionStrafe;  
	
	public TalonSRX[] mDriveTalons = {new TalonSRX(constants.DRIVE_ONE), new TalonSRX(constants.DRIVE_TWO), new TalonSRX(constants.DRIVE_THREE), new TalonSRX(constants.DRIVE_FOUR)}; 
	public TalonSRX[] mGearTalons = {new TalonSRX(constants.STEER_ONE), new TalonSRX(constants.STEER_TWO), new TalonSRX(constants.STEER_THREE), new TalonSRX(constants.STEER_FOUR)};
	
	private double kLength;
	private double kWidth;
	
	private double kMaxSpeed;
	private double kMaxRotation;
	
	private double cMaxSpeed;
	private double cAngle;
	
	private double cForwardCommand;
	private double cStrafeCommand;
	private double cSpinCommand;
	
	public enum state{
		NEUTRAL,
		HOMING,
		DRIVE,
		VISION,
		AUTON
	}
	
	public enum coordType{
		ROBOTCENTRIC,
		FIELDCENTRIC
	}
	
	public enum inputType{
		PERCENTPOWER,
		VELOCITY
	}
	
	private state mCurrentState;
	private state mWantedState;
	
	private drivetrain(){
		//instantiate the mPods
		mUpperRight = new swervepod(0, mDriveTalons[0], mGearTalons[0]);
		mUpperLeft = new swervepod(1, mDriveTalons[1], mGearTalons[1]);
		mLowerLeft = new swervepod(2, mDriveTalons[2], mGearTalons[2]);
		mLowerRight = new swervepod(3, mDriveTalons[3], mGearTalons[3]);

		//set drive type
		mCoordType = coordType.FIELDCENTRIC;
		mInputType = inputType.PERCENTPOWER;

		autonVision = false;

		visionForward = new pid(0.007, 0, 0, .8); 
		visionTurn = new pid(0.02, 0, 0, .8); 
		visionStrafe = new pid(0.1, 0, 0, .8); 
		
		//Instantiate array list
		mPods = new ArrayList<swervepod>();
				
		//Add instantiated mPods to the array list
		mPods.add(mUpperRight);
		mPods.add(mUpperLeft);
		mPods.add(mLowerLeft);
		mPods.add(mLowerRight);
		
		//Setting constants
		kLength = constants.DRIVETRAINLENGTH;
		kWidth = constants.DRIVETRAINWIDTH;

		kMaxSpeed = constants.DRIVETRAINMAXWHEELSPEED;
		kMaxRotation = constants.DRIVETRAINMAXROTATIONSPEED;
		
		//Instantiating the Gyro
		mGyro = new AHRS(SPI.Port.kMXP);
		resetGyro();
		updateAngle();
		
		//Start wheels in a forward facing direction
		cForwardCommand = Math.pow(10, -15); 
		cStrafeCommand = 0.0;
		cSpinCommand = 0.0;
	}
	
	/**
	 * Prevents more than one instance of Drivetrain
	 */
	public static drivetrain getInstance(){
		return instance;
	}
	
	/**
	 * Handles each swerve command and communicates with the Swerve Pods
	 */
	private void crabDrive() {
		if(mCoordType == coordType.FIELDCENTRIC){
			final double temp = cForwardCommand * Math.sin(cAngle) + cStrafeCommand * Math.cos(cAngle);
		    cStrafeCommand = (-cForwardCommand * Math.cos(cAngle) + cStrafeCommand * Math.sin(cAngle));
		    cForwardCommand = temp;
		}

		if(mInputType == inputType.PERCENTPOWER){
			cForwardCommand *= kMaxSpeed;
			cStrafeCommand *= kMaxSpeed;
			cSpinCommand *= kMaxRotation;
		}

		//Create arrays with the speed and angle of each pod
		double[] podDrive = new double[4];
		double[] podGear = new double[4];
		
		//Calculating components
		double a = cStrafeCommand + cSpinCommand * getRadius("A"); 
		double b = cStrafeCommand - cSpinCommand * getRadius("B"); 
		double c = cForwardCommand - cSpinCommand * getRadius("C"); 
		double d = cForwardCommand + cSpinCommand * getRadius("D"); 
		
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
		cMaxSpeed = Math.max(Math.max(podDrive[0],podDrive[1]),Math.max(podDrive[2], podDrive[3]));
		
		//Reducing mPods by the relative max speed
		if(cMaxSpeed > kMaxSpeed) {
			for(int idx = 0; idx < mPods.size(); idx++) {
				podDrive[idx] /= cMaxSpeed/kMaxSpeed;
			}
		}
		
		//If enabled, sends each pod to a defensive lock when not moving 
		if(mController.defenseEnabled()) {
			// Sending each pod their respective commands
			mPods.get(0).setPod(0.0,-1.0*Math.PI/4.0);
			mPods.get(1).setPod(0.0, 1.0*Math.PI/4.0);
			mPods.get(2).setPod(0.0, 3.0*Math.PI/4.0);
			mPods.get(3).setPod(0.0, -3.0* Math.PI/4.0);
		} else { 			//Sending each pod their respective commands
			for(int idx = 0; idx < mPods.size(); idx++) {
				mPods.get(idx).setPod(podDrive[idx],podGear[idx]); 
			}
		}
	}

	public void autonVision(boolean state){ 
		autonVision = state; 
	}

	public void setForwardCommand(double wantedForwardCommand){
		cForwardCommand = wantedForwardCommand; 
	}

	public void setStrafeCommand(double wantedStrafeCommand){
		cStrafeCommand = wantedStrafeCommand; 
	}

	public void setSpinCommand(double wantedSpinCommand){
		cSpinCommand = wantedSpinCommand; 
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
	
	private void checkState() {
		if(mWantedState!=mCurrentState) {
			mCurrentState = mWantedState;
		}
	}
	
	public PowerDistributionPanel getmPDP() {return mPDP;}
	
	public swervepod getPod(int idx) {return mPods.get(idx);}
	
	public double getAvgWheelSpeed() {
		double average =0;
		for(swervepod pod: mPods) {
			average += pod.getWheelSpeed();
		}
		return average/mPods.size();
	}
	
	public double getAngle() {return ((mGyro.getAngle()* Math.PI/180.0) % (2*Math.PI));} //Converts mGyro Angle (0-360) to Radians (0-2pi)
	
	private void updateAngle(){
		//-pi to pi 0 = straight ahead
		cAngle = ((((mGyro.getAngle()+90)* Math.PI/180.0)) % (2*Math.PI));
	}

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

	public boolean isAtTarget(){
		if(Math.abs((visionTurn.returnOutput(mVision.getDistance(), 0))) < .5){
			return true;
		} else {
			return false;
		}
	}
	
	public void resetGyro() {mGyro.reset();}
	
	@Override public void zeroAllSensors() {
		for(int idx = 0; idx < 4; idx++)
		{
			mPods.get(idx).zeroAllSensors();
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
			if(mController.getGyroReset()) {
				resetGyro();
			}
			updateAngle();
			switch(mCurrentState) {
				case NEUTRAL:
					checkState();
					break;
				case DRIVE:
					cForwardCommand = mController.getForward();
					cStrafeCommand = mController.getStrafe();
					cSpinCommand = mController.getSpin();

					if (!mController.boost()){
						cForwardCommand *= constants.MAXSLOWPERCENTSPEED;
						cStrafeCommand *= constants.MAXSLOWPERCENTSPEED;
						cSpinCommand *= constants.MAXSLOWPERCENTSPEED;
					}

					if(mController.robotCentric()){
						setCoordType(coordType.ROBOTCENTRIC);
					} else {
						setCoordType(coordType.FIELDCENTRIC);
					}
					setInputType(inputType.PERCENTPOWER);

					crabDrive();
					checkState();
					break;
				case VISION:
					//System.out.println(Robot.getDistance());
					double distance = mVision.getDistance();
					if(distance != -1){
						cForwardCommand = visionForward.returnOutput(distance, 15);
					} else {
						cForwardCommand = 0;
					}
					System.out.println(mVision.getAngle());
					//cSpinCommand = visionTurn.returnOutput(Robot.getAngle(), 0);
					if(mVision.getAngle() != -1){
						cStrafeCommand = -visionStrafe.returnOutput(mVision.getAngle(), 130);
					} else {
						cStrafeCommand = 0;
					}
					setCoordType(coordType.ROBOTCENTRIC); 
					setInputType(inputType.PERCENTPOWER);
					crabDrive();
					checkState();
					break;
				case AUTON:
					System.out.println(isAtTarget());
					if(autonVision){
						if(mVision.getDistance() != -1){
							cForwardCommand = visionForward.returnOutput(mVision.getDistance(), 15);
						}
						//cSpinCommand = visionTurn.returnOutput(Robot.getAngle(), 0);
						if(mVision.getAngle() != -1){
							cStrafeCommand = visionStrafe.returnOutput(mVision.getAngle(), 130);
							System.out.println(mVision.getAngle());
						}
						setCoordType(coordType.ROBOTCENTRIC); 
						setInputType(inputType.PERCENTPOWER);
					} else {
						setCoordType(coordType.FIELDCENTRIC); 
						setInputType(inputType.VELOCITY);
					}
					crabDrive();
					checkState();
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