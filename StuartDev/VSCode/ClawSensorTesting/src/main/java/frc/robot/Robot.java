/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
  //I2C_Coms irSensor;
 SPI_Coms arduino;
//  TalonSRX talon1;
 Timer timer1;
 double lastTime;
 int loops;
 CANSparkMax motor;
 CANPIDController controller;
 CANEncoder encoder;
 public static final double wheelRev2motorRev = (54/17) * (48/30);
 public static final double fps2rpm =  12.0 * (1.0/(3.5*Math.PI)) * (48.0/30.0) * (54.0/17.0) * (60.0);
 double lastPosition;

 double wheelRevs;
 double motorRevs;
 double setPosition;

  @Override
  public void robotInit() {
    // irSensor = new I2C_Coms();
    // talon1 = new TalonSRX(44);
    // timer1 = new Timer();
    // timer1.start();
    loops = 0;
    // talon1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
    // arduino = new SPI_Coms();
    motor = new CANSparkMax(1, MotorType.kBrushless);
    controller = motor.getPIDController();
    encoder = motor.getEncoder();
    motor.setSmartCurrentLimit(80);
    wheelRevs = 5;
    lastPosition = 0;
    motorRevs = 0;
    setPosition = 0;
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
    // System.out.println(arduino.getInt());
    // System.out.println(irSensor.getValue());
    //System.out.println(arduino.getInt());
    // if(timer1.get() < 2) {
    //   talon1.set(ControlMode.Position, 4096*5);
    // }
    // else if(timer1.get() >= 2 && timer1.get() < 4) {
    //   talon1.set(ControlMode.Position, 0);
    // }
    // else {
    //   timer1.reset();
    // }
    // talon1.set(ControlMode.Position, 4096);
    // timer1.delay(2);
    // talon1.set(ControlMode.Position, 0);
    // timer1.delay(2);
    // talon1.set(ControlMode.Velocity,-200);
    


    //System.out.println(arduino.getInt());
    
    controller.setReference(2*fps2rpm, ControlType.kVelocity);
    //motor.set(.5);
  }

  @Override
  public void testPeriodic() {
    // if(loops < 150){
    //  // talon1.set(ControlMode.Position, 2048);
    //  motorRevs = wheelRevs*wheelRev2motorRev;
    //  controller.setReference(setPosition, ControlType.kPosition);
    // }
    // // else if(loops >= 150 && loops <300){
    // //  // talon1.set(ControlMode.Position, 0);      
    // // }
    // else{
    //   loops = 0;
    //   setPosition = lastPosition + wheelRevs;
    // }
    // lastPosition = encoder.getPosition();
    // System.out.println("Set Postion: " + setPosition + " | CurrentPosition: " + encoder.getPosition());
    // System.out.println(loops);
    //loops++;
    controller.setReference(240, ControlType.kVelocity);
  //   if(loops < 150){
     // talon1.set(ControlMode.Position, 20000);
  //   }
  //   else if(loops >= 150 && loops <300){
  //     talon1.set(ControlMode.Position, 0);      
  //   }
  //   else{
  //     loops = 0;
  //   }
  //   //System.out.println(loops);
  //   loops++;
  }

  // private double ToDeg(double units) {
	// 	double deg = units * (360.0 / 4096.0);

	// 	/* truncate to 0.1 res */
	// 	deg *= 10;
	// 	deg = (int) deg;
	// 	deg /= 10;

	// 	return deg;
  // }
  
  // private double ToUnits(double degs) {
  //   double unit = degs * (4096.0 / 360.0);
  //   return unit;
  // } 
}

// 50
// 50
// 0
// 0.001
// 3
// 1
// 204000