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

    private DigitalInput mStowedSwitch;
    private DigitalInput mCargoSwitch;

    private Encoder encoder; 

    private Talon actuator;
    private Talon roller; 

    private Timer timer; 

    private int kStowedHeight = constants.STOWED_HEIGHT;
    private int kIntakeHeight = constants.DEPLOYED_HEIGHT;
    private int kRocketHeight = constants.ROCKET_HEIGHT;

    public cargointake(){
        cargoStowPID = new pid(0.00009, 0,0,.25);
        cargoPID = new pid(0.00009,0,0, .8);

        mStowedSwitch = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        mCargoSwitch = new DigitalInput(constants.CARGO_IN_INTAKE);

        encoder = new Encoder(constants.CARGO_INTAKE_ENCODER[0],
                              constants.CARGO_INTAKE_ENCODER[1], 
                              false, Encoder.EncodingType.k4X);

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
        //if(!mStowedSwitch.get()){
            if(wantedHeight < encoder.getRaw()){
                actuator.set(-cargoStowPID.returnOutput(encoder.getRaw(), wantedHeight));
            } else {
                actuator.set(-cargoPID.returnOutput(encoder.getRaw(), wantedHeight));
            }
        //} else {
          //  actuator.set(0);
           // encoder.reset();
        //}
    }

    public void deploy(){
        closedLoopControl(kIntakeHeight);
    }

    public void stow(){
        closedLoopControl(kStowedHeight);
    }

    public void moveTo(int height){
        closedLoopControl(height);
    }

    public double getHeight(){
        return encoder.getRaw();
    }

    public void intake(){
        roller.set(-.5);
    }

    public void spit(){
        roller.set(.9);
    }

    public void hold(){
        roller.set(0);
    }

    public void zeroAllSensors() {
        encoder.reset();
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Encoder", encoder.get()); 
    }
}