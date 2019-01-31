/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Kyle is doing this.
 */
public class HatchIntake {
    
    public static HatchIntake instance = new HatchIntake();
    Talon inMotor;
    Talon upMotor;
    Joystick stick;
    
    public static HatchIntake getInstance() {
        return instance;
    }
    
    public static HatchIntake() {
        inMotor = new Talon();
        upMotor = new Talon();
        stick = new Joystick();
    }

    public void checkButtons() {
        switch (Boolean.toString(stick.getRawButtonPressed(10))) {
            case "true":
                System.out.println("Button 10 is pressed.");
                break;
            case "false":
                System.out.println("Button 10 is not pressed.");
        }
        switch (Boolean.toString(stick.getRawButtonPressed(11))) {
            case "true":
                System.out.println("Button 11 is pressed.");
                break;
            case "false":
                System.out.println("Button 11 is not pressed.");
        }
        switch (Boolean.toString(stick.getRawButtonPressed(12))) {
            case "true":
                System.out.println("Button 12 is pressed.");
                break;
            case "false":
                System.out.println("Button 12 is not pressed.");
        }
        System.out.println("All button values are recognized.");
    }

    public void hatchFunction() {

    }
}
