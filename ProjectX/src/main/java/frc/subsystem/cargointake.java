package frc.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.util.pid;
import frc.robot.constants;

/** 
 * Back intake, picking up cargo from the floor and depositing it into
 * low scoring positions
 */
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

    Boolean intakeIsAtStowedLimit;
    Boolean intakeIsAtDeployedLimit;
    Boolean isHomed;
    int cargoIntakeWinchPosition;
    double cargoIntakeWinchPower;

    Boolean isOpenLoop = false; 

    private int kStowedHeight = constants.STOWED_HEIGHT;   // The position of the winch when stowed
    private int kIntakeHeight = constants.DEPLOYED_HEIGHT; // The position of the winch when deployed
    private int kRocketHeight = constants.ROCKET_HEIGHT;   // The position of the winch when shooting in the rocket
    private int kTransferHeight = constants.TRANSFER_HEIGHT;  // The position of the winch when transfer cargo through the elevator

    public cargointake() {
        mCargoStowPID = new pid(0.00015, 0,0, .3); // The PID values for Deploying the mechanism
        mCargoDeployPID = new pid(0.00015,0,0, .7); // The PID values for Retracting the mechanism
        mCargoManualPID = new pid(0.00007,0,0, .6);  // The PID values for manual control

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
         * Please Note: Winch Commands are Inverteds
         */                     
        mCargoIntakeWinch = new Talon(constants.CARGO_INTAKE_ACTUATOR);
        mCargoIntakeBeaterBar = new Talon(constants.CARGO_INTAKE_ROLLER); 
        
        isHomed = false; 

        // Initialize members
        mCargoWinchEncoder.reset();
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
    private void closedLoopControl(int wantedHeight) {
            SmartDashboard.putNumber("WANTED CARGO INTAKE", wantedHeight);
            intakeIsAtDeployedLimit = !mCargoIntakeDeployedSwitch.get();
            intakeIsAtStowedLimit = !mCargoIntakeStowedSwitch.get();
            cargoIntakeWinchPosition = mCargoWinchEncoder.getRaw();

            if(intakeIsAtStowedLimit && !intakeIsAtDeployedLimit) {
                if(wantedHeight < cargoIntakeWinchPosition) {
                    //In this scenario we are commanding a winch position that is beyond the stowed limit
                    cargoIntakeWinchPower = mCargoDeployPID.returnOutput(cargoIntakeWinchPosition, cargoIntakeWinchPosition);
                } else {
                    //In this scenario we are commanding the winch to come off of the limit switch
                    cargoIntakeWinchPower = mCargoDeployPID.returnOutput(cargoIntakeWinchPosition, wantedHeight);
                }

            } else if (intakeIsAtDeployedLimit && !intakeIsAtStowedLimit) {
                if(wantedHeight > cargoIntakeWinchPosition) {
                    //In this scenario we are commanding a winch position that is beyond the stowed deployed limit
                    cargoIntakeWinchPower = mCargoDeployPID.returnOutput(cargoIntakeWinchPosition, cargoIntakeWinchPosition);
                } else {
                    //In this scenario we are commanding the winch to come off of the deployed limit switch
                    cargoIntakeWinchPower = mCargoStowPID.returnOutput(cargoIntakeWinchPosition, wantedHeight);
                }
            } else {
                    //We are in the middle just responding to normal commands
                    if(wantedHeight < cargoIntakeWinchPosition) {
                        //In this scenario we are commanding a winch position that is lower
                        cargoIntakeWinchPower = mCargoStowPID.returnOutput(cargoIntakeWinchPosition, wantedHeight);
                    } else {
                        //In this scenario we are commanding the winch position that is higher
                        cargoIntakeWinchPower = mCargoDeployPID.returnOutput(cargoIntakeWinchPosition, wantedHeight);
                    }
            }
        
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
        if(!mCargoIntakeStowedSwitch.get()) {
            mCargoWinchEncoder.reset();
        }
        SmartDashboard.putNumber("Manual Wanted", -mCargoManualPID.returnOutput(mCargoWinchEncoder.getRaw(), height));
        mCargoIntakeWinch.set(mCargoManualPID.returnOutput(mCargoWinchEncoder.getRaw(), height));
    }

    public boolean hasBall() {
        return !mBallCapturedSwitch.get();
    }

    public int getHeight() {
        return mCargoWinchEncoder.getRaw();
    }

    // Spins wheels to grab cargo ball
    public void intake() {
        mCargoIntakeBeaterBar.set(.5);
    }

    // Fires cargo ball 
    public void spit() {
        mCargoIntakeBeaterBar.set(-.7);
    }

    // Moves intake to transfer height and feeds ball to claw
    public void transfer() {
        if(!isOpenLoop){
            closedLoopControl(kTransferHeight);
            mCargoIntakeBeaterBar.set(.3);
        }
    }

    public void hold() {
        mCargoIntakeBeaterBar.set(0);
    }

    // Allows for control in case of a faulty encoder
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