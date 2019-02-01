package frc.subsystem;

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants;

public class cargointake extends subsystem {
    private static cargointake instance = new cargointake(); 
    private DigitalInput isDown;
    private DigitalInput isUp;
    private Talon actuator;
    private Talon roller; 
    private Timer intakeTimer; 
    private controller c; 

    public cargointake(){
        isDown = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        isUp = new DigitalInput(constants.CARGO_INTAKE_UP);
        actuator = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        roller = new Talon(constants.CARGO_INTAKE_ROLLER);
        

        intakeTimer = new Timer();
        intakeTimer.start();

        c = controller.getInstance();
    }

    public static cargointake getInstance(){ return instance; }

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

    public void runIntake(double speed){
        roller.set(speed);
    }

    public void zeroAllSensors(){}

    public void outputToSmartDashboard(){}
}