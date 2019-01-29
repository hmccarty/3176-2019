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
public class cargo_intake {

   Talon motorIn = new Talon(0); 
   Talon motorPivot = new Talon(1);
   Joystick stick =  new Joystick(0);
   Button intakeButton = new JoystickButton(stick, 0),
       pivotButton = new JoystickButton(stick, 1);

    if /* see cargo close enough */{
        //input code relative to pseudocode
    }
}
