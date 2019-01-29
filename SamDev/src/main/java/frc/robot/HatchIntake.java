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
 * Add your docs here.
 */
public class HatchIntake {
    Talon inMotor = new Talon(0);
    Talon upMotor = new Talon(1);
    Joystick stick = new Joystick(0);
    boolean intakeButton = stick.getRawButton(12);
    boolean sendButton = stick.getRawButton(13);

    if (getButton()) {

    }
}
