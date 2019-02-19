/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
  //I2C_Coms irSensor;
 SPI_Coms arduino;
 TalonSRX talon1;
 Timer timer1;
 double lastTime;
 int loops;

  @Override
  public void robotInit() {
    // irSensor = new I2C_Coms();
    // talon1 = new TalonSRX(22);
    // timer1 = new Timer();
    // timer1.start();
    // loops = 0;
    // talon1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
    arduino = new SPI_Coms();
    
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
    System.out.println(arduino.getInt());
  }

  @Override
  public void testPeriodic() {
    // if(loops < 150){
    //   talon1.set(ControlMode.Position, 1024);
    // }
    // else if(loops >= 150 && loops <300){
    //   talon1.set(ControlMode.Position, 0);      
    // }
    // else{
    //   loops = 0;
    // }
    // System.out.println(loops);
    // loops++;
  }
}
