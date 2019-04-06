package frc.subsystem;

// import edu.wpi.first.wpilibj.Talon;
// import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.constants;
// import frc.subsystem.controller;

// import edu.wpi.first.wpilibj.DoubleSolenoid;

// CURRENTLY NOT IN USE

// public class hatchintake {
    
//     private static hatchintake instance = new hatchintake();
//     private DigitalInput mHasHatch;
//     private Talon mRoller;
//     private DoubleSolenoid mActuator;

//     public static hatchintake getInstance() {
//         return instance;
//     }
    
//     public hatchintake() {
//         mRoller = new Talon(constants.HATCH_INTAKE_ROLLER);
//         mActuator = new DoubleSolenoid(1, constants.HATCH_INTAKE_ACTUATOR_FRONT, constants.HATCH_INTAKE_ACTUATOR_BACK);

//         mHasHatch = new DigitalInput(constants.HATCH_IN_INTAKE);
//     }

//     // public boolean getUp() {
//     //     return isUp.get();
//     // }

//     // public boolean getDown() {
//     //     return isDown.get();
//     // }
    
//     // public boolean getSensor() {
//     //     return sensor.get();
//     // }

//     // public void deployIntake() {
//     //     mActuator.set(value.kForward);
//     // }

//     // public void stowIntake() {
//     //     if (getUp()) {
//     //         dSol.set(DoubleSolenoid.Value.kForward);
//     //         getUp();
//     //     }
//     //     else {
//     //         dSol.set(DoubleSolenoid.Value.kOff);
//     //         getUp();
//     //     }
//     // }

//     // public void runIntake(double speed) {
//     //     if (sensor.get()) {
//     //         mRoller.set(0);
//     //         getSensor();
//     //     }
//     //     else {
//     //         mRoller.set(speed);
//     //         getSensor();
//     //     }
//     // }

//     // public void zeroAll() {
//     //     dSol.set(DoubleSolenoid.Value.kOff);
//     //     mRoller.set(0);
//     // }

//     // public void outputToSmartDashboard() {
//     //     SmartDashboard.putBoolean("isDown value: ", getDown());
//     //     SmartDashboard.putBoolean("isUp value: ", getUp());
//     //     SmartDashboard.putBoolean("sensor value: ", getSensor());
//     // }
// }