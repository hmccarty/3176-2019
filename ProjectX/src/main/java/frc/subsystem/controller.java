package frc.subsystem; 

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.constants;

public class controller {
    private static controller instance = new controller();
    private Joystick mThrustStick = new Joystick(0);
    private Joystick mYawStick = new Joystick(1);
    private Joystick mButtonMonkeyMain = new Joystick(3); 
    private Joystick mButtonMonkeyBackup = new Joystick(4); 

    private double kThrustStickDeadband = constants.TRANSLATIONAL_DEADBAND; 
    private double kThrustStickScale = constants.TRANSLATIONAL_SCALE; 
    private double kThrustStickOffset = constants.TRANSLATIONAL_SCALE; 

    private double kYawStickDeadband = constants.ROTATIONAL_DEADBAND; 
    private double kYawStickScale = constants.ROTATIONAL_SCALE; 
    private double kYawStickOffset = constants.ROTATIONAL_SCALE; 


    private Boolean elevatorOpenLoop = false;

    private Boolean compressorSwitch = true;
    
    public static controller getInstance() {
        return instance; 
    }

    /**
     * For a visual reference, visit this link: https://www.desmos.com/calculator/0guzfqsu2m
     * @param input the raw input from the axis
     * @param deadband magnitude required to send a value
     * @param offset value determined by exponential scale
     * @param scale amount to scale the joysick by
     * @return the input value on an exponential scale
     */
    public double expoScale(double input, double deadband, double offset, double scale) {
        if(Math.abs(input) > deadband) {
            input = (input > 0 ? input - kThrustStickDeadband : input + kThrustStickDeadband); 
            return -(scale * Math.pow(input, 3) + (1 - scale) * input) * offset;
        } else {
            return 0; 
        }
    }
    
    /*********************\
	|* Button Monkey Main*|
    \**************s*******/

    public  boolean crossbowDeliverMain() {
        return mButtonMonkeyMain.getRawButton(3);
    }

    public boolean neutralMain() {
        return mButtonMonkeyMain.getRawButton(1);
    }

    public  boolean crossbowIntakeMain() {
        return mButtonMonkeyMain.getRawButton(2);
    }

    public  boolean crossbowHoldMain() {
        return mButtonMonkeyMain.getRawButton(4);
    }

    public boolean deployCargoIntakeMain() {
        return mButtonMonkeyMain.getRawButton(5);
    }

    public boolean spitCargoIntakeMain() {
        return mButtonMonkeyMain.getRawButton(6);
    }

    public boolean intakeClawMain() {
        return mButtonMonkeyMain.getRawButton(7);
    }

    public boolean deployClawMain() {
        return mButtonMonkeyMain.getRawButton(8);
    }

    public boolean stowCargoIntakeMain() {
        return mButtonMonkeyBackup.getRawButton(9);
    }

    public boolean rocketCargoIntakeMain() {
        return mButtonMonkeyMain.getRawButton(10);
    }    

    public void alertOperatorMain() { 
        mButtonMonkeyMain.setRumble(RumbleType.kRightRumble, 1);
    }

    public void stopOperatorAlert() {
        mButtonMonkeyMain.setRumble(RumbleType.kRightRumble, 0);
    }

    /**
     * @return driver set position of cargo intake in encoder units
     *         (if no position is wanted, then returns -1)
     */
    public int wantedCargoIntakePositionMain() {
        if(Math.abs(mButtonMonkeyMain.getRawAxis(5)) > 0.015) {
            return (int)(mButtonMonkeyMain.getRawAxis(5)*600);
        } else {
            return -1; 
        }
    }

    /**
     * @return driver wanted velocity of elevator
     */
    public double wantedElevatorVelocityMain() {
        if(Math.abs(mButtonMonkeyMain.getRawAxis(1)) > 0.045) {
             return -mButtonMonkeyMain.getRawAxis(1)*450;
        } else {
            return 0; 
        }
    }

    public boolean clawNeutral() {
        if(Math.abs(mButtonMonkeyMain.getRawAxis(3)) > 0.1) {
             return true;
        } else {
            return false; 
        }
    }

