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

    private pid cargoDeployPID;
    private pid cargoStowPID;

    private DigitalInput mCargoIntakeStowedSwitch;
    private DigitalInput mCargoIntakeDeployedSwitch;
    private DigitalInput mBallCapturedSwitch;

    private Encoder cargoWinchEncoder; 

    private Talon cargoIntakeWinch;
    private Talon cargoIntakeBeaterBar; 

    private Timer randomGodDamnTimer; 

    Boolean cIntakeIsAtStowedLimit;
    Boolean cIntakeIsAtDeployedLimt;
    int cCargoIntakeWinchPosition;
    double cCargoIntakeWinchPower;

    private int kStowedHeight = constants.STOWED_HEIGHT;   //The position of the winch when stowed
    private int kIntakeHeight = constants.DEPLOYED_HEIGHT; //The position of the winch when Deployed
    private int kRocketHeight = constants.ROCKET_HEIGHT;   //The position of the winch when shooting for the Rocket

    public cargointake(){
        cargoStowPID = new pid(0.00009, 0,0,.25); //The PID values for Deploying the mechanism
        cargoDeployPID = new pid(0.00009,0,0, .8);      //The PID values for Retracting the mechanism


        //Declare Sensors
        mCargoIntakeStowedSwitch = new DigitalInput(constants.PORTSW_CARGO_INTAKE_STOWED);
        mCargoIntakeDeployedSwitch = new DigitalInput(constants.PORTSW_CARGO_INTAKE_DOWN);
        mBallCapturedSwitch = new DigitalInput(constants.PORTSW_CARGO_IN_INTAKE);

        cargoWinchEncoder = new Encoder(constants.CARGO_INTAKE_ENCODER[0],
                              constants.CARGO_INTAKE_ENCODER[1], 
                              false, Encoder.EncodingType.k4X);



        //Declare Actuators                      
        cargoIntakeWinch = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        cargoIntakeBeaterBar = new Talon(constants.CARGO_INTAKE_ROLLER); 
        

        //Initialize members
        cargoWinchEncoder.reset();
        
        randomGodDamnTimer = new Timer();
        randomGodDamnTimer.start();
    }

    public static cargointake getInstance() {
        return instance;
    }

    private void closedLoopControl (int wantedHeight) {
        //This method sets the winch motor based on the commanded height
            //This method checks the downLimit Switch and the stowed limit switch
            //And reads the encoder position.
            //
            // And sets the winch motor 


            //if(intakeIsAtStowedLimit && !cargoIntakeWinchIsHomed){
            //    cargoIntakeWinchIsHomed = true;
            //    cargoWinchEncoder.reset();
            //}

            //Inputs
            cIntakeIsAtDeployedLimt = mCargoIntakeDeployedSwitch.get();
            cIntakeIsAtStowedLimit = mCargoIntakeStowedSwitch.get();
            cCargoIntakeWinchPosition = cargoWinchEncoder.getRaw();

            //Processing

            if(cIntakeIsAtStowedLimit && !cIntakeIsAtDeployedLimt){
                if(wantedHeight > cCargoIntakeWinchPosition){
                    //In this scenario we are commanding a winch position that is beyond the stowed limit
                    cCargoIntakeWinchPower = 0;
                }else{
                    //In this scenario we are commanding the winch to come off of the limit switch
                    cCargoIntakeWinchPower = cargoDeployPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                }

            } else if (cIntakeIsAtDeployedLimt && !cIntakeIsAtStowedLimit){
                if(wantedHeight < cCargoIntakeWinchPosition){
                    //In this scenario we are commanding a winch position that is beyond the stowed deployed limit
                    cCargoIntakeWinchPower = 0;
                }else{
                    //In this scenario we are commanding the winch to come off of the deployed limit switch
                    cCargoIntakeWinchPower = cargoStowPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                }
            } else {
                    //We are in the middle just responding to normal commands
                    if(wantedHeight < cCargoIntakeWinchPosition){
                        //In this scenario we are commanding a winch position that is lower
                        cCargoIntakeWinchPower = cargoStowPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                    }else{
                        //In this scenario we are commanding the winch position that is higher
                        cCargoIntakeWinchPower = cargoDeployPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                    }
            }
            cargoIntakeWinch.set(cCargoIntakeWinchPower);
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

    public boolean hasBall(){
        return mBallCapturedSwitch.get();
    }

    public double getHeight(){
        return cargoWinchEncoder.getRaw();
    }

    public void intake(){
        cargoIntakeBeaterBar.set(-.6);
    }

    public void spit(){
        cargoIntakeBeaterBar.set(.8);
    }

    public void hold(){
        cargoIntakeBeaterBar.set(0);
    }

    public void outputToSmartDashboard(){
        //Set the outputs
        SmartDashboard.putBoolean("Up Limit Switch", mCargoIntakeDeployedSwitch.get());
        SmartDashboard.putBoolean("Down Limit Switch", mCargoIntakeStowedSwitch.get());
        SmartDashboard.putNumber("Cargo Winch Encoder: ", cCargoIntakeWinchPosition);
        SmartDashboard.putNumber("Cargo Winch Power: ", cCargoIntakeWinchPower);
    }

    public void zeroAllSensors() {
        cargoWinchEncoder.reset();
    }
}