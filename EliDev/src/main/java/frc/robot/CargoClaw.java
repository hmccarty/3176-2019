package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;

public class CargoClaw extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick Stick = new Joystick(0);
  Solenoid grabber1 = new Solenoid(2);
  Solenoid grabber2 = new Solenoid(3); 
  Solenoid mover1 = new Solenoid(0);
  Solenoid mover2 = new Solenoid(1);
  int cycler = 1;
  Timer timer =  new Timer();

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

  
  @Override
  public void teleopPeriodic()
  {   
    
  }
  
  public void loop()
  {
    if(Stick.getRawButtonPressed(5)){
      cycler = 1;
    }
    else if (Stick.getRawButtonPressed(4)){
      cycler = 2;
    }
    switch(cycler){
      case 1:
        outtake();
        break;
      case 2:
        intake();
        break;
    }
  }

  public void intake()
  {
    //put in some garbage for sensors.
    setGrabber(true);
    if(timer.get() > 0.5)
    {
      setMover(false);
      timer.reset();
    }
 }


  public void outtake()
  {
    setMover(true);
    if(timer.get() > 2.0)
    {
      setGrabber(false);
      timer.reset();
    }
  }

  public void setGrabber(boolean on)
  {
    if(on)
    {
      grabber1.set(true);
      grabber2.set(false);
    }
    else
    {
      grabber1.set(false);
      grabber2.set(true);
    }
  }

  public void setMover(boolean on)
  {
      if(on)
      {
          mover1.set(true);
          mover2.set(false);
      }
      else
      {
        mover1.set(false);
        mover2.set(true);
      }
  }
  
  @Override
  public void testPeriodic() 
  {

  }
}
