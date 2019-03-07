package frc.subsystem; 

import frc.util.*; 
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.*;

public class vision extends subsystem{
    private static vision instance = new vision();
    loopmanager mLoopMan = loopmanager.getInstance();

    NetworkTableInstance inst = NetworkTableInstance.getDefault(); 
    NetworkTable table = inst.getTable("SmartDashboard");

    NetworkTableEntry distanceToTarget = table.getEntry("distance");
    NetworkTableEntry angleToTarget = table.getEntry("angle");
    NetworkTableEntry whichCamera = table.getEntry("WhichCamera");
    NetworkTableEntry streamType = table.getEntry("Driver");

    private state mWantedState; 
    private state mCurrentState;

    private int cVisionSide = 0; 
    private boolean cIsTracking = false; 

    public vision(){}

    public static vision getInstance(){
        return instance; 
    }

    public double getDistance(){
        return distanceToTarget.getDouble(-1);
    }

    public double getAngle(){
        return angleToTarget.getDouble(-1); 
    }

    public void setWantedState(state wantedState){
        mWantedState = wantedState; 
    }

    public void checkState(){
        if(mWantedState != mCurrentState){
            mCurrentState = mWantedState; 
        }
    }

    public void postToNetwork(int visionSide, boolean isTracking){
        whichCamera.setDouble(visionSide);
        streamType.setBoolean(false);
    }

    public void registerLoop(){
        mLoopMan.addLoop(
            new loop(){
                public void onStart(){
                    CameraServer.getInstance().startAutomaticCapture();
                    mWantedState = state.NEUTRAL; 
                    mCurrentState = state.NEUTRAL; 
                }
                public void onLoop(){
                    switch(mCurrentState){
                        case SWITCH_MODE:
                            cIsTracking = !cIsTracking; 
                            mWantedState = state.NEUTRAL; 
                            break;
                        case SWITCH_CAMERA:
                            if(cVisionSide == 0) {cVisionSide = 1;}
                            else { cVisionSide = 0;}
                            mWantedState = state.NEUTRAL; 
                            break;
                        case NEUTRAL: 
                            break; 
                    }
                    postToNetwork(cVisionSide, cIsTracking);
                    checkState(); 
                }
                public void onStop(){

                }
            }
        );
    }

    public void zeroAllSensors(){}
    public void outputToSmartDashboard(){}

    public enum state {
        SWITCH_MODE,
        SWITCH_CAMERA,
        NEUTRAL
    }
}   