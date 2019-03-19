/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
  CANSparkMax motor;
  CANSparkMax motor1;
  Joystick stick;

  @Override
  public void robotInit() {
    motor = new CANSparkMax(5, MotorType.kBrushless);
    motor1 = new CANSparkMax(4, MotorType.kBrushless);
    stick = new Joystick(0);
    motor.setSmartCurrentLimit(40);
    motor1.setSmartCurrentLimit(40);
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
    motor.set(stick.getY()*.7);
    motor1.follow(motor, true);
    System.out.println(motor.getOutputCurrent());
  }

  @Override
  public void testPeriodic() {
  }
}
