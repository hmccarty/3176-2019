/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Anna is also doing this.
 */
public class CargoIntake {
    
    private static CargoIntake instance = new CargoIntake();
    Talon inMotor;
    Talon upMotor;
    Joystick stick;
    
    public static CargoIntake getInstance() {
        return instance;
    }
    
    private CargoIntake() {
        inMotor = new Talon(0);
        upMotor = new Talon(1);
        stick = new Joystick(0);
    }

    public void checkButtons() {
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
    
    public void cargoIntake() {
        
    }
}
