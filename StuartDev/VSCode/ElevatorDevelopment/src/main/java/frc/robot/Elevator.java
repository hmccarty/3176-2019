package frc.robot;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
    private static Elevator instance = new Elevator();
    private CANSparkMax motor;
    private CANEncoder neoEncoder;
    private Controller joystick = Controller.getInstance();
    private PIDLoop elevatorControlLoop;
    private double wantedFloor;
    private double liftSpeed;
    private Trajectory1D motionProfileTrajectory;
    private double motionProfileStartTime;

    private enum systemStates {
        NEUTRAL,
        LEVEL_FOLLOW,
        OPEN_LOOP,
        MOTION_PROFILE
    }

    private systemStates currentState;
    private systemStates wantedState;
    private systemStates lastState;

    public Elevator() {
        motor = new CANSparkMax(Constants.ELEVATORMOTOR, MotorType.kBrushless);
        neoEncoder = motor.getEncoder();
        elevatorControlLoop = new PIDLoop(Constants.ELEVATOR_KP,
                                            Constants.ELEVATOR_KI,
                                            Constants.ELEVATOR_KD,
                                            1);
    }

    public static Elevator getInstance() {
        return instance;
    }

    private void setLevel(double wantedHeight) {
        liftSpeed = elevatorControlLoop.returnOutput(neoEncoder.getPosition(), wantedHeight);
        motor.set(liftSpeed);
    }

    public void setWantedFloor(double wF) {
        this.wantedFloor = wF;
    }

    public double getHeight() {
        return neoEncoder.getPosition();
    }

    public void setWantedState(systemStates wantedState) {
        this.wantedState = wantedState;
    }

    public systemStates getState() {
        return currentState;
    }

    public void checkState() {
        if (currentState != wantedState) {
            currentState = wantedState;
        }
    }

    public void registerLoop() {
        Loop_Manager.getInstance().addLoop(new Loop()
        {
            @Override
            public void onStart() {
                currentState = systemStates.NEUTRAL;
                wantedState = systemStates.NEUTRAL;
            }

            @Override
            public void onLoop() {
                switch(currentState) {
                    case NEUTRAL:
                        motor.set(0.0);
                        checkState();
                        lastState = systemStates.NEUTRAL;
                        break;
                    case OPEN_LOOP:
                        motor.set(joystick.elevatorPosition());
                        checkState();
                        lastState = systemStates.OPEN_LOOP;
                        break;
                    case LEVEL_FOLLOW:
                        checkState();
                        if(Math.abs(wantedFloor - getHeight()) > /*some distance away before switching to PID control*/){
                            currentState = systemStates.MOTION_PROFILE;
                        } else {
                            setFloor(wantedFloor);
                        }
                        lastState = systemStates.LEVEL_FOLLOW;
                        break;
                    case MOTION_PROFILE:
                        if(lastState != systemStates.MOTION_PROFILE) {
                            if (getHeight()<wantedFloor){
                                motionProfileTrajectory = new Trajectory1D(/*velocity and acceleration to be determined*/);
                            } else {
                                motionProfileTrajectory = new Trajectory1D(/*velocity and acceleration TBD*/);
                            }
                            motionProfileTrajectory.addWaypoint(new Waypoint(getHeight(),0.0,0.0));
							motionProfileTrajectory.addWaypoint(new Waypoint(wantedFloor,0.0,0.0));
							motionProfileTrajectory.calculateTrajectory();
							motionProfileStartTime = Timer.getFPGATimestamp();
                        }   else if(Timer.getFPGATimestamp()-motionProfileStartTime<motionProfileTrajectory.getTimeToComplete()) {
							setFloor(motionProfileTrajectory.getPosition(Timer.getFPGATimestamp()-motionProfileStartTime));
						} else {
							currentState = systemStates.POSITION_FOLLOW;
						}
						lastState = systemStates.MOTION_PROFILE;
						break;
                        
                }
            }
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