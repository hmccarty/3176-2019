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
        streamType.setBoolean(!isTracking);
    }

    public void registerLoop(){
        mLoopMan.addLoop(
            new loop(){
                public void onStart(){
                    CameraServer.getInstance().startAutomaticCapture();
                    mWantedState = state.STREAM_FRONT; 
                    mCurrentState = state.STREAM_FRONT; 
                }
                public void onLoop(){
                    switch(mCurrentState){
                        case TRACK_TARGET:
                            cIsTracking = true; 
                        case STREAM_FRONT:
                            cIsTracking = false;
                            cVisionSide = 0; 
                        case STREAM_BACK: 
                            cIsTracking = false;
                            cVisionSide = 1; 
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
        TRACK_TARGET, 
        TRACK_AND_STREAM, 
        STREAM_FRONT,
        STREAM_BACK
    }
}   