package frc.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.util.pid;
import frc.robot.constants;

public class cargointake {
    private static cargointake instance = new cargointake(); 

    private pid mCargoIntakePID;
    private pid mCargoDeployPID;
    private pid mCargoStowPID;
    private pid mCargoManualPID;

    private DigitalInput mCargoIntakeStowedSwitch;
    private DigitalInput mCargoIntakeDeployedSwitch;
    private DigitalInput mBallCapturedSwitch;

    private Encoder mCargoWinchEncoder; 

    private Talon mCargoIntakeWinch;
    private Talon mCargoIntakeBeaterBar; 

    Boolean intakeIsAtStowedLimit;
    Boolean intakeIsAtDeployedLimit;
    Boolean isHomed;
    int currentHeight;
    double cargoIntakeWinchPower;

    Boolean isOpenLoop = false; 

    private int kStowedHeight = constants.STOWED_HEIGHT;   //The position of the winch when stowed
    private int kIntakeHeight = constants.DEPLOYED_HEIGHT; //The position of the winch when deployed
    private int kRocketHeight = constants.ROCKET_HEIGHT;   //The position of the winch when shooting in the rocket
    private int kTransferHeight = constants.TRANSFER_HEIGHT; 

    public cargointake() {
        mCargoIntakePID = new pid(constants.ELEVATOR_SPEED_PID_CONFIG[0],
                                  constants.ELEVATOR_SPEED_PID_CONFIG[1],
                                  constants.ELEVATOR_SPEED_PID_CONFIG[2],
                                  constants.ELEVATOR_SPEED_PID_CONFIG[3],
                                  constants.ELEVATOR_SPEED_PID_CONFIG[4],
                                  constants.ELEVATOR_SPEED_PID_CONFIG[5], 
                                  constants.ELEVATOR_SPEED_PID_CONFIG[6]); 

        //mCargoStowPID = new pid(0.00015, 0,0, .3); //The PID values for Deploying the mechanism
        //mCargoDeployPID = new pid(0.00015,0,0, .7); //The PID values for Retracting the mechanism
        //mCargoManualPID = new pid(0.00007,0,0, .6); //The PID values for manual control

        /**
         * Declaring Sensors
         * Please Note: Deployed and Stowed Switches are Inverted
         */
        mCargoIntakeStowedSwitch = new DigitalInput(constants.CARGO_INTAKE_STOWED);
        mCargoIntakeDeployedSwitch = new DigitalInput(constants.CARGO_INTAKE_DOWN);
        mBallCapturedSwitch = new DigitalInput(constants.CARGO_IN_ROLLER);

        mCargoWinchEncoder = new Encoder(constants.CARGO_INTAKE_ENCODER[0],
                              constants.CARGO_INTAKE_ENCODER[1], 
                              false, Encoder.EncodingType.k4X);



        /**
         * Declaring Motors
         * Please Note: Winch Commands are Inverted
         */                     
        mCargoIntakeWinch = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        mCargoIntakeBeaterBar = new Talon(constants.CARGO_INTAKE_ROLLER); 
        
        isHomed = false; 

        //Initialize members
       // mCargoWinchEncoder.reset();
    }

    public static cargointake getInstance() {
        return instance;
    }

    public void setOpenLoop(boolean isOpenLoop){
        this.isOpenLoop = isOpenLoop; 
    }

    /** 
    * Handles position control of cargo intake
    */
    private void closedLoopControl (int wantedHeight) {
        intakeIsAtDeployedLimit = !mCargoIntakeDeployedSwitch.get();
        intakeIsAtStowedLimit = !mCargoIntakeStowedSwitch.get();
        currentHeight = mCargoWinchEncoder.getRaw();

        if(intakeIsAtStowedLimit) { 
            wantedHeight = (wantedHeight < currentHeight) ? wantedHeight : currentHeight; 
        } else if (intakeIsAtDeployedLimit) {
            wantedHeight = (wantedHeight > currentHeight) ? wantedHeight : currentHeight; 
        } 

        cargoIntakeWinchPower = mCargoIntakePID.returnOutput(currentHeight, wantedHeight);
        mCargoIntakeWinch.set(cargoIntakeWinchPower);
    }

    public void deploy() {
        if(!isOpenLoop){
            closedLoopControl(kIntakeHeight);
        }
    }

    public void stow() {
        if(!isOpenLoop){
            closedLoopControl(kStowedHeight);
        }
    }

    public void rocket() {
        if(!isOpenLoop){
            closedLoopControl(kRocketHeight);
        }
    }

    public void moveToTransfer() {
        if(!isOpenLoop){
            closedLoopControl(kTransferHeight);
        }
    }

    public boolean  isStowed() {
        return !mCargoIntakeStowedSwitch.get();
    }

    public boolean isHomed() {
        return isHomed; 
    }

    public void home() {
        if(!mCargoIntakeStowedSwitch.get()) {
            isHomed = true;
            mCargoIntakeWinch.set(0);
            mCargoWinchEncoder.reset();
        } else {
            isHomed = false; 
            mCargoIntakeWinch.set(.3);
        }
    }

    /**
     * Moves cargo intake to set height
     * @param height in encoder units
     */
    public void moveTo(int height) {
        closedLoopControl(height);
    }

    /**
     * Allows for driver to manually control position
     * @param height in encoder units
     * @param override determines whether or not the system is in open loop
     */
    public void manualControl(int height, boolean override) {
        closedLoopControl(height); 
        // if(!mCargoIntakeStowedSwitch.get()) {
        //     //mCargoWinchEncoder.reset();
        // }
        // mCargoIntakeWinch.set(mCargoIntakePID.returnOutput(mCargoWinchEncoder.getRaw(), height));
    }

    public void deployBoolean() {
        if(mCargoIntakeDeployedSwitch.get()) {
            mCargoIntakeWinch.set(.4);
        } else {
            mCargoIntakeWinch.set(0);
        }
    }

    public void stowBoolean() {
        if(mCargoIntakeStowedSwitch.get()) {
            mCargoIntakeWinch.set(.2);
        } else {
            mCargoIntakeWinch.set(0);
        }
    }

    public boolean hasBall() {
        return !mBallCapturedSwitch.get();
    }

    public int getHeight() {
        return mCargoWinchEncoder.getRaw();
    }

    public void intake() {
        mCargoIntakeBeaterBar.set(.5);
    }

    public void spit() {
        mCargoIntakeBeaterBar.set(-.7);
    }

    public void transfer() {
        if(!isOpenLoop){
            closedLoopControl(kTransferHeight);
            mCargoIntakeBeaterBar.set(.3);
        }
    }

    public void hold() {
        mCargoIntakeBeaterBar.set(0);
    }

    public void openLoop(double openLoopCommand) {
        mCargoIntakeWinch.set((openLoopCommand + .15));
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("DEPLOYED", !mCargoIntakeDeployedSwitch.get());
        SmartDashboard.putBoolean("STOWED", !mCargoIntakeStowedSwitch.get());
        SmartDashboard.putBoolean("HAVE CARGO", !mBallCapturedSwitch.get());
        SmartDashboard.putBoolean("HOMED", isHomed());
        SmartDashboard.putNumber("CARGO INTAKE POSITION", mCargoWinchEncoder.getRaw());
    }

    public void zeroAllSensors() {
        mCargoWinchEncoder.reset();
    }
}