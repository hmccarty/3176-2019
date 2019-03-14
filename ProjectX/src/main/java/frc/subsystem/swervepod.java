package frc.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.util.subsystem; 
import frc.robot.constants; 
import frc.subsystem.controller; 

/**
 * Handles commands on the Talon Level. Ensuring that each pod is being
 * given the correct commands in the most effecient manner. 
 */
// public class swervepod extends subsystem {
// 	private TalonSRX driveMotor;
// 	private TalonSRX steerMotor;
// 	private int id;

// 	private double PI = Math.PI;
// 	private double kEncoderUnits = constants.ENCODER_UNITS; //# of ticks on Mag Encoder
// 	private double kConstants[] = constants.OFFSETS;
	
// 	private double lastEncoderPosition; //Previous position in encoder units	
// 	private double encoderError; //Error in encoder units
// 	private double encoderPosition; //Current position in encoder units
// 	//private double encoderSetpoint; //Position wanted in encoder units
// 	private double driveCommand;
	
// 	private double radianError; //Error in radians
// 	private double radianPosition; //Current position in radian units
	
// 	private double velocitySetpoint; //Wanted velocity in ft/s
// 	private double fps2ups = constants.fps2ups; //Converts Feet/s to Encoder Units (770.24)

// 	controller mController;
	
// 	swervepod(int id,TalonSRX driveMotor,TalonSRX steerMotor) {
// 		this.id = id;
// 		mController = controller.getInstance();
// 		this.driveMotor = driveMotor;
// 		this.steerMotor = steerMotor;
// 		this.driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
// 		this.steerMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
// 		//this.steerMotor.setSelectedSensorPosition(0,0,0);
// 		this.steerMotor.config_kP(0, constants.SWERVE_KP, 0);
// 		this.steerMotor.config_kI(0, constants.SWERVE_KI, 0);
// 		this.steerMotor.config_kD(0, constants.SWERVE_KD, 0);
		
		
// 		this.driveMotor.config_kP(0, constants.DRIVE_KP, 0);
// 		this.driveMotor.config_kI(0, constants.DRIVE_KI, 0);
// 		this.driveMotor.config_kD(0, constants.DRIVE_KD, 0);
// 		this.driveMotor.config_kF(0, constants.DRIVE_KF,0);
// 		this.driveMotor.config_IntegralZone(0, constants.DRIVE_IZONE, 0);
// 		this.driveMotor.configClosedloopRamp(constants.DRIVE_RAMPRATE, 0);
		
// 		this.steerMotor.configAllowableClosedloopError(0, constants.SWERVE_ALLOWABLE_ERROR, 0);
// 		this.steerMotor.configAllowableClosedloopError(0, constants.DRIVE_ALLOWABLE_ERROR, 0);
// 	}
	
// 	/**
// 	 * Commands the Talons to give the wanted wheel values
// 	 * @param Speed Velocity value from 0 - 13 ft/s
// 	 * @param Angle Position value from 0 - 2pi
// 	 */
// 	public void setPod(double Speed, double Angle) {
// 		velocitySetpoint  = Speed * fps2ups;
// 		double encoderSetpoint = findSteerPosition(Angle);
		
// 		if(Speed != 0.0) {
// 			steerMotor.set(ControlMode.Position, encoderSetpoint);
// 			lastEncoderPosition = encoderSetpoint;
// 		}
// 		else {
// 			steerMotor.set(ControlMode.Position, lastEncoderPosition);
// 		}
// 		driveMotor.set(ControlMode.Velocity, velocitySetpoint);
// 		outputToSmartDashboard();
// 	}
	
