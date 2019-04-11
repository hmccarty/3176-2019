package frc.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.subsystem.controller;
import frc.robot.constants;
import frc.util.*;
import frc.util.trajectory;

public class elevator {
    private static elevator instance = new elevator();
    private controller mController = controller.getInstance();
    private claw mClaw = claw.getInstance();

    private CANSparkMax mWinchLeft;
    private CANSparkMax mWinchRight;

    private CANPIDController mPositionController;
    private pid mSpeedController; 
    private CANEncoder mEncoder; 

    private PowerDistributionPanel powerPanel = new PowerDistributionPanel();

    private double wantedHeight;
    private double lastWantedHeight = 0; 
    private double wantedSpeed; 

    private DigitalInput mCarriageBottomSwitch;
    private DigitalInput mStageBottomSwitch;
    private Boolean isAtBottom = false;

    public enum state {
        HOLDING,
        OPEN_LOOP,
        MANUAL_CONTROL,
        POSITION_CONTROL
    }

    private state mCurrentState;
    private state mWantedState;
    private state mLastState;

    public elevator() {
        mWinchLeft = new CANSparkMax(constants.ELEVATOR_LEFT, MotorType.kBrushless);
        mWinchRight = new CANSparkMax(constants.ELEVATOR_RIGHT, MotorType.kBrushless);
        mWinchLeft.restoreFactoryDefaults();
        mWinchRight.restoreFactoryDefaults();
        mEncoder = mWinchLeft.getEncoder();
        mPositionController = mWinchLeft.getPIDController();

        mPositionController.setP(constants.ELEVATOR_POSITION_PID_CONFIG[0]);
        mPositionController.setI(constants.ELEVATOR_POSITION_PID_CONFIG[1]);
        mPositionController.setD(constants.ELEVATOR_POSITION_PID_CONFIG[2]);
        mPositionController.setFF(constants.ELEVATOR_POSITION_PID_CONFIG[3]);
        mPositionController.setIZone(constants.ELEVATOR_POSITION_PID_CONFIG[4]);

        mPositionController.setOutputRange(constants.ELEVATOR_POSITION_PID_CONFIG[5], constants.ELEVATOR_POSITION_PID_CONFIG[6]);

        mSpeedController = new pid(0.07, 0, 0, 0.8);

        mWinchLeft.setSmartCurrentLimit(constants.SMART_CURRENT_LIMIT);
        mWinchRight.setSmartCurrentLimit(constants.SMART_CURRENT_LIMIT);

        mWinchLeft.setClosedLoopRampRate(constants.RAMP_RATE);
        mWinchRight.setClosedLoopRampRate(constants.RAMP_RATE);

        mWinchRight.follow(mWinchLeft, true);

        mCarriageBottomSwitch = new DigitalInput(constants.CARRIAGE_BOTTOM_BUMP_SWITCH);
        mStageBottomSwitch = new DigitalInput(constants.STAGE_BOTTOM_BUMP_SWITCH);

        mEncoder.setPosition(0);
    }

    public static elevator getInstance() {
        return instance;
    }

    private void setHeight(double wantedHeight) {
        if(wantedHeight == -1) {
            wantedHeight = 0;
        }

        if(mClaw.isExtended() && wantedHeight > 4.5 && mCurrentState != state.MANUAL_CONTROL) {
            if(mController.wantedElevatorHeight() > 15.25 ) {
                wantedHeight -= 4.0;
            } else if (mController.wantedElevatorHeight() < 14) {
                wantedHeight -= 5.1;
            } else {
                wantedHeight -= 6.0;
            }
        }

        if(!cargointake.getInstance().isStowed()) {
            mPositionController.setReference(wantedHeight, ControlType.kPosition); 
        } 
    }


    private void setSpeed(double wantedSpeed) {
        mWinchLeft.set(mSpeedController.returnOutput(mEncoder.getVelocity(), wantedSpeed));
    }

    public boolean inPosition() {
        if(Math.abs(mEncoder.getVelocity())  < .1){
            return true; 
        } else {
            return false; 
        }
    }

    public void setWantedElevatorHeight(double wantedHeight){
        this.wantedHeight = wantedHeight;
    }

    public double getHeight() {
        return mEncoder.getPosition();
    }

    public double getLastWantedHeight(){
        return lastWantedHeight;
    }

    private void checkLimits() {
        if(!mCarriageBottomSwitch.get() && !mStageBottomSwitch.get()){
            isAtBottom = true;
            mEncoder.setPosition(0);
        } else{
            isAtBottom = false;
        }
    }

    public void setWantedState(state wantedState) {
        this.mWantedState = wantedState;
    }

    public state getState() {
        return mCurrentState;
    }

    public void checkState() {
        if (mCurrentState != mWantedState) {
            mCurrentState = mWantedState;
        }
    }

    public void registerLoop() {
        loopmanager.getInstance().addLoop(new loop()
        {
            @Override
            public void onStart() {
                mCurrentState = state.HOLDING;
                mWantedState = state.HOLDING;
            }

            @Override
            public void onLoop() {
                switch(mCurrentState) {
                    case HOLDING:
                        if(mLastState == state.MANUAL_CONTROL && mClaw.isExtended()){
                            lastWantedHeight += 4.5;
                        }
                        setHeight(lastWantedHeight);
                        
                        break; 
                    case MANUAL_CONTROL:
                        wantedSpeed = mController.wantedElevatorVelocity();
                        if(isAtBottom && wantedSpeed < 0){
                            setSpeed(0.0);
                        } else {
                            setSpeed(wantedSpeed);
                        }
                        lastWantedHeight = getHeight();
                        break;
                    case POSITION_CONTROL:
                        setHeight(wantedHeight);
                        lastWantedHeight = wantedHeight;
						break;     
                }
            checkLimits();
            mLastState = mCurrentState;
            checkState();
            }
            public void onStop(){}
        });
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Left Output", mWinchLeft.getOutputCurrent());
        SmartDashboard.putNumber("Right Output", mWinchRight.getOutputCurrent());
        SmartDashboard.putNumber("Right Current", powerPanel.getCurrent(2));
        SmartDashboard.putNumber("Left Current", powerPanel.getCurrent(1));
        SmartDashboard.putNumber("Current Height", mEncoder.getPosition());
        SmartDashboard.putNumber("Last Height", lastWantedHeight);
        SmartDashboard.putNumber("Wanted Height", wantedHeight);
        SmartDashboard.putNumber("Current Velocity", mEncoder.getVelocity());
        SmartDashboard.putBoolean("rightBottom", mCarriageBottomSwitch.get());
        SmartDashboard.putBoolean("leftBottom", mStageBottomSwitch.get());
        SmartDashboard.putString("Elevator state", mCurrentState.toString());
        SmartDashboard.putNumber("Speed", wantedSpeed);
    }
}
