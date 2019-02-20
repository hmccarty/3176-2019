package frc.subsystem;

import frc.util.subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import frc.util.pid;
import frc.robot.constants;

public class cargointake {
    private static cargointake instance = new cargointake(); 

    private pid cargoPID;
    private DigitalInput sensor;
    private Encoder encoder; 
    private Talon actuator;
    private Talon roller; 
    private Timer timer; 
    //private int stowedHeight = constants.CARGO_STOWED_HEIGHT; 
    //private int intakeHeight = constants.CARGO_INTAKE_HEIGHT;

    public cargointake(){
        cargoPID = new pid(0,0,0);
        sensor = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        actuator = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        roller = new Talon(constants.CARGO_INTAKE_ROLLER);
        

        timer = new Timer();
        timer.start();
    }

    public static cargointake getInstance() {
        return instance;
    }

    private void closedLoopControl (int wantedHeight) {
        actuator.set(cargoPID.returnOutput(encoder.getRaw(), wantedHeight));
    }

    public void deploy(){
        if(sensor.get()){
            actuator.set(0);
            encoder.reset();
        }
        else {
            //closedLoopControl(intakeHeight);
        }
    }

    public void stow(){
        //closedLoopControl(stowedHeight);
    }

    public void run(double speed) {
        roller.set(speed);
    }

    public void zeroAllSensors() {

    }

    public void outputToSmartDashboard() {

    }
}