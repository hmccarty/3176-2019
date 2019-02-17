/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
  //I2C_Coms irSensor;
 // SPI_Coms arduino;
 TalonSRX talon1;

  @Override
  public void robotInit() {
    //irSensor = new I2C_Coms();
    talon1 = new TalonSRX(3);
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
   // System.out.println(irSensor.getValue());
    //System.out.println(arduino.getInt());\
    talon1.set(ControlMode.PercentOutput,.5);
  }

  @Override
  public void testPeriodic() {
  }
}
