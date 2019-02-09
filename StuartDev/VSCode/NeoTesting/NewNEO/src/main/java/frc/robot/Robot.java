package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder; 
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends IterativeRobot {
  private Joystick stick;
  private CANSparkMax motor;
  CANPIDController c; 
  CANEncoder e; 
  double kp, ki, kd, maxOutput, maxRM, minOutput; 
  
  @Override
  public void robotInit() {
    stick = new Joystick(0);
    motor = new CANSparkMax(0, MotorType.kBrushless);
    c = motor.getPIDController();
    e = motor.getEncoder();
    kp = 4e-5; 
    ki = 1e-6;
    kd = 0; 
    maxOutput = 1;
    maxRM = 5700;
    minOutput = -1; 
    c.setP(kp);
    c.setI(ki);
    c.setD(kd);
    c.setOutputRange(minOutput, maxOutput);
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
    if(Math.abs(stick.getY())>.09){
    c.setReference(stick.getY()*maxRM, ControlType.kVelocity);
    } else {
      c.setReference(0, ControlType.kVelocity);
    }

  }

  @Override
  public void testPeriodic() {
  }
}
