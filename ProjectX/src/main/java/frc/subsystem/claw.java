package frc.subsystem; 

import frc.util.subsystem; 

public class claw extends subsystem {
    private static claw instance = new claw();

    public claw(){}
    
    public static claw getInstance(){
        return instance; 
    }
}