    /** 
     * @return driver wanted elevator height in inches
     */
    public double wantedElevatorHeightMain() {
        if(mButtonMonkeyMain.getPOV() == 0) {
            return 27.75; 
        }
        else if(mButtonMonkeyMain.getPOV() == 90) {
            return 15.25; 
        }
        else if(mButtonMonkeyMain.getPOV() == 180) {
            return 0; 
        } else if(mButtonMonkeyMain.getPOV() == 270) {
            return 8.0;
        } else {
            return -1; 
        }
    }

    /************************\
	|* Button Monkey Back Up*|
    \************************/

    public boolean neutralBackup() {
        return mButtonMonkeyBackup.getRawButton(1);
    }

    public  boolean crossbowIntakeBackup() {
        return mButtonMonkeyBackup.getRawButton(2);
    }

    public  boolean crossbowDeliverBackup() {
        return mButtonMonkeyBackup.getRawButton(3);
    }

    public  boolean crossbowHoldBackup() {
        return mButtonMonkeyBackup.getRawButton(4);
    }

    public boolean deployCargoIntakeBackup() {
        return mButtonMonkeyBackup.getRawButton(5);
    }

    public boolean spitCargoIntakeBackup() {
        return mButtonMonkeyBackup.getRawButton(6);
    }

    public boolean intakeClawBackup() {
        return mButtonMonkeyBackup.getRawButton(7);
    }

    public boolean deployClawBackup() {
        return mButtonMonkeyBackup.getRawButton(8);
    }

    public boolean stowCargoIntakeBackup() {
        return false;//mButtonMonkeyBackup.getRawButton(9);
    }

    public boolean rocketCargoIntakeBackup() {
        return mButtonMonkeyBackup.getRawButton(10);
    }    

    /**
     * @return driver set position of cargo intake in encoder units
     *         (if no position is wanted, then returns -1)
     */
    public double wantedCargoIntakeOpenLoop() {
        SmartDashboard.putNumber("Open Loop Value", mButtonMonkeyBackup.getRawAxis(5));
        if(Math.abs(mButtonMonkeyBackup.getRawAxis(5)) > 0.015) {
            return mButtonMonkeyBackup.getRawAxis(5)*.6;
        } else {
            return 0; 
        }
    }

    /**
     * @return driver wanted velocity of elevator
     */
    public double wantedElevatorOpenLoop() {
        if(Math.abs(mButtonMonkeyBackup.getRawAxis(1)) > 0.045) {
             return -mButtonMonkeyBackup.getRawAxis(1);
        } else {
            return 0; 
        }
    }

    /** 
     * @return driver wanted elevator height in inches
     */
    public double wantedElevatorHeightBackup() {
        if(mButtonMonkeyBackup.getPOV() == 0) {
            return 25; 
        }
        else if(mButtonMonkeyBackup.getPOV() == 90) {
            return 18; 
        }
        else if(mButtonMonkeyBackup.getPOV() == 180) {
            return 0.2; 
        } else if(mButtonMonkeyBackup.getPOV() == 270){
            return 10.0;
        } 
        else {
            return -1; 
        }
    }

    /*************************\
	|* Button Monkey Handler *|
    \*************************/

    public boolean neutral() {
        return neutralMain() || neutralBackup();
    }

    public  boolean crossbowIntake() {
        return crossbowIntakeMain() || crossbowIntakeBackup();
    }

    public  boolean crossbowDeliver() {
        return crossbowDeliverMain() || crossbowDeliverBackup();
    }

    public  boolean crossbowHold() {
        return crossbowHoldMain() || crossbowHoldBackup();
    }

    public boolean deployCargoIntake() {
        return deployCargoIntakeMain() || deployCargoIntakeBackup(); 
    }

    public boolean spitCargoIntake() {
        return spitCargoIntakeMain() || spitCargoIntakeBackup(); 
    }

    public boolean intakeClaw() {
        return intakeClawMain() || intakeClawBackup(); 
    }

    public boolean deployClaw() {
        return deployClawMain() || deployClawBackup(); 
    }

    public boolean stowCargoIntake() {
        return false;//mButtonMonkeyBackup.getRawButton(9);
    }

    public boolean rocketCargoIntake() {
        return rocketCargoIntakeMain() || rocketCargoIntakeBackup(); 
    }    

    /**
     * @return driver set position of cargo intake in encoder units
     *         (if no position is wanted, then returns -1)
     */
    public int wantedCargoIntakePosition() {
        if(wantedCargoIntakePositionMain() != -1) {
            return wantedCargoIntakePositionMain();
        } else {
            return -1; 
        }
    }

