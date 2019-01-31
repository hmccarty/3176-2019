package frc.subsystem; 

import edu.wpi.first.wpilibj.Joystick;

public class controller {
    private static controller instance = new controller();
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
}