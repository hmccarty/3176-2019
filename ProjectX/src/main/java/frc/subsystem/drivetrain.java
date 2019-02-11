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
	
	public TalonSRX[] mDriveTalons = {new TalonSRX(1), new TalonSRX(2), new TalonSRX(3), new TalonSRX(4)}; 
	public TalonSRX[] mGearTalons = {new TalonSRX(11), new TalonSRX(22), new TalonSRX(33), new TalonSRX(44)};
	
	private double kLength;
	private double kWidth;
	private double kRadius; 
	
	private double kMaxSpeed;
	private double kMaxRotation;
	
	private double cMaxSpeed;
	private double cAngle;
	
	private double cForwardCommand;
	private double cStrafeCommand;
	private double cSpinCommand;
	
	public enum systemStates{
		NEUTRAL,
		HOMING,
		DRIVE,
		VISION
	}
	
	public enum coordType{
		ROBOTCENTRIC,
		FIELDCENTRIC
	}
	
	public enum inputType{
		PERCENTPOWER,
		VELOCITY
	}
	
	private systemStates mCurrentState;
	private systemStates mWantedState;
	
	private drivetrain(){
		//instantiate the mPods
		mUpperRight = new swervepod(0, mDriveTalons[0], mGearTalons[0]);
		mUpperLeft = new swervepod(1, mDriveTalons[1], mGearTalons[1]);
		mLowerLeft = new swervepod(2, mDriveTalons[2], mGearTalons[2]);
		mLowerRight = new swervepod(3, mDriveTalons[3], mGearTalons[3]);

		//set drive type
		mCoordType = coordType.FIELDCENTRIC;
		mInputType = inputType.PERCENTPOWER;
		
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
		kRadius = Math.sqrt(Math.pow(kLength,2)+Math.pow(kWidth,2));
		kMaxSpeed = constants.DRIVETRAINMAXWHEELSPEED;
		kMaxRotation = constants.DRIVETRAINMAXROTATIONSPEED;
		
		//Instantiating the mGyro
		mGyro = new AHRS(SPI.Port.kMXP);
		resetGyro();
		updateAngle();
		
		//Initializing the commands
		cForwardCommand = Math.pow(10, -15); //Puts wheels in forward-facing direction
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
	 * Handles each swerve command and communicates with the swervemPods
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
		double a = cStrafeCommand + cSpinCommand * kLength/2; 
		double b = cStrafeCommand - cSpinCommand * kLength/2; 
		double c = cForwardCommand - cSpinCommand * kWidth/2; 
		double d = cForwardCommand + cSpinCommand * kWidth/2; 
		
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
		}
		else {
			//Sending each pod their respective commands
			for(int idx = 0; idx < mPods.size(); idx++) {
				mPods.get(idx).setPod(podDrive[idx],podGear[idx]); 
			}
		}
	}

	private void setCoordType(coordType mCoordType){
		this.mCoordType = mCoordType; 
	}

	private void setInputType(inputType mInputType){
		this.mInputType = mInputType; 
	}
	
	public void setWantedState(systemStates wanted) {
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
	
	private void resetGyro() {mGyro.reset();}
	
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
			mCurrentState = systemStates.NEUTRAL;
			mWantedState = systemStates.NEUTRAL;		
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

					if (!mController.Boosted()){
						cForwardCommand *= constants.MAXSLOWPERCENTSPEED;
						cStrafeCommand *= constants.MAXSLOWPERCENTSPEED;
						cSpinCommand *= constants.MAXSLOWPERCENTSPEED;
					}

					if(mController.RobotCentric()){
						setCoordType(coordType.ROBOTCENTRIC);
					} else {
						setCoordType(coordType.FIELDCENTRIC);
					}
					setInputType(inputType.PERCENTPOWER);

					crabDrive();
					checkState();
					break;
				case VISION:
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