/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants;

/**
 * Add your docs here.
 */
public class HatchIntake {
    static HatchIntake instance = new HatchIntake();
    Joystick Joy;
    Timer Tim1;
    DigitalInput isDown;
    DigitalInput isUp;
    Talon actuator;
    Talon roller;

  public HatchIntake(){
    Joy = new Joystick(0);
    Tim1 = new Timer();
    isDown = new DigitalInput(constants.HATCH_INTAKE_DOWN);
    isUp = new DigitalInput(constants.HATCH_INTAKE_UP);
    actuator = new Talon(constants.HATCH_INTAKE_ACTUATOR);
    roller = new Talon(constants.HATCH_INTAKE_ROLLER); 
  }

  public static HatchIntake getInstance() {
    return instance; 
  }

  public boolean IntakeUp() {
    if(isDown.get()) {
        actuator.set(0);
        return true; 
    }
    else {
        actuator.set(-.3);
        return false; 
    }
  }

  public boolean IntakeDown() {
    if(isUp.get()) {
        actuator.set(0);
        return true;
    }
    else {
        actuator.set(.3);
        return false;
    }
  }
  public void IntoIntake(double speed) { //Sensor//
    roller.set(speed); 
  }
  public void ZeroSensors() {
    //When the sensor is added//
  }
  public void OutputToDash() {
    // 
  }
}