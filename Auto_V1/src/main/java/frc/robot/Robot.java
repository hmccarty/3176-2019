package frc.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.networktables.*;


public class Robot extends SampleRobot {
  
 
 
  //Global accees to center values 
  NetworkTableEntry x;
  NetworkTableEntry y;
  NetworkTableEntry radius;
  public Robot() {
    
  }

  @Override
  public void robotInit() {

  // Set up and populate the networkTable
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  NetworkTable table = inst.getTable("SmartDashboard");
  x = table.getEntry("X");
  y = table.getEntry("Y");
  radius = table.getEntry("R");
  //System.out.print(x.getType());
  //System.out.println(y.getType());
   }
   
   
  
   @Override
  
  public void autonomous() {     
     
    }
  public void operatorControl(){
    while(true){
      teleopPeriodic();
    }
  }

  
  public void teleopPeriodic() {
    System.out.println(x.getDouble(-1));
  }

  /**
   * Runs during test mode.
   */
  

}

