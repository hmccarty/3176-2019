/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class HatchIntake {
    static HatchIntake instance = new HatchIntake();
    Joystick Joy;
    Talon Tal1 = new Talon(0);
    Talon Tal2 = new Talon(1);
    Timer Tim1 = new Timer();

  public HatchIntake(){
    Joy = new Joystick(0);
  }

  public static HatchIntake getInstance() {
    return instance; 
  }
  public void HatchIng() {      
    while (Joy.getRawButtonPressed(10) == true) {
      Tal1.set(1);
      if (Joy.getRawButton(10) == false/*sensor*/) {
        Tal1.set(0);
        break;
      }
    }
    if (Joy.getRawButtonPressed(11) == true) {
      Tim1.start();
      if (Tim1.get() < 7) {
        Tal2.set(-1);
      }
      else {
        Tal2.set(0);
        Tim1.stop();
        Tim1.reset();
      }
    }
    if (Joy.getRawButtonPressed(12) == true) {
      Tim1.start();
      if (Tim1.get() < 7) {
        Tal2.set(1);
      }
      else {
        Tal2.set(0);
        Tim1.stop();
        Tim1.reset();
      }
    }
  }
}