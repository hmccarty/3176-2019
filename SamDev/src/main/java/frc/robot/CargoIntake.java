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
    Talon inMotor = new Talon(0);
    Talon upMotor = new Talon(1);
    Joystick stick = new Joystick(0);

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
        System.out.println("All button values are recognized.");
    }
    
    public void cargoIntake() {
        while (stick.getRawButtonPressed(11) == true) /*camera recognizes cargo near enough to intake*/ {
            upMotor.set(-1);
            if (true) /*sensor input*/ {
                upMotor.set(0);
                break;
            }
        }
    }
}