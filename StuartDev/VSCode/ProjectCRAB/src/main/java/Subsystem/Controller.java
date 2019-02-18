package Subsystem;


import org.usfirst.frc.team6713.robot.*;
import edu.wpi.first.wpilibj.Joystick;

/** 
*  Handles all the driver input, and controller deadband
*/

public class Controller extends Subsystem {
	
	private static Controller instance = new Controller(); 
	private Joystick velocityStick;
	private Joystick thetaStick;
	private Joystick buttonMonkey;
	
	private Joystick velocityStick_slave;
	private Joystick thetaStick_slave;
	
	boolean isMaster = false; 
	//public static PurpleTrigger vision;
	
	public Controller() {
		velocityStick = new Joystick(Constants.DRIVE_JOYSTICK);
		thetaStick = new Joystick(Constants.GEAR_JOYSTICK);
		buttonMonkey = new Joystick(Constants.BUTTON_MONKEY);
		
		velocityStick_slave = new Joystick(Constants.DRIVE_JOYSTICK + 3);
		thetaStick_slave = new Joystick(Constants.GEAR_JOYSTICK + 3); 
		//vision = new PurpleTrigger(velocityStick, 2);
	}
	
	public enum driveStationStates {
		ALL,
		DRIVER,
		PROGRAMMING
	}
	
	public static Controller getInstance() {
		return instance; 
	}
	
	public void checkIsMaster() {
		if(velocityStick.getY()+velocityStick.getX()+thetaStick.getX() == 0) {
			isMaster = false;
		}
		else {
			isMaster = true;
		}
	}
	
	/** 
	 * @return double deadbanded Y axis
	 */
	public double getForward() {
		if(Math.abs(velocityStick.getY())<.06) {
			return 0.0;
		}
		else {
			if(isMaster) {return Math.pow(velocityStick.getY(),1);}
			else {return Math.pow(velocityStick_slave.getY(), 1)*.7;}
		}
	}
	
	/** 
	 * @return double deadbanded X axis
	 */
	public double getStrafe() {
		if(Math.abs(velocityStick.getX())<.06) {
			return 0;
		}
		else {
			if(isMaster) {return Math.pow(velocityStick.getX(),1);}
			else {return Math.pow(velocityStick_slave.getX(), 1)*.7;}
		}
	}
	
	/**
	 * @return double deadbanded X axis
	 */
	public double getRotation() {
		if(Math.abs(thetaStick.getX())<.06) {
			return 0.0;
		}
		else {
			if(isMaster) { return Math.pow(thetaStick.getX(),1)/6.0;}
			else {return Math.pow(thetaStick_slave.getX(), 1)/7.0;}
		}
	}
	
	public double elevatorPositionJoystick() {return -buttonMonkey.getThrottle();}
	public boolean elevatorResetEncoder() {return buttonMonkey.getRawButton(12);};
	public boolean elevatorHigh() {return buttonMonkey.getRawButton(8);}
	public boolean elevatorMid() {return buttonMonkey.getRawButton(6);}
	public boolean elevatorSwitch() {return buttonMonkey.getRawButton(12);};
	public boolean elevatorLow() {return buttonMonkey.getRawButton(3);}
	
	public double getintakePositionJoystick() {return buttonMonkey.getY();}
	public boolean getOuttakeButton() {return buttonMonkey.getRawButton(4);}//||velocityStick.getRawButton(6);}
	public boolean getSlowOuttakeButton() {return buttonMonkey.getRawButton(1);}
	public boolean getIntakeButton() {return buttonMonkey.getRawButton(2);}//||velocityStick.getRawButton(4);}
	public boolean unjamButton() {return buttonMonkey.getRawButton(10);}
	public boolean Stow() {return buttonMonkey.getRawButton(5);}//||thetaStick.getRawButton(4);}
	public boolean unStow() {return buttonMonkey.getRawButton(7);}//||thetaStick.getRawButton(3);}
	public boolean solenoidOut() {return buttonMonkey.getRawButton(9);}
	
	public int getPOVButton() {return thetaStick.getPOV();}
	public boolean getGyroResetButton() {return velocityStick.getRawButton(3);}
	public boolean getSlowFieldCentricButton() {return velocityStick.getRawButton(1);}
	public boolean getSlowRobotCentricButton() {return thetaStick.getRawButton(1);}
	public boolean getVisionTracking() {return velocityStick.getRawButton(2);}//return velocityStick.getRawButton(2);}
	
	public boolean executeAutoButton() {return false;}//velocityStick.getRawButton();}
	public boolean resetAutoButton() {return false;}// velocityStick.getRawButton(3);}
	public boolean forwardTest() {return velocityStick.getRawButton(4);}
	public boolean secretSauce() {return velocityStick.getRawButton(5);}

	@Override
	public void zeroAllSensors() {
	}

	@Override
	public boolean checkSystem() {
		return false;
	}

	@Override
	public void registerLoop() {
	}
	
	@Override
	public void outputToSmartDashboard() {
	}
}