// 	/**
// 	 * Finds the shortest path to the given angle, converts it into a encoder position, and determines when to reverse drive direction.
// 	 * @param wantedAngle Position wanted in radians
// 	 * @return Encoder position needed to move to
// 	 * @see {@link Swervepod#radianToEncoderUnits(double Angle) radianToEncoderUnits()} </p>
// 	 *		{@link Swervepod#encoderUnitsToRadian(double EncoderUnits) encoderUnitsToRadian()}
// 	 */
// 	private double findSteerPosition(double wantedAngle) {
// 		encoderPosition = steerMotor.getSelectedSensorPosition(0) - kConstants[id];
// 		radianPosition = encoderUnitsToRadian(encoderPosition);
// 		radianError = wantedAngle - radianPosition;
// 		if(Math.abs(radianError) > (3*PI/2)) {
// 			radianError -= Math.copySign(2*PI, radianError);
// 		}
// 		else if (Math.abs(radianError) > (PI/2)) {
// 			radianError -= Math.copySign(PI, radianError);
// 			velocitySetpoint = -velocitySetpoint;
// 		}
// 		encoderError = radianToEncoderUnits(radianError);
// 		driveCommand = encoderError + encoderPosition + kConstants[id];
// 		return (driveCommand);
// 	}
	
// 	/**
// 	 * @return Whether pod is still driving
// 	 */
// 	public boolean isStopped() {
// 		if(Math.abs(driveMotor.getSelectedSensorVelocity(0))<300)
// 		{
// 			return true;
// 		}
// 		else
// 		{
// 			return false;
// 		}
// 	}
	
// 	/**
// 	 * @param Angle The radian position to be converted
// 	 * @return Given position in encoder units
// 	 */
// 	private double radianToEncoderUnits(double Angle) {
// 		double encoderUnits = ((Angle / (2.0*Math.PI)) * kEncoderUnits);
// 		return encoderUnits;
// 	}
	
// 	/**
// 	 * @param EncoderUnits The encoder position to be converted
// 	 * @return Given position in radians
// 	 */
// 	private double encoderUnitsToRadian(double EncoderUnits) {
// 		EncoderUnits = EncoderUnits % kEncoderUnits;
// 		if(EncoderUnits < 0) {
// 			EncoderUnits+= kEncoderUnits;
// 		}
// 		EncoderUnits -= (kEncoderUnits/2.0);
// 		double Angle = (EncoderUnits/kEncoderUnits) * (2 * Math.PI);
// 		return Angle;
// 	}
	
// 	/**
// 	 * @return Encoder position in absolute encoder ticks
// 	 */
// 	public double getPosition() {return (steerMotor.getSelectedSensorPosition(0));}
	
// 	/**
// 	 * @return Encoder units traveled total
// 	 */
// 	public double getWheelDisplacment() {return(driveMotor.getSelectedSensorPosition(0));}
	
// 	/**
// 	 * @return Encoder units per second
// 	 */
// 	public double getWheelSpeed() {return (driveMotor.getSelectedSensorVelocity(0));}
	
// 	/**
// 	 * @return Position error in radians
// 	 */
// 	public double getPhi() {return radianError;}
	
// 	/**
// 	 * @return Position error in encoder ticks
// 	 */
// 	public double getFinal() {return encoderError;}
	
// 	/**
// 	 * @return Position of wheel in encoder ticks
// 	 */
// 	public double getCur() {return encoderPosition;}
	
// 	/** 
// 	 * @return Wanted velocity of the wheels
// 	 */
// 	public double getSpeed() {return velocitySetpoint;}
	
// 	/**
// 	 * @return Speed of the wheel in Encoder Ticks/100 Ms
// 	 */
// 	public double getRawSpeed() {return driveMotor.getSelectedSensorVelocity(0);}
	
// 	@Override public void zeroAllSensors() {
// 		steerMotor.set(ControlMode.Position, 0.0);
// 		driveMotor.set(ControlMode.Velocity, 0.0);
// 	}
	
// 	@Override public void registerLoop() {/*NA*/} //Not being used

// 	@Override public void outputToSmartDashboard() {
// 		SmartDashboard.putNumber("Pod " + id + "'s Encoder Position", encoderPosition);
// 		SmartDashboard.putNumber("Pod " + id + "'s velocity", driveMotor.getSelectedSensorVelocity(0));
// 	}
// }