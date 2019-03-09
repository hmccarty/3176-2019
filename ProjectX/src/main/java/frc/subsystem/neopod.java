/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.util.subsystem;
import frc.robot.constants;
import frc.subsystem.controller;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
public class neopod extends subsystem {
    private CANSparkMax driveMotor;
	private TalonSRX steerMotor;
	private int id;

	private double PI = Math.PI;
	private double kEncoderUnits = constants.ENCODER_UNITS; //# of ticks on Mag Encoder
	private double kConstants[] = constants.OFFSETS;
	private double gearRatio = constants.DRIVE_GEAR_RATI0;
	private double fps2ups = constants.fps2ups; //Converts Feet/s to Encoder Units (770.24)
	private double fps2rpm = constants.fps2rpm;
	private double rev2ft = constants.rev2ft;
	
	private double lastEncoderPosition; //Previous position in encoder units	
	private double encoderError; //Error in encoder units
	private double encoderPosition; //Current position in encoder units
	//private double encoderSetpoint; //Position wanted in encoder units
	private double driveCommand;
	
	private double radianError; //Error in radians
	private double radianPosition; //Current position in radian units
	
	private double velocitySetpoint; //Wanted velocity in ft/s

    private CANPIDController m_pidController;
    private CANEncoder driveEncoder;

	controller mController;
	
    neopod(int id, CANSparkMax driveMotor, TalonSRX steerMotor) {
		this.id = id;
		mController = controller.getInstance();
		this.driveMotor = driveMotor;
		this.steerMotor = steerMotor;
		this.steerMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        driveEncoder = this.driveMotor.getEncoder();
        //this.steerMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
		//this.steerMotor.setSelectedSensorPosition(0,0,0);
		this.steerMotor.config_kP(0, constants.SWERVE_KP[id], 0);
		this.steerMotor.config_kI(0, constants.SWERVE_KI[id], 0);
        this.steerMotor.config_kD(0, constants.SWERVE_KD[id], 0);
        
        m_pidController = driveMotor.getPIDController();
        m_pidController.setP(constants.NEO_KP);
        m_pidController.setI(constants.NEO_KI);
        m_pidController.setD(constants.NEO_KD);
        m_pidController.setFF(constants.NEO_FF);
		m_pidController.setIZone(constants.NEO_IZ);
		driveMotor.setSmartCurrentLimit(100);
		m_pidController.setSmartMotionMaxAccel(constants.NEO_MAX_ACCEL,0);
		m_pidController.setSmartMotionMaxVelocity(constants.NEO_MAX_VEL,0);
		driveEncoder.setPosition(0);
		
		// this.driveMotor.config_kP(0, constants.DRIVE_kP, 0);
		// this.driveMotor.config_kI(0, constants.DRIVE_kI, 0);
		// this.driveMotor.config_kD(0, constants.DRIVE_kD, 0);
		// this.driveMotor.config_kF(0, constants.DRIVE_kF,0);
		// this.driveMotor.config_IntegralZone(0, constants.DRIVE_IZONE, 0);
		// this.driveMotor.configClosedloopRamp(constants.DRIVE_RAMPRATE, 0);
        
		//this.steerMotor.configAllowableClosedloopError(0, constants.SWERVE_ALLOWABLE_ERROR, 0);
		//this.steerMotor.configAllowableClosedloopError(0, constants.DRIVE_ALLOWABLE_ERROR, 0);
	}
	
	/**
	 * Commands the Talons to give the wanted wheel values
	 * @param Speed Velocity value from 0 - 13 ft/s
	 * @param Angle Position value from 0 - 2pi
	 */
	public void setPod(double Speed, double Angle) {
		System.out.println("Angle: " + Angle);
		velocitySetpoint  = Speed * fps2rpm;
		double encoderSetpoint = findSteerPosition(Angle);
		
		if(Speed != 0.0) {
            steerMotor.set(ControlMode.Position, encoderSetpoint);
			lastEncoderPosition = encoderSetpoint;
		}
		else {
            steerMotor.set(ControlMode.Position, lastEncoderPosition);
		}
        // driveMotor.set(ControlMode.Velocity, velocitySetpoint);
        m_pidController.setReference(velocitySetpoint, ControlType.kVelocity);
		outputToSmartDashboard();
	}
	
