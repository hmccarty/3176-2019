package frc.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.subsystem.controller;
import frc.robot.constants;
import frc.util.*;
import frc.util.trajectory;

public class elevator {
    private static elevator instance = new elevator();
    private controller mController = controller.getInstance();

    private CANSparkMax mWinch;
    private CANPIDController mPIDController;
    private CANEncoder mEncoder; 

    private trajectory mTrajectory;

    private double cWantedFloor;
    private double cLiftSpeed;
    private double cTrajectoryStartTime;

    private enum state {
        NEUTRAL,
        LEVEL_FOLLOW,
        OPEN_LOOP,
        MOTION_PROFILE
    }

    private state mCurrentState;
    private state mWantedState;

    public elevator() {
        mWinch = new CANSparkMax(constants.ELEVATOR, MotorType.kBrushless);
        mEncoder = mWinch.getEncoder();
        mPIDController = mWinch.getPIDController();

        mPIDController.setP(constants.ELEVATOR_KP);
        mPIDController.setI(constants.ELEVATOR_KI);
        mPIDController.setD(constants.ELEVATOR_KD);
    }

    public static elevator getInstance() {
        return instance;
    }

    private void setLevel(double wantedHeight) {
        mPIDController.setReference(wantedHeight, ControlType.kSmartMotion); 
    }

    public void setWantedFloor(double wF) {
        this.cWantedFloor = wF;
    }

    public double getHeight() {
        return mEncoder.getPosition();
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
                mCurrentState = state.NEUTRAL;
                mWantedState = state.NEUTRAL;
            }

            @Override
            public void onLoop() {
                switch(mCurrentState) {
                    case NEUTRAL:
                        setLevel(getHeight());
                        break;
                    case OPEN_LOOP:
                        //motor.set(joystick.elevatorPosition());
                        break;
                    case LEVEL_FOLLOW:
                        checkState();
                        // if(Math.abs(wantedFloor - getHeight()) > /*some distance away before switching to PID control*/){
                        //     currentState = systemStates.MOTION_PROFILE;
                        // } else {
                        //     setFloor(wantedFloor);
                        // }
                        break;
                    case MOTION_PROFILE:
                        // if(lastState != systemStates.MOTION_PROFILE) {
                        //     if (getHeight()<wantedFloor){
                        //         motionProfileTrajectory = new trajectory(/*velocity and acceleration to be determined*/);
                        //     } else {
                        //         motionProfileTrajectory = new trajectory(/*velocity and acceleration TBD*/);
                        //     }
                        //     motionProfileTrajectory.addWaypoint(new Waypoint(getHeight(),0.0,0.0));
						// 	motionProfileTrajectory.addWaypoint(new Waypoint(wantedFloor,0.0,0.0));
						// 	motionProfileTrajectory.calculateTrajectory();
						// 	motionProfileStartTime = Timer.getFPGATimestamp();
                        // }   else if(Timer.getFPGATimestamp()-motionProfileStartTime<motionProfileTrajectory.getTimeToComplete()) {
						// 	setFloor(motionProfileTrajectory.getPosition(Timer.getFPGATimestamp()-motionProfileStartTime));
						// } else {
						// 	currentState = systemStates.POSITION_FOLLOW;
						// }
						// lastState = systemStates.MOTION_PROFILE;
						// break;
                        
                }
            checkState();
            }
            public void onStop(){}
        });
    }
}
/* 
switch case between setPosition/openLoop/PIDLoop
        setLevel
            gets level from controller and PID controls to that level
        openLoop
            gets velocity from joystick
        PIDLoop get ticks from controller than adjusts speed to match joystick through PIDLoop
            Similar to openLoop but more gradual
*/