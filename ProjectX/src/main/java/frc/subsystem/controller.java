package frc.subsystem; 

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants;

public class controller {
    private static controller instance = new controller();
    private Joystick thrustStick = new Joystick(0);
    private Joystick yawStick = new Joystick(1);
    private Joystick buttonMonkey = new Joystick(2); 

    private boolean openLoop = false; 
    private Timer openLoopTimer = new Timer();
    private boolean firstTime = true;

    public controller(){
        openLoopTimer.start();
    }
    
    public static controller getInstance(){
        return instance; 
    }

    /*****************\
	|* Button Monkey *|
    \*****************/

    public boolean neutral(){
        return buttonMonkey.getRawButton(1);
    }

    public  boolean crossbowIntake(){
        return buttonMonkey.getRawButton(2);
    }

    public  boolean crossbowHold(){
        return buttonMonkey.getRawButton(3);
    }

    public  boolean crossbowDeliver(){
        return buttonMonkey.getRawButton(4);
    }

    public boolean deployCargoIntake(){
        return buttonMonkey.getRawButton(5);
    }

    public boolean spitCargoIntake(){
        return buttonMonkey.getRawButton(6);
    }

    public boolean cargoIntakeToRocketHeight(){
        return buttonMonkey.getRawButton(10);
    }    

    public boolean stowCargoIntake(){
        return buttonMonkey.getRawButton(9);
    }

    /*public boolean openLoopEnabled(){
        if(buttonMonkey.getRawButton(11) && buttonMonkey.getRawButton(12)) {
            if(firstTime == true){
                firstTime = false; 
                openLoopTimer.reset();
            }
            if(openLoopTimer.get() > 2.0){
                firstTime = true;
                openLoop = !openLoop;
            }
        }
        return openLoop;
    }*/

    public int getWantedCargoIntakePosition(){
        if(cargoIntakeToRocketHeight()){
            return constants.ROCKET_HEIGHT;
        }
        else if(Math.abs(buttonMonkey.getY()) > 0.07){
            return (int)(buttonMonkey.getY()*2500);
        } else {
            return -1; 
        }
    }

    public double getElevatorVelocity(){
        if(Math.abs(buttonMonkey.getY()) > 0.07){
            return buttonMonkey.getY();
        } else {
            return 0; 
        }
    }

    public double getElevatorHeight(){
        if(buttonMonkey.getPOV() == 0){
            return 74.3; 
        }
        else if(buttonMonkey.getPOV() == 2){
            return 46.3; 
        }
        else if(buttonMonkey.getPOV() == 4){
            return 0.0; 
        } else {
            return -1; 
        }
    }

    /****************\
	|* Thrust Stick *|
    \****************/

    public boolean boost(){
        return thrustStick.getRawButton(1);
    }

    public boolean getGyroReset(){
        return thrustStick.getRawButton(2);
    }

    public boolean robotCentric(){
        return thrustStick.getRawButton(3);
    }

    public boolean defenseEnabled(){
        return thrustStick.getRawButton(4);
    }

    public boolean frontLeftRotation(){
        return thrustStick.getRawButton(5);
    }

    public boolean clockOne(){
        return thrustStick.getRawButton(10);
    }

    public boolean clockTwo(){
        return thrustStick.getRawButton(12);
    }

    public boolean clockThree(){
        return thrustStick.getRawButton(9);
    }

    public boolean clockFour(){
        return thrustStick.getRawButton(10);
    }

    public boolean frontRightRotation(){
        return thrustStick.getRawButton(6);
    }

    public double getForward(){
        if(Math.abs(thrustStick.getY()) > 0.08){
            return thrustStick.getY();
        } else {
            return 0; 
        }
    }

    public double getStrafe(){
        if(Math.abs(thrustStick.getX()) > 0.08){
            return thrustStick.getX();
        } else {
            return 0; 
        }
    }

    /*************\
	|* Yaw Stick *|
    \*************/

    public boolean trackTarget(){
        return yawStick.getRawButton(1);
    }

    public boolean visionFront(){
        return yawStick.getRawButton(4);
    }

    public boolean visionBack(){
        return yawStick.getRawButton(6);
    }

    public double gyroClockPosition(){
        int position = yawStick.getPOV(0);
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

    public double getSpin(){
        if(Math.abs(yawStick.getX()) > 0.07){
            return -yawStick.getX() * 0.19;
        } else {
            return 0; 
        }
    }
}