    /**
     * @return driver wanted velocity of elevator
     */
    public double wantedElevatorVelocity() {
        SmartDashboard.putNumber("Jostick Velocity", wantedElevatorVelocityMain());
        if(wantedElevatorVelocityMain() != 0){
            return wantedElevatorVelocityMain();
        } else {
            return 0;
        }
    }

    /** 
     * @return driver wanted elevator height in inches
     */
    public double wantedElevatorHeight() {
        SmartDashboard.putNumber("Wanted Elevator Joystick", wantedElevatorHeightMain() );
        if(wantedElevatorHeightMain() != -1){
            return wantedElevatorHeightMain();
        } 
        //} else if(wantedElevatorHeightBackup() != -1){
        //    return wantedElevatorHeightBackup(); 
         else {
            return -1; 
        }
    }

    public boolean transferCargo() {
         return false;// mButtonMonkeyMain.getRawButton(9);
    }

    /****************\
	|* Thrust Stick *|
    \****************/

    /**
     * @return if driver wants more speed
     */
    public boolean sickoMode() {
        return mThrustStick.getRawButton(1);
    }

    /**
     * @return if driver wants to reset gyro
     */
    public boolean gyroReset() {
        return mThrustStick.getRawButton(2);
    }

     /**
     * @return if driver wants to control in robot centric
     */
    public boolean robotCentric() {
        return mThrustStick.getRawButton(3);
    }

     /**
     * @return if driver wants to enable a locking position
     */
    public boolean defenseEnabled() {
        return mThrustStick.getRawButton(4);
    }

     /**
     * @return if driver wants to pivot around front left pod
     */
    public boolean frontLeftRotation() {
        return mThrustStick.getRawButton(5);
    }

    /**
     * @return if driver wants to pivot around front right pod
     */
    public boolean frontRightRotation() {
        return mThrustStick.getRawButton(6);
    }

    public boolean toggleCompressor() {
        return mThrustStick.getRawButtonPressed(7);
    }
   
    /**
     * @return wanted motion in the y direction on an exponential scale
     */
    public double getForward() {
        return -expoScale(mThrustStick.getY(), kThrustStickDeadband, kThrustStickOffset, kThrustStickScale);
    }

    /**
     * @return wanted motion in the x direction
     */
    public double getStrafe() {
        return -expoScale(mThrustStick.getX(), kThrustStickDeadband, kThrustStickOffset, kThrustStickScale);
    }

    public boolean runCompressor() {
        if (mThrustStick.getRawButton(7)){
            compressorSwitch = !compressorSwitch;
        }
        return compressorSwitch;
    }

    /*************\
	|* Yaw Stick *|
    \*************/

    /**
     * @return if driver wants to enable tracking
     */
    public boolean trackTarget() {
        return mYawStick.getRawButton(1);
    }

    /**
     * @return if driver wants to enable tracking
     */
    public boolean finishedTracking() {
        return mYawStick.getRawButtonReleased(1);
    }

    /**
     * @return if driver wants to control in reverse robot centric
     */
    public boolean backRobotCentric() {
        return false;//mYawStick.getRawButton(3);
    }

    /**
     * @return if driver wants to switch streaming camera
     */
    public boolean switchVisionCamera() {
        return false;//mYawStick.getRawButtonPressed(4);
    }

    /**
     * @return if driver wants to switch streaming mode (either dark and tracking or autoexposure)
     */
    public boolean switchVisionMode() {
        return false;//mYawStick.getRawButtonPressed(6);
    }

    /**
     * @return wanted direction when tracking
     */
    public double gyroClockPosition() {
        if(mYawStick.getRawButton(6)) {
            return 29 * Math.PI / 180;
        }
        else if(mYawStick.getRawButton(4)) {
            return 3 * Math.PI / 4;
        }
        else if(mYawStick.getRawButton(3)) {
            return 5 * Math.PI / 4;
        }
        else if(mYawStick.getRawButton(5)) {
            return 5.72;
        } 
        else if(mYawStick.getRawButton(11)) {
            return Math.PI;
        } else {
            return -1;
        }
    }

    /**
     * @return wanted motion in the omega axis
     */
    public double getSpin() {
        return 0.2*expoScale(mYawStick.getX(), kYawStickDeadband, kYawStickOffset, kYawStickScale);
    }
}