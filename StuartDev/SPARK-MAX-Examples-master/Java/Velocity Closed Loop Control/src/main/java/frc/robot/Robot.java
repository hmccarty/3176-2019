/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private Joystick m_stick;
  private static final int deviceID1 = 1;
  private static final int deviceID2 = 2;
  private static final int deviceID3 = 3;
  private static final int deviceID4 = 4;
  private CANSparkMax m_motor1, m_motor2, m_motor3, m_motor4;
  private CANPIDController m_pidController1, m_pidController2, m_pidController3, m_pidController4;
  private CANEncoder m_encoder1,m_encoder2,m_encoder3,m_encoder4;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  int loops = 0;

  @Override
  public void robotInit() {
    m_stick = new Joystick(0);

    // initialize motor
    m_motor1 = new CANSparkMax(deviceID1, MotorType.kBrushless);
    m_motor2 = new CANSparkMax(deviceID2, MotorType.kBrushless);
    m_motor3 = new CANSparkMax(deviceID3, MotorType.kBrushless);
    m_motor4 = new CANSparkMax(deviceID4, MotorType.kBrushless);
    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */
    m_motor1.restoreFactoryDefaults();
    m_motor2.restoreFactoryDefaults();
    m_motor3.restoreFactoryDefaults();
    m_motor4.restoreFactoryDefaults();

    /**
     * In order to use PID functionality for a controller, a CANPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax object
     */
    m_pidController1 = m_motor1.getPIDController();
    m_pidController2 = m_motor2.getPIDController();
    m_pidController3 = m_motor3.getPIDController();
    m_pidController4 = m_motor4.getPIDController();

    // Encoder object created to display position values
    m_encoder1 = m_motor1.getEncoder();
    m_encoder2 = m_motor2.getEncoder();
    m_encoder3 = m_motor3.getEncoder();
    m_encoder4 = m_motor4.getEncoder();

    // PID coefficients
    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 2000;

    // set PID coefficients
    m_pidController1.setP(kP);
    m_pidController1.setI(kI);
    m_pidController1.setD(kD);
    m_pidController1.setIZone(kIz);
    m_pidController1.setFF(kFF);
    m_pidController1.setOutputRange(kMinOutput, kMaxOutput);

    m_pidController2.setP(kP);
    m_pidController2.setI(kI);
    m_pidController2.setD(kD);
    m_pidController2.setIZone(kIz);
    m_pidController2.setFF(kFF);
    m_pidController2.setOutputRange(kMinOutput, kMaxOutput);

    m_pidController3.setP(kP);
    m_pidController3.setI(kI);
    m_pidController3.setD(kD);
    m_pidController3.setIZone(kIz);
    m_pidController3.setFF(kFF);
    m_pidController3.setOutputRange(kMinOutput, kMaxOutput);

    m_pidController4.setP(kP);
    m_pidController4.setI(kI);
    m_pidController4.setD(kD);
    m_pidController4.setIZone(kIz);
    m_pidController4.setFF(kFF);
    m_pidController4.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
  }

  @Override
  public void teleopPeriodic() {
    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);

    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) {
      m_pidController1.setP(p);
      m_pidController2.setP(p);
      m_pidController3.setP(p);
      m_pidController4.setP(p);
      kP = p;
    }
    if((i != kI)) {
      m_pidController1.setI(i);
      m_pidController2.setI(i);
      m_pidController3.setI(i);
      m_pidController4.setI(i);
      kI = i;
    }
    if((d != kD)) {
      m_pidController1.setD(d);
      m_pidController2.setD(d);
      m_pidController3.setD(d);
      m_pidController4.setD(d);
      kD = d;
    }
    if((iz != kIz)) {
      m_pidController1.setIZone(iz);
      m_pidController2.setIZone(iz);
      m_pidController3.setIZone(iz);
      m_pidController4.setIZone(iz);
      kIz = iz;
    }
    if((ff != kFF)) {
      m_pidController1.setFF(ff);
      m_pidController2.setFF(ff);
      m_pidController3.setFF(ff);
      m_pidController4.setFF(ff);
      kFF = ff;
    }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      m_pidController1.setOutputRange(min, max); 
      m_pidController2.setOutputRange(min, max); 
      m_pidController3.setOutputRange(min, max); 
      m_pidController4.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    /**
     * PIDController objects are commanded to a set point using the 
     * SetReference() method.
     * 
     * The first parameter is the value of the set point, whose units vary
     * depending on the control type set in the second parameter.
     * 
     * The second parameter is the control type can be set to one of four 
     * parameters:
     *  com.revrobotics.ControlType.kDutyCycle
     *  com.revrobotics.ControlType.kPosition
     *  com.revrobotics.ControlType.kVelocity
     *  com.revrobotics.ControlType.kVoltage
     */
    double setPoint = 0;
    if(Math.abs(m_stick.getY())<.25){
      setPoint = 0;
    }
    else {
      setPoint = m_stick.getY()*maxRPM;
    }      
    m_pidController1.setReference(setPoint, ControlType.kVelocity);
    m_pidController2.setReference(setPoint, ControlType.kVelocity);
    m_pidController3.setReference(setPoint, ControlType.kVelocity);
    m_pidController4.setReference(setPoint, ControlType.kVelocity);
    
    // if(loops < 200){
    //   setPoint = 2500;
    // }
    // else if (loops >= 200 && loops < 400){
    //   setPoint = 1000;
    // }
    // else{
    //   loops = 0;
    // }

    // m_pidController.setReference(setPoint, ControlType.kVelocity);
    // loops++;
    SmartDashboard.putNumber("SetPoint", setPoint);
    SmartDashboard.putNumber("ProcessVariable", m_encoder1.getVelocity());
  }
}
