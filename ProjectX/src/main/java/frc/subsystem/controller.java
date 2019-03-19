package frc.subsystem; 

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.constants;

public class controller {
    private static controller instance = new controller();
    private Joystick mThrustStick = new Joystick(0);
    private Joystick mYawStick = new Joystick(1);
    private Joystick mButtonMonkey = new Joystick(2); 

    private boolean mOpenLoop = false; 
    private Timer mOpenLoopTimer = new Timer();
    private boolean  mFirstTime = true;

    private double kThrustStickDeadband = constants.TRANSLATIONAL_DEADBAND; 
    private double kThrustStickScale = constants.TRANSLATIONAL_SCALE; 
    private double kThrustStickOffset = constants.TRANSLATIONAL_SCALE; 

    public controller() {
        mOpenLoopTimer.start();
    }
    
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

    /*****************\
	|* Button Monkey *|
    \*****************/

    public boolean neutral() {
        return mButtonMonkey.getRawButton(1);
    }

    public  boolean crossbowIntake() {
        return mButtonMonkey.getRawButton(2);
    }

    public  boolean crossbowHold() {
        return mButtonMonkey.getRawButton(4);
    }

    public  boolean crossbowDeliver() {
        return mButtonMonkey.getRawButton(3);
    }

    public boolean deployCargoIntake() {
        return mButtonMonkey.getRawButton(5);
    }

    public boolean spitCargoIntake() {
        return mButtonMonkey.getRawButton(6);
    }

    public boolean rocketCargoIntake() {
        return mButtonMonkey.getRawButton(10);
    }    

    public boolean stowCargoIntake() {
        return mButtonMonkey.getRawButton(9);
    }

    public void alertOperator() { 
        mButtonMonkey.setRumble(RumbleType.kRightRumble, 1);
    }

    public double openLoopElevator() {
        if(Math.abs(mThrustStick.getY()) > 0.01) {
            return (mThrustStick.getY()*.7);
        } else {
            return 0;
        }
    }

    public boolean elevatorFailSafeMode() {
        if(mButtonMonkey.getRawButton(11) && mButtonMonkey.getRawButton(12)) {
            if(mFirstTime == true) {
                 mFirstTime = false; 
                mOpenLoopTimer.reset();
            }
            if(mOpenLoopTimer.get() > 2.0) {
                 mFirstTime = true;
                mOpenLoop = !mOpenLoop;
            }
        }
        return mOpenLoop;
    }

    /**
     * @return driver set position of cargo intake in encoder units
     *         (if no position is wanted, then returns -1)
     */
    public int wantedCargoIntakePosition() {
        if(Math.abs(mButtonMonkey.getY()) > 0.07) {
            return (int)(mButtonMonkey.getY()*800);
        } else {
            return -1; 
        }
    }

    /**
     * @return driver wanted velocity of elevator
     */
    public double wantedElevatorVelocity() {
        if(Math.abs(mButtonMonkey.getY()) > 0.07) {
            return mButtonMonkey.getY();
        } else {
            return 0; 
        }
    }

    /** 
     * @return driver wanted elevator height in inches
     */
    public double wantedElevatorHeight() {
        if(mButtonMonkey.getPOV() == 0) {
            return 74.3; 
        }
        else if(mButtonMonkey.getPOV() == 2) {
            return 46.3; 
        }
        else if(mButtonMonkey.getPOV() == 4) {
            return 0.0; 
        } else {
            return -1; 
        }
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
     * @return if driver wants to control in reverse robot centric
     */
    public boolean backRobotCentric() {
        return mYawStick.getRawButton(3);
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

    public boolean transferCargo() {
        return mThrustStick.getRawButton(7);
    }
    
    public boolean deployCargo(){
        return mThrustStick.getRawButton(8); 
    }

    public boolean stowCargo(){
        return mThrustStick.getRawButton(9); 
    }

    public boolean release(){
        return mThrustStick.getRawButton(10); 
    }

    public boolean clamp(){
        return mThrustStick.getRawButton(11); 
    }

    /**
     * @return wanted motion in the y direction on an exponential scale
     */
    public double getForward() {
        return expoScale(mThrustStick.getY(), kThrustStickDeadband, kThrustStickOffset, kThrustStickScale);
    }

    /**
     * @return wanted motion in the x direction
     */
    public double getStrafe() {
        return expoScale(mThrustStick.getX(), kThrustStickDeadband, kThrustStickOffset, kThrustStickScale);
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
     * @return if driver wants to switch streaming camera
     */
    public boolean switchVisionCamera() {
        return mYawStick.getRawButtonPressed(4);
    }

    /**
     * @return if driver wants to switch streaming mode (either dark and tracking or autoexposure)
     */
    public boolean switchVisionMode() {
        return mYawStick.getRawButtonPressed(6);
    }

    /**
     * @return wanted direction when tracking
     */
    public double gyroClockPosition() {
        int position = mYawStick.getPOV(0);
        if(position < 22 && position < 338) {
            return 0; 
        }
        else if(position > 22 && position < 67) {
            return Math.PI / 4;
        }
        else if(position > 67 && position < 112) {
            return Math.PI / 2;
        }
        else if(position > 112 && position < 157) {
            return 3 * Math.PI / 4;
        }
        else if(position > 157 && position < 202) {
            return Math.PI;
        }
        else if(position > 202 && position < 247) {
            return 5 * Math.PI / 4;
        }
        else if(position > 247 && position < 292) {
            return 3 * Math.PI / 2;
        }
        else if(position > 247 && position < 292) {
            return 7 * Math.PI / 4;
        } else {
            return 0; 
        }
    }

    /**
     * @return wanted motion in the omega axis
     */
    public double getSpin() {
        if(Math.abs(mYawStick.getX()) > 0.04) {
            return mYawStick.getX() * 0.14;
        } else {
            return 0; 
        }
    }
}