	/**
	 * Finds the shortest path to the given angle, converts it into a encoder position, and determines when to reverse drive direction.
	 * @param wantedAngle Position wanted in radians
	 * @return Encoder position needed to move to
	 * @see {@link Swervepod#radianToEncoderUnits(double Angle) radianToEncoderUnits()} </p>
	 *		{@link Swervepod#encoderUnitsToRadian(double EncoderUnits) encoderUnitsToRadian()}
	 */
	private double findSteerPosition(double wantedAngle) {
        encoderPosition = steerMotor.getSelectedSensorPosition(0) - kConstants[id];
		radianPosition = encoderUnitsToRadian(encoderPosition);
		radianError = wantedAngle - radianPosition;
		if(Math.abs(radianError) > (3*PI/2)) {
			radianError -= Math.copySign(2*PI, radianError);
		}
		else if (Math.abs(radianError) > (PI/2)) {
			radianError -= Math.copySign(PI, radianError);
			velocitySetpoint = -velocitySetpoint;
		}
		encoderError = radianToEncoderUnits(radianError);
		driveCommand = encoderError + encoderPosition + kConstants[id];
		return (driveCommand);
	}
	
	/**
	 * @return Whether pod is still driving
	 */
	public boolean isStopped() {
		if(Math.abs(driveEncoder.getVelocity())*gearRatio<300)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @param Angle The radian position to be converted
	 * @return Given position in encoder units
	 */
	private double radianToEncoderUnits(double Angle) {
		double encoderUnits = ((Angle / (2.0*Math.PI)) * kEncoderUnits);
		return encoderUnits;
	}
	
	/**
	 * @param EncoderUnits The encoder position to be converted
	 * @return Given position in radians
	 */
	private double encoderUnitsToRadian(double EncoderUnits) {
		EncoderUnits = EncoderUnits % kEncoderUnits;
		if(EncoderUnits < 0) {
			EncoderUnits+= kEncoderUnits;
		}
		EncoderUnits -= (kEncoderUnits/2.0);
		double Angle = (EncoderUnits/kEncoderUnits) * (2 * Math.PI);
		return Angle;
	}
	
	/**
	 * @return Encoder position in absolute encoder ticks
	 */
	public double getPosition() {return (steerMotor.getSelectedSensorPosition(0));}
	
	/**
	 * @return Encoder units traveled total
	 */
	public double getWheelDisplacment() {return(driveEncoder.getPosition());}
	
	/**
	 * @return Encoder units per second
	 */
	public double getWheelSpeed() {return (driveEncoder.getPosition());}
	
	/**
	 * @return Position error in radians
	 */
	public double getPhi() {return radianError;}
	
	/**
	 * @return Position error in encoder ticks
	 */
	public double getFinal() {return encoderError;}
	
	/**
	 * @return Position of wheel in encoder ticks
	 */
	public double getCur() {return encoderPosition;}
	
	/** 
	 * @return Wanted velocity of the wheels
	 */
	public double getSpeed() {return velocitySetpoint;}
	
	/**
	 * @return Speed of the wheel in Encoder Ticks/100 Ms
	 */
	public double getRawSpeed() {return driveEncoder.getVelocity();}
	
	@Override public void zeroAllSensors() {
		steerMotor.set(ControlMode.Position, 0.0);
		m_pidController.setReference(0, ControlType.kVelocity);
	}
	
	@Override public void registerLoop() {/*NA*/} //Not being used

	@Override public void outputToSmartDashboard() {
		// SmartDashboard.putNumber("Pod " + id + "'s Encoder Position", driveEncoder.getPosition());
		SmartDashboard.putNumber("Pod " + id + "'s motor velocity", driveEncoder.getVelocity());
		// SmartDashboard.putNumber("Pod " + id + "'s linear feet", driveEncoder.getPosition()*rev2ft);
		// SmartDashboa
		SmartDashboard.putNumber("Pod " + id + "'s linear velocity", driveEncoder.getVelocity() * (1/fps2rpm));
		SmartDashboard.putNumber("Pod " + id + "'s velocity setpoint", velocitySetpoint);
		// // SmartDashboard.putNumber("Pod " + id + "'s kP", m_pidController.getP());
		// SmartDashboard.putNumber("Pod " + id + "'s kI", m_pidController.getI());
		// SmartDashboard.putNumber("Pod " + id + "'s kD", m_pidController.getD());
		// SmartDashboard.putNumber("Pod " + id + "'s current", driveMotor.getOutputCurrent());
	}
}