package frc.subsystem; 

import edu.wpi.first.wpilibj.Joystick;

public class controller {
    private static controller instance = new controller();
    private Joystick thrustStick = new Joystick(0);
    private Joystick yawStick = new Joystick(1);
    private Joystick buttonMonkey; 

    public controller(){
        buttonMonkey = new Joystick(0);
    }
    
    public static controller getInstance(){
        return instance; 
    }

    public boolean getCargoIntake(int version){
        if(version == 0){
            return buttonMonkey.getRawButtonPressed(1);
        }
        else {
            return buttonMonkey.getRawButton(1);
        }
    }

    /***********************\
	|* Drivetrain Controls *|
	\***********************/

    public boolean getGyroReset(){
        return thrustStick.getRawButton(2);
    }

    public boolean RobotCentric(){
        return thrustStick.getRawButton(3);
    }

    public boolean frontLeftRotation(){
        return thrustStick.getRawButton(5);
    }

    public boolean frontRightRotation(){
        return thrustStick.getRawButton(6);
    }

    public boolean Boosted(){
        return thrustStick.getRawButton(1);
    }

    public boolean TrackTarget(){
        return yawStick.getRawButton(1);
    }

    public boolean defenseEnabled(){
        return thrustStick.getRawButton(4);
    }

    public double getForward(){
        if(Math.abs(thrustStick.getY()) > 0.05){
            return -thrustStick.getY();
        } else {
            return 0; 
        }
    }

    public double getStrafe(){
        if(Math.abs(thrustStick.getX()) > 0.05){
            return -thrustStick.getX();
        } else {
            return 0; 
        }
    }

    public double getSpin(){
        if(Math.abs(yawStick.getX()) > 0.05){
            return yawStick.getX()*0.2;
        } else {
            return 0; 
        }
    }
}