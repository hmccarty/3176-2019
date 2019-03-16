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

    private pid mCargoDeployPID;
    private pid mCargoStowPID;
    private pid mCargoManualPID;

    private DigitalInput mCargoIntakeStowedSwitch;
    private DigitalInput mCargoIntakeDeployedSwitch;
    private DigitalInput mBallCapturedSwitch;

    private Encoder mCargoWinchEncoder; 

    private Talon mCargoIntakeWinch;
    private Talon mCargoIntakeBeaterBar; 

    Boolean cIntakeIsAtStowedLimit;
    Boolean cIntakeIsAtDeployedLimt;
    Boolean cIsHomed;
    int cCargoIntakeWinchPosition;
    double cCargoIntakeWinchPower;

    private int kStowedHeight = constants.STOWED_HEIGHT;   //The position of the winch when stowed
    private int kIntakeHeight = constants.DEPLOYED_HEIGHT; //The position of the winch when Deployed
    private int kRocketHeight = constants.ROCKET_HEIGHT;   //The position of the winch when shooting for the Rocket

    private Boolean cInOpenLoop = false;
    private double cOpenLoopPower = 0;

    public cargointake(){
        mCargoStowPID = new pid(0.0004, 0,0, .4); //The PID values for Deploying the mechanism
        mCargoDeployPID = new pid(0.00003,0,0, .7);      //The PID values for Retracting the mechanism
        mCargoManualPID = new pid(0.00007,0,0, .6);      //Thr PID values for manual control

        /**
         * Declaring Sensors
         * Please Note: Deployed and Stowed Switches are Inverted
         */
        mCargoIntakeStowedSwitch = new DigitalInput(constants.CARGO_INTAKE_STOWED);
        mCargoIntakeDeployedSwitch = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        mBallCapturedSwitch = new DigitalInput(constants.CARGO_IN_INTAKE);

        mCargoWinchEncoder = new Encoder(constants.CARGO_INTAKE_ENCODER[0],
                              constants.CARGO_INTAKE_ENCODER[1], 
                              false, Encoder.EncodingType.k4X);



        /**
         * Declaring Motors
         * Please Note: Winch Commands are Inverteds
         */                     
        mCargoIntakeWinch = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        mCargoIntakeBeaterBar = new Talon(constants.CARGO_INTAKE_ROLLER); 
        
        cIsHomed = false; 

        //Initialize members
        mCargoWinchEncoder.reset();
    }

    public static cargointake getInstance() {
        return instance;
    }

    /** 
    * Handles position control of cargo intake
    */
    private void closedLoopControl (int wantedHeight) {
        //This method sets the winch motor based on the commanded height
            //This method checks the downLimit Switch and the stowed limit switch
            //And reads the encoder position.
            //
            // And sets the winch motor 

            //Inputs
            cIntakeIsAtDeployedLimt = !mCargoIntakeDeployedSwitch.get();
            cIntakeIsAtStowedLimit = !mCargoIntakeStowedSwitch.get();
            cCargoIntakeWinchPosition = mCargoWinchEncoder.getRaw();


            //Processing

            if(cIntakeIsAtStowedLimit && !cIntakeIsAtDeployedLimt){
                mCargoWinchEncoder.reset();
                if(wantedHeight < cCargoIntakeWinchPosition){
                    //In this scenario we are commanding a winch position that is beyond the stowed limit
                    cCargoIntakeWinchPower = mCargoDeployPID.returnOutput(cCargoIntakeWinchPosition, cCargoIntakeWinchPosition);
                }else{
                    //In this scenario we are commanding the winch to come off of the limit switch
                    cCargoIntakeWinchPower = mCargoDeployPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                }

            } else if (cIntakeIsAtDeployedLimt && !cIntakeIsAtStowedLimit){
                if(wantedHeight > cCargoIntakeWinchPosition){
                    //In this scenario we are commanding a winch position that is beyond the stowed deployed limit
                    cCargoIntakeWinchPower = mCargoDeployPID.returnOutput(cCargoIntakeWinchPosition, cCargoIntakeWinchPosition);
                }else{
                    //In this scenario we are commanding the winch to come off of the deployed limit switch
                    cCargoIntakeWinchPower = mCargoStowPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                }
            } else {
                    //We are in the middle just responding to normal commands
                    if(wantedHeight < cCargoIntakeWinchPosition){
                        //In this scenario we are commanding a winch position that is lower
                        cCargoIntakeWinchPower = mCargoStowPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                    }else{
                        //In this scenario we are commanding the winch position that is higher
                        cCargoIntakeWinchPower = mCargoDeployPID.returnOutput(cCargoIntakeWinchPosition, wantedHeight);
                    }
            }
        mCargoIntakeWinch.set(-cCargoIntakeWinchPower);
    }

    public void deploy(){
        closedLoopControl(kIntakeHeight);
    }

    public void stow(){
        closedLoopControl(kStowedHeight);
    }

    public void rocket(){
        closedLoopControl(kRocketHeight);
    }

    public boolean  isStowed(){
        return !mCargoIntakeStowedSwitch.get();
    }

    public boolean isHomed(){
        return cIsHomed; 
    }

    public void home(){
        if(!mCargoIntakeStowedSwitch.get()){
            cIsHomed = true;
            mCargoIntakeWinch.set(0);
            mCargoWinchEncoder.reset();
        } else {
            cIsHomed = false; 
            mCargoIntakeWinch.set(-.3);
        }
    }

    /**
     * Moves cargo intake to set height
     * @param height in encoder units
     */
    public void moveTo(int height){
        closedLoopControl(height);
    }

    /**
     * Allows for driver to manually control position
     * @param height in encoder units
     * @param override determines whether or not the system is in open loop
     */
    public void manualControl(int height, boolean override){
        if(!mCargoIntakeStowedSwitch.get()) {
            mCargoWinchEncoder.reset();
        }
        mCargoIntakeWinch.set(-mCargoManualPID.returnOutput(mCargoWinchEncoder.getRaw(), height));
    }

    public void deployBoolean(){
        if(mCargoIntakeDeployedSwitch.get()){
            mCargoIntakeWinch.set(-.4);
        } else {
            mCargoIntakeWinch.set(0);
        }
    }

    public void stowBoolean(){
        if(mCargoIntakeStowedSwitch.get()){
            mCargoIntakeWinch.set(.2);
        } else {
            mCargoIntakeWinch.set(0);
        }
    }

    public boolean hasBall(){
        return !mBallCapturedSwitch.get();
    }

    public int getHeight(){
        return mCargoWinchEncoder.getRaw();
    }

    public void intake(){
        mCargoIntakeBeaterBar.set(.5);
    }

    public void spit(){
        mCargoIntakeBeaterBar.set(-.7);
    }

    public void transfer(){
        mCargoIntakeBeaterBar.set(.3);
    }

    public void hold(){
        mCargoIntakeBeaterBar.set(0);
    }

    public void cargoIntakeOpenLoop(double openLoopCommand){
        if(!mCargoIntakeStowedSwitch.get() && mCargoIntakeDeployedSwitch.get()){
            mCargoWinchEncoder.reset();
            if(openLoopCommand < 0) {
                mCargoIntakeWinch.set(openLoopCommand);
           }
           else {
               mCargoIntakeWinch.set(0);
           }
       }
       else if(mCargoIntakeStowedSwitch.get() && !mCargoIntakeDeployedSwitch.get()) {
           if(openLoopCommand > 0) {
               mCargoIntakeWinch.set(openLoopCommand);
           }
           else {
               mCargoIntakeWinch.set(0);
           }
        }
    }

    public void outputToSmartDashboard(){
        //Set the outputs
        SmartDashboard.putBoolean("DEPLOYED", !mCargoIntakeStowedSwitch.get());
        SmartDashboard.putBoolean("STOWED", !mCargoIntakeDeployedSwitch.get());
        SmartDashboard.putBoolean("HAVE CARGO", !mBallCapturedSwitch.get());
        SmartDashboard.putBoolean("HOMED", isHomed());

        // SmartDashboard.putNumber("Cargo Winch Encoder Real: ", mCargoWinchEncoder.getRaw());
        // SmartDashboard.putNumber("Cargo Winch Power: ", cCargoIntakeWinchPower);
    }

    public void zeroAllSensors() {
        mCargoWinchEncoder.reset();
    }
}