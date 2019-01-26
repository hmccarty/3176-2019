
package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick Stick = new Joystick(0);
  Compressor Compressor = new Compressor();
  Solenoid blue1 = new Solenoid(2);
  Solenoid blue2 = new Solenoid(3);
  Solenoid purple1 = new Solenoid(0);
  Solenoid purple2 = new Solenoid(1);  
  Timer timer =  new Timer();
  int cycler = 1;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    timer.start();
    timer.reset();
  }

  
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
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
    if(Stick.getRawButtonPressed(5)){
      timer.reset();
      cycler = 1;
    }
    else if (Stick.getRawButtonPressed(3)){
      cycler = 2;
    }
    else if (Stick.getRawButtonPressed(4)){
      cycler = 3; 
    }
    switch(cycler){
      case 1:
        outtake();
        break; 
      case 2:
        hold();
        break;
      case 3:
        intake();
        break;
    }
    
    if(Stick.getRawButton(2))
    {
      Compressor.stop();
    }
    else
    {
      Compressor.start();
    }
  }

  public void intake()
  {
    setBlue(true);
    setPurple(false);
  }

  public void hold()
  {
    setPurple(true);
  }

  public void outtake()
  {
    
    setBlue(false);
    if(timer.get() > 0.5)
    {
      setPurple(false);
      timer.reset();
    }
  }

  public void setBlue(boolean on)
  {
    if(on)
    {
      blue1.set(true);
      blue2.set(false);
    }
    else
    {
      blue1.set(false);
      blue2.set(true);
    }
  }

  public void setPurple(boolean on)
  {
    if(on)
    {
      purple1.set(true);
      purple2.set(false);
    }
    else
    {
      purple1.set(false);
      purple2.set(true);
    }
  }

  @Override
  public void testPeriodic() 
  {

  }
}
