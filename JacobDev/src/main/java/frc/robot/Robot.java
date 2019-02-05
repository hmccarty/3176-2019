
package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;

public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  SmartDashboard dashboard = new SmartDashboard();
  Ultrasonic ballDetector = new Ultrasonic(0, 1); //Ultrasonic(pingChannel, echoChannel);
  private double range;
  Compressor compressor = new Compressor();
  DigitalInput yeet = new DigitalInput(0);
  AnalogInput ai;
  DigitalInput di;


  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    ballDetector.setAutomaticMode(true);
    ballDetector.setEnabled(true);
    

    ai = new AnalogInput(0);
    di = new DigitalInput(9);

  }

  
  @Override
  public void robotPeriodic()
  {
    
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic()

  {
    // if (ai.getVoltage() > 4.5){
    //   isSignal = true;
    // }
    //System.out.println(ai.getVoltage());
    System.out.println(di.get());
  {   

    System.out.println(yeet.get());
    System.out.println(ballDetector.getRangeInches());
    compressor.stop();

    System.out.println(ballDetector.getRangeInches());
  }

  }

  @Override
  public void testPeriodic() 
  {

  }
}