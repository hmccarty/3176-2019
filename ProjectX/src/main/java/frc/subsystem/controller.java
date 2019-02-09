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
            return buttonMonkey.getRawButtonPressed(0);
        }
        else {
            return buttonMonkey.getRawButton(0);
        }
    }

    public boolean getGyroReset(){
        return thrustStick.getRawButton(2);
    }

    public double getForward(){
        return thrustStick.getY();
    }

    public double getStrafe(){
        return thrustStick.getX();
    }

    public double getRotation(){
        return yawStick.getX();
    }
}