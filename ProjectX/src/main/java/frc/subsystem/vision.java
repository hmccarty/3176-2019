package frc.subsystem; 

import frc.util.*; 
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.*;

public class vision extends subsystem{
    private static vision instance = new vision();
    loopmanager mLoopMan = loopmanager.getInstance();

    NetworkTableInstance inst = NetworkTableInstance.getDefault(); 
    NetworkTable table = inst.getTable("SmartDashboard");

    NetworkTableEntry distanceToTarget = table.getEntry("distance");
    NetworkTableEntry angleToTarget = table.getEntry("Angle");

    private state mWantedState; 
    private state mCurrentState;

    public vision(){}

    public vision getInstance(){
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
                        case TRACK_AND_STREAM: 
                        case STREAM_FRONT:
                        case STREAM_BACK: 
                    }
                    checkState(); 
                }
                public void onStop(){

                }
            }
        );
    }

    public enum state {
        TRACK_TARGET, 
        TRACK_AND_STREAM, 
        STREAM_FRONT,
        STREAM_BACK
    }
}   