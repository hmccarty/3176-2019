/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;

public class Robot extends IterativeRobot {
  private CANSparkMax motor;
  private CANEncoder encoder;
  private CANPIDController controller;
  private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
  private Timer timer;

  @Override
  public void robotInit() {
    motor = new CANSparkMax(33, MotorType.kBrushless);
    encoder = motor.getEncoder();
    controller = motor.getPIDController();
    timer = new Timer();

    motor.restoreFactoryDefaults();

    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0.000156; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 5700;

    controller.setP(kP);
    controller.setI(kI);
    controller.setD(kD);
    controller.setIZone(kIz);
    controller.setFF(kFF);
    controller.setOutputRange(kMinOutput, kMaxOutput);

    int smartMotionSlot = 0;
    controller.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    controller.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    controller.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    controller.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
    SmartDashboard.putNumber("Max Velocity", maxVel);
    SmartDashboard.putNumber("Min Velocity", minVel);
    SmartDashboard.putNumber("Max Acceleration", maxAcc);
    SmartDashboard.putNumber("Allowed Closed Loop Error", allowedErr);
    SmartDashboard.putNumber("Set Position", 0);
    SmartDashboard.putNumber("Set Velocity", 0);
    SmartDashboard.putNumber("Set Time Interval", 1);
    SmartDashboard.putNumber("Set Low Position", 0);
    SmartDashboard.putNumber("Set High Position", 100);

    SmartDashboard.putBoolean("mode", false);
    SmartDashboard.putBoolean("Square Wave", false);
    SmartDashboard.putBoolean("Velocity Override", true);

    timer.start();
  }
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    double maxV = SmartDashboard.getNumber("Max Velocity", 0);
    double minV = SmartDashboard.getNumber("Min Velocity", 0);
    double maxA = SmartDashboard.getNumber("Max Acceleration", 0);
    double allE = SmartDashboard.getNumber("Allowed Closed Loop Error", 0);
    double minP = SmartDashboard.getNumber("Set Low Position", 0);
    double maxP = SmartDashboard.getNumber("Set High Position", 0);
    double time = SmartDashboard.getNumber("Set Time Interval", 0);

    if((p != kP)) { controller.setP(p); kP = p; }
    if((i != kI)) { controller.setI(i); kI = i; }
    if((d != kD)) { controller.setD(d); kD = d; }
    if((iz != kIz)) { controller.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { controller.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      controller.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }
    if((maxV != maxVel)) { controller.setSmartMotionMaxVelocity(maxV,0); maxVel = maxV; }
    if((minV != minVel)) { controller.setSmartMotionMaxVelocity(minV,0); minVel = minV; }
    if((maxA != maxAcc)) { controller.setSmartMotionMaxAccel(maxA,0); maxAcc = maxA; }
    if((allE != allowedErr)) { controller.setSmartMotionAllowedClosedLoopError(allE,0); allE = allowedErr; }

    double setPoint, processVariable;
    boolean mode = SmartDashboard.getBoolean("Mode", false);
    boolean wave = SmartDashboard.getBoolean("Square Wave", false);
    boolean override = SmartDashboard.getBoolean("Velocity Override", true);
    if(mode) {
      if(override){
        setPoint = 0;
      }
      else {
        setPoint = SmartDashboard.getNumber("Set Velocity", 0);
      }
      controller.setReference(setPoint, ControlType.kVelocity);
      processVariable = encoder.getVelocity();
    }else {
      setPoint = SmartDashboard.getNumber("Set Position", 0);
      if(wave) {
        if(timer.get() <= time) {
          controller.setReference(minP, ControlType.kSmartMotion);
        }
        else if(timer.get() > time) {
          controller.setReference(maxP, ControlType.kSmartMotion);
        }
        else if(timer.get() >= 2*time) {
          timer.reset();
        }
      }
      else{
        controller.setReference(setPoint, ControlType.kSmartMotion);
      }
      processVariable = encoder.getPosition();
    }
  
  //   if(!wave) {
  //     controller.setReference(0, ControlType.kSmartVelocity);
  //   }
  //   else{
  //     if(timer.get() < time) {
  //       controller.setReference(minP, ControlType.kSmartMotion);
  //     }
  //     else if(timer.get() > time && timer.get() < time*2) {
  //       controller.setReference(maxP, ControlType.kSmartMotion);
  //     }
  //     else {
  //       timer.reset();
  //     }
  //   }
    processVariable = encoder.getPosition();

    SmartDashboard.putNumber("Process Variable", processVariable);
    SmartDashboard.putNumber("Set Point", setPoint);
    SmartDashboard.putNumber("Output", motor.getAppliedOutput());
  }

  @Override
  public void testPeriodic() {
  }
}
