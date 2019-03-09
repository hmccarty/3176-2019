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


    public controller(){
        mOpenLoopTimer.start();
    }
    
    public static controller getInstance(){
        return instance; 
    }

    /*****************\
	|* Button Monkey *|
    \*****************/

    public boolean neutral(){
        return mButtonMonkey.getRawButton(1);
    }

    public  boolean crossbowIntake(){
        return mButtonMonkey.getRawButton(2);
    }

    public  boolean crossbowHold(){
        return mButtonMonkey.getRawButton(3);
    }

    public  boolean crossbowDeliver(){
        return mButtonMonkey.getRawButton(4);
    }

    public boolean deployCargoIntake(){
        return mButtonMonkey.getRawButton(5);
    }

    public boolean spitCargoIntake(){
        return mButtonMonkey.getRawButton(6);
    }

    public boolean cargoIntakeToRocketHeight(){
        return mButtonMonkey.getRawButton(10);
    }    

    public boolean stowCargoIntake(){
        return mButtonMonkey.getRawButton(9);
    }

    public void alertOperator(){ 
        mButtonMonkey.setRumble(RumbleType.kRightRumble, 1);
    }

    public boolean cargoIntakeOpenLoopEnabled(){
        if(mButtonMonkey.getRawButton(11) && mButtonMonkey.getRawButton(12)) {
            if( mFirstTime == true){
                 mFirstTime = false; 
                mOpenLoopTimer.reset();
            }
            if(mOpenLoopTimer.get() > 2.0){
                 mFirstTime = true;
                mOpenLoop = !mOpenLoop;
            }
        }
        return mOpenLoop;
    }

    public double getCargoIntakeOpenLoopCommand() {
        return mButtonMonkey.getY() * .5;
    }

    /**
     * @return driver set position of cargo intake in encoder units
     *         (if no position is wanted, then returns -1)
     */
    public int wantedCargoIntakePosition(){
        if(Math.abs(mButtonMonkey.getY()) > 0.07){
            return (int)(mButtonMonkey.getY()*400);
        } else {
            return -1; 
        }
    }

    /**
     * @return driver wanted velocity of elevator
     */
    public double wantedElevatorVelocity(){
        if(Math.abs(mButtonMonkey.getY()) > 0.07){
            return mButtonMonkey.getY();
        } else {
            return 0; 
        }
    }

    /** 
     * @return driver wanted elevator height in inches
     */
    public double wantedElevatorHeight(){
        if(mButtonMonkey.getPOV() == 0){
            return 74.3; 
        }
        else if(mButtonMonkey.getPOV() == 2){
            return 46.3; 
        }
        else if(mButtonMonkey.getPOV() == 4){
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
    public boolean boost(){
        return mThrustStick.getRawButton(1);
    }

    /**
     * @return if driver wants to reset gyro
     */
    public boolean gyroReset(){
        return mThrustStick.getRawButton(2);
    }

     /**
     * @return if driver wants to control in robot centric
     */
    public boolean robotCentric(){
        return mThrustStick.getRawButton(3);
    }

     /**
     * @return if driver wants to control in reverse robot centric
     */
    public boolean backRobotCentric(){
        return mYawStick.getRawButton(3);
    }

     /**
     * @return if driver wants to enable a locking position
     */
    public boolean defenseEnabled(){
        return mThrustStick.getRawButton(4);
    }

     /**
     * @return if driver wants to pivot around front left pod
     */
    public boolean frontLeftRotation(){
        return mThrustStick.getRawButton(5);
    }

    /**
     * @return if driver wants to pivot around front right pod
     */
    public boolean frontRightRotation(){
        return mThrustStick.getRawButton(6);
    }

    /**
     * @return wanted motion in the y direction
     */
    public double getForward(){
        if(Math.abs(mThrustStick.getY()) > 0.08){
            return mThrustStick.getY();
        } else {
            return 0; 
        }
    }

    /**
     * @return wanted motion in the x direction
     */
    public double getStrafe(){
        if(Math.abs(mThrustStick.getX()) > 0.08){
            return mThrustStick.getX();
        } else {
            return 0; 
        }
    }

    /*************\
	|* Yaw Stick *|
    \*************/

    /**
     * @return if driver wants to enable tracking
     */
    public boolean trackTarget(){
        return mYawStick.getRawButton(1);
    }

    /**
     * @return if driver wants to switch streaming camera
     */
    public boolean switchVisionCamera(){
        return mYawStick.getRawButtonPressed(4);
    }

    /**
     * @return if driver wants to switch streaming mode (either dark and tracking or autoexposure)
     */
    public boolean switchVisionMode(){
        return mYawStick.getRawButtonPressed(6);
    }

    /**
     * @return wanted direction when tracking
     */
    public double gyroClockPosition(){
        int position = mYawStick.getPOV(0);
        if(position < 22 && position < 338){
            return 0; 
        }
        else if(position > 22 && position < 67){
            return Math.PI / 4;
        }
        else if(position > 67 && position < 112){
            return Math.PI / 2;
        }
        else if(position > 112 && position < 157){
            return 3 * Math.PI / 4;
        }
        else if(position > 157 && position < 202){
            return Math.PI;
        }
        else if(position > 202 && position < 247){
            return 5 * Math.PI / 4;
        }
        else if(position > 247 && position < 292){
            return 3 * Math.PI / 2;
        }
        else if(position > 247 && position < 292){
            return 7 * Math.PI / 4;
        } else {
            return 0; 
        }
    }

    /**
     * @return wanted motion in the omega axis
     */
    public double getSpin(){
        if(Math.abs(mYawStick.getX()) > 0.07){
            return -mYawStick.getX() * 0.14;
        } else {
            return 0; 
        }
    }
}