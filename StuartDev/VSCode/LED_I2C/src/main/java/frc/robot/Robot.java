package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
  private I2C arduino;
  private byte[] toSend = new byte[1];
  private byte[] emptyArray = new byte[1];
  private Joystick stick;


  @Override
  public void robotInit() {
    stick = new Joystick(0);
    arduino = new I2C(I2C.Port.kOnboard, 9);
   
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
    if (stick.getRawButton(1)){
      toSend[0] = (byte) 1;
    }
    if (stick.getRawButton(2)){
      toSend[0] = (byte) 2;
    }
    if (stick.getRawButton(3)){
      toSend[0] = (byte) 3;
    }
    System.out.println(toSend);
    arduino.transaction(toSend, 1, emptyArray, 1);
  }

  @Override
  public void testPeriodic() {
  }
}
