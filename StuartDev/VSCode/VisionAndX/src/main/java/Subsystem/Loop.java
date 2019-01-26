package Subsystem;

public abstract class Loop {
	/* Each subsystem will have the onStart() called after the constructor.
	 * The loop() method will be called every 20 ms in the main robot class. 
	 * The stop method will be called when the robot is disabled  
	 */
	public abstract void onStart();
	public abstract void onloop();
	public abstract void stop();
	public void test() {
		// TODO Auto-generated method stub
		
	}
}
