/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;


public class Robot extends IterativeRobot {
  private Joystick stick;
  private CANSparkMax motor;
  private CANEncoder encoder;
  
  @Override
  public void robotInit() {
    stick = new Joystick(1);
    motor = new CANSparkMax(0, MotorType.kBrushless);
    encoder = motor.getEncoder();

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
    if (Math.abs(stick.getY()) < .06){
      motor.set(0);
    }
    else{
      motor.set(stick.getY());
    }
    System.out.println(encoder.getPosition());
  }

  @Override
  public void testPeriodic() {
  }
}
