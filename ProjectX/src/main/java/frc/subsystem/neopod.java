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

public class neopod extends subsystem {
	private CANSparkMax driveMotor;
	private CANPIDController driveController;
    private CANEncoder driveEncoder;
	private TalonSRX spinMotor;
	private int id;

	private double PI = Math.PI;
	private double kEncoderUnits = constants.ENCODER_UNITS; //# of ticks on Mag Encoder
	private double kAbsoluteOffsets[] = constants.OFFSETS;
	private double gearRatio = constants.DRIVE_GEAR_RATI0;
	private double fps2rpm = constants.FPS_TO_RPM;
	
	private double lastEncoderPosition; //Previous position in encoder units	
	private double encoderError; //Error in encoder units
	private double encoderPosition; //Current position in encoder units
	private double driveCommand;

	private double encoderSetpoint;
	
	private double radianError; //Error in radians
	private double radianPosition; //Current position in radian units
	
	private double velocitySetpoint; //Wanted velocity in ft/s

	controller mController = controller.getInstance();
	
    neopod(int id, CANSparkMax driveMotor, TalonSRX spinMotor) {
		this.id = id;

		this.driveMotor = driveMotor;
		driveController = driveMotor.getPIDController();
		driveEncoder = this.driveMotor.getEncoder();

        driveController.setP(constants.DRIVE_PID_CONFIG[0]);
        driveController.setI(constants.DRIVE_PID_CONFIG[1]);
        driveController.setD(constants.DRIVE_PID_CONFIG[2]);
        driveController.setFF(constants.DRIVE_PID_CONFIG[3]);
		driveController.setIZone(constants.DRIVE_PID_CONFIG[4]);

		driveMotor.setSmartCurrentLimit(constants.DRIVE_CURRENT_LIMIT);
		driveMotor.setClosedLoopRampRate(constants.DRIVE_RAMP_RATE);

		this.spinMotor = spinMotor;
		this.spinMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);

		this.spinMotor.config_kP(0, constants.SPIN_PID_CONFIG[0][id], 0);
		this.spinMotor.config_kI(0, constants.SPIN_PID_CONFIG[1][id], 0);
		this.spinMotor.config_kD(0, constants.SPIN_PID_CONFIG[2][id], 0);
		this.spinMotor.config_kF(0, constants.SPIN_PID_CONFIG[3][id], 0);
	}
	
	/**
	 * Commands the Talons to give the wanted wheel values
	 * @param Speed Velocity value from 0 - 13 ft/s
	 * @param Angle Position value from 0 - 2pi
	 */
	public void setPod(double Speed, double Angle) {
		velocitySetpoint  = Speed * fps2rpm;
		encoderSetpoint = findSteerPosition(Angle);
		
		if(Speed != 0.0) {
            spinMotor.set(ControlMode.Position, encoderSetpoint);
			lastEncoderPosition = encoderSetpoint;
		}
		else {
            spinMotor.set(ControlMode.Position, lastEncoderPosition);
		}
        driveController.setReference(velocitySetpoint, ControlType.kVelocity);
	}
	
	/**
	 * Finds the shortest path to the given angle, converts it into a encoder position, and determines when to reverse drive direction.
	 * @param wantedAngle Position wanted in radians
	 * @return Encoder position needed to move to
	 * @see {@link Swervepod#radianToEncoderUnits(double Angle) radianToEncoderUnits()} </p>
	 *		{@link Swervepod#encoderUnitsToRadian(double EncoderUnits) encoderUnitsToRadian()}
	 */
	private double findSteerPosition(double wantedAngle) {
        encoderPosition = spinMotor.getSelectedSensorPosition(0) - kAbsoluteOffsets[id];
		radianPosition = encoderUnitsToRadian(encoderPosition);
		radianError = wantedAngle - radianPosition;
		if(Math.abs(radianError) > (5*PI/2)){
			System.out.println("Error Overload");
		}
		else if(Math.abs(radianError) > (3*PI/2)) {
			radianError -= Math.copySign(2*PI, radianError);
		}
		else if (Math.abs(radianError) > (PI/2)) {
			radianError -= Math.copySign(PI, radianError);
			velocitySetpoint = -velocitySetpoint;
		}
		encoderError = radianToEncoderUnits(radianError);
		driveCommand = encoderError + encoderPosition + kAbsoluteOffsets[id];
		return (driveCommand);
	}
	
	/**
	 * @return Whether pod is still driving
	 */
	public boolean isStopped() {
		if(Math.abs(driveEncoder.getVelocity()) * gearRatio < 300) {
			return true;
		} else {
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
	public double getPosition() {return (spinMotor.getSelectedSensorPosition(0));}
	
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
		spinMotor.set(ControlMode.Position, 0.0);
		driveController.setReference(0, ControlType.kVelocity);
	}
	
	@Override public void registerLoop() {/*NA*/}

	@Override public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Pod " + id + "'s Encoder Position", spinMotor.getSelectedSensorPosition());
		SmartDashboard.putNumber("Pod " + id + "'s Wanted Position", encoderSetpoint );
		SmartDashboard.putNumber("Pod " + id + "'s motor velocity", driveEncoder.getVelocity());
		SmartDashboard.putNumber("Pod " + id + "'s velocity setpoint", velocitySetpoint);
		SmartDashboard.putNumber("Pod " + id + "'s kP", driveController.getP());
		SmartDashboard.putNumber("Pod " + id + "'s kI", driveController.getI());
		SmartDashboard.putNumber("Pod " + id + "'s kD", driveController.getD());
		SmartDashboard.putNumber("Pod " + id + "'s current", driveMotor.getOutputCurrent());
	}
}