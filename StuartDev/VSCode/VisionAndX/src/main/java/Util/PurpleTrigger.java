package Util;

import edu.wpi.first.wpilibj.Joystick;

public class PurpleTrigger {
	private boolean currentState;
	private Joystick joyStick;
	private int buttonID;

	public PurpleTrigger(Joystick joyStick, int buttonID) {
		this.joyStick = joyStick;
		this.buttonID = buttonID;
		currentState = false;
	}

	public boolean getTrigger() {
		if(joyStick.getRawButton(buttonID)) { //If joystick button was pressed, then reverse current state
			if(currentState == false) {currentState = true;}
			else {currentState = false;}
		}
		return currentState;
	}
	
	public void setFalse() {
		currentState = false;
	}
}
