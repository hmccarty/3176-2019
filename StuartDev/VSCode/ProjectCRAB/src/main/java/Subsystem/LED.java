package Subsystem;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LED extends Subsystem{
	private static LED instance = new LED(); 
	private I2C arduino;
	private byte[] toSend = new byte[1];
	private byte[] emptyArray = new byte[1];
	
	private LED() {
		arduino = new I2C(I2C.Port.kOnboard, 9);
	}
	
	public enum ledStates{
		CUBE_INTAKED,
		INTAKE_STOWED,
		LIGHTSHOW
	}
	
	private ledStates wantedState; 
	private ledStates currState;
	
	public static LED getInstance() {
		return instance; 
	}
	public void setWantedState(ledStates wantedState) {this.wantedState = wantedState;}
	@Override public void zeroAllSensors() {}
	
	private void sendToArduino(byte value) {
		toSend[0] = (value);
		arduino.transaction(toSend, 1, emptyArray, 1);
	}

	@Override
	public boolean checkSystem() {
		// TODO Auto-generated method stub
		return false;
	}

 	private void checkState()
 	{
 		if(wantedState != currState)
		{
			currState = wantedState;
		}
 	}

	@Override
	public void registerLoop() {
		Loop_Manager.getInstance().addLoop(new Loop() {
			@Override
			public void onStart() {
				currState = ledStates.LIGHTSHOW;
			}
			@Override
			public void onloop() {
				SmartDashboard.putString("LED State", currState.toString());
				
				
				//sendToArduino((byte)0);
				switch(currState) {
					case CUBE_INTAKED:
						sendToArduino((byte)1);
						checkState();
						break;
					case INTAKE_STOWED:
						sendToArduino((byte)2);
						checkState();
						break;
					case LIGHTSHOW:
						sendToArduino((byte)0);
						checkState();
						break;
				}
			}	
			@Override
			public void stop() {				
			}
		});	
		}
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putString("LED state", currState.toString());
		
	}	
}

