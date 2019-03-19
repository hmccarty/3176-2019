package frc.util; 

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class sparkconfig {
    private CANPIDController mPIDController; 
    private CANSparkMax mSparkMax;
    private int CANID; 

    public sparkconfig(CANPIDController PIDController, CANSparkMax SparkMax, int CANID) {
        this.mPIDController = PIDController; 
        this.mSparkMax = SparkMax;
        this.CANID = CANID; 
    }

    public void configPID(double[] PIDValues) {
        try {
            mPIDController.setP(PIDValues[0]);
            mPIDController.setI(PIDValues[1]);
            mPIDController.setD(PIDValues[2]);
            mPIDController.setOutputRange(PIDValues[3], PIDValues[4]);
            mPIDController.setIZone(PIDValues[5]);
            mPIDController.setFF(PIDValues[6]);
        }
        catch (Exception e) {
            System.out.println("Spark Controller " + CANID + " could not be configured. Please check PID values.");
        }
    }

    public void configSmartMotion(double[] SmartMotionValues) {
        try {
            mPIDController.setSmartMotionMaxVelocity(SmartMotionValues[0], 0);
            mPIDController.setSmartMotionMinOutputVelocity(SmartMotionValues[1], 0);
            mPIDController.setSmartMotionMaxAccel(SmartMotionValues[2], 0);
            mPIDController.setSmartMotionAllowedClosedLoopError(SmartMotionValues[3], 0);
        }
        catch (Exception e) {
            System.out.println("Spark Controller " + CANID + " could not be configured. Please check Smart Motion values.");
        }
    }

    public void configCurrentLimit(int CurrentLimit) {
        try {
            mSparkMax.setSmartCurrentLimit(CurrentLimit);
        }
        catch (Exception e) {
            System.out.println("Spark Controller " + CANID + " could not be configured. Please check Smart Current Limit value.");
        }
    }
}