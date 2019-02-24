package frc.subsystem;

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.util.pid;
import frc.robot.constants;

public class cargointake {
    private static cargointake instance = new cargointake(); 

    private pid cargoPID;
    private pid cargoStowPID;
    //private DigitalInput sensor;
    private Encoder encoder; 
    private Talon actuator;
    private Talon roller; 
    private Timer timer; 

    private int kStowedHeight = -10;
    private int kIntakeHeight = -45000;
    private int kRocketHeight = -24000;

    public cargointake(){
        cargoStowPID = new pid(0.00009, 0,0,.25);
        cargoPID = new pid(0.00009,0,0, .8);
        //sensor = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        actuator = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        roller = new Talon(constants.CARGO_INTAKE_ROLLER);
        
        encoder.reset();

        timer = new Timer();
        timer.start();
    }

    public static cargointake getInstance() {
        return instance;
    }

    private void closedLoopControl (int wantedHeight) {
        if(wantedHeight > encoder.getRaw()){
            actuator.set(-cargoStowPID.returnOutput(encoder.getRaw(), wantedHeight));
        } else {
        actuator.set(-cargoPID.returnOutput(encoder.getRaw(), wantedHeight));
        }
    }

    public void deploy(){
        closedLoopControl(kIntakeHeight);
    }


    public void stow(){
        closedLoopControl(kStowedHeight);
    }

    public void moveToRocket(){
        closedLoopControl(kRocketHeight);
    }

    public void intake(){
        roller.set(-.5);
    }

    public void stopRoller(){
        roller.set(0);
    }

    public void stopActuator(){
        actuator.set(0);
    }

    public void spit(){
        roller.set(.7);
    }

    public void run(double speed) {
        roller.set(speed);
    }

    public void zeroAllSensors() {
        encoder.reset();
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Encoder", encoder.get()); 
    }
}