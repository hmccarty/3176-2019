package frc.subsystem;

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class hatchintake extends subsystem{
    private static hatchintake instance = new hatchintake();
    private DigitalInput isDown;
    private DigitalInput isUp;
    private DigitalInput sensor;
    private Talon actuator;
    private Talon roller; 
    private Timer intakeTimer; 
    private controller c; 

    public hatchintake(){
        isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
        isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
        actuator = new Talon(constants.HATCH_INTAKE_ACTUATOR);
        roller = new Talon(constants.HATCH_INTAKE_ROLLER);
        

        intakeTimer = new Timer();
        intakeTimer.start();

        c = controller.getInstance();
    }

    public static hatchintake getInstance(){ return instance; }

    public boolean deployIntake(){
        if(isDown.get()){
            actuator.set(0);
            return true; 
        }
        else {
            actuator.set(-.3);
            return false; 
        }
    }

    public boolean stowIntake(){
        if(isUp.get()){
            actuator.set(0);
            return true;
        }
        else {
            actuator.set(.3);
            return false;
        }
    }

    public boolean runIntake(double speed){
        if (sensor.get()){
            roller.set(0);
            return true;
        }
        else {
            roller.set(speed);
            return false;
        }       
    }

    public void zeroAllSensors(){}

    public void outputToSmartDashboard(){
        SmartDashboard.putBoolean("isDown value: ", isDown.get());
        SmartDashboard.putBoolean("isUp value: ", isUp.get());
        SmartDashboard.putBoolean("sensor value; ", sensor.get());
    